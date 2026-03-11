package com.pfm.budget.service;

import com.pfm.budget.dto.BudgetSummaryDto;
import com.pfm.budget.entity.BudgetCategoryLimit;
import com.pfm.budget.entity.BudgetIncomePlanItem;
import com.pfm.budget.entity.BudgetPlan;
import com.pfm.budget.rule.BudgetComputationContext;
import com.pfm.budget.rule.BudgetWarningEngine;
import com.pfm.transaction.enumtype.TransactionType;
import com.pfm.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetSummaryService {
    private final BudgetService budgetService;
    private final TransactionRepository txRepo;
    private final BudgetWarningEngine warningEngine;

    public BudgetSummaryDto buildSummary(Long userId, String monthKey) {
        BudgetPlan plan = budgetService.getOrCreatePlan(userId, monthKey, "USD");
        LocalDate d = LocalDate.parse(monthKey + "-01");
        LocalDateTime start = d.atStartOfDay();
        LocalDateTime end = d.plusMonths(1).atStartOfDay();

        BigDecimal actualIncome = txRepo.sumAmountByTypeInPeriod(userId, TransactionType.income, start, end);
        BigDecimal actualExpense = txRepo.sumAmountByTypeInPeriod(userId, TransactionType.expense, start, end);

        List<BudgetIncomePlanItem> incomes = budgetService.incomeItems(plan.getId());
        List<BudgetCategoryLimit> limits = budgetService.categoryLimits(plan.getId());

        Map<String, BigDecimal> actualIncomeByCategory = txRepo.sumByCategory(userId, TransactionType.income, start, end).stream()
                .collect(Collectors.toMap(o -> (String) o[0], o -> (BigDecimal) o[1]));
        Map<String, BigDecimal> actualExpenseByCategory = txRepo.sumByCategory(userId, TransactionType.expense, start, end).stream()
                .collect(Collectors.toMap(o -> (String) o[0], o -> (BigDecimal) o[1]));

        List<BudgetSummaryDto.IncomeRow> incomeRows = incomes.stream().map(i -> {
            BigDecimal actual = actualIncomeByCategory.getOrDefault(i.getCategory(), BigDecimal.ZERO);
            return new BudgetSummaryDto.IncomeRow(i.getCategory(), i.getPlannedAmount(), actual, actual.subtract(i.getPlannedAmount()));
        }).toList();

        List<BudgetSummaryDto.ExpenseRow> expenseRows = new ArrayList<>();
        for (BudgetCategoryLimit l : limits) {
            BigDecimal actual = actualExpenseByCategory.getOrDefault(l.getCategory(), BigDecimal.ZERO);
            BigDecimal remaining = l.getLimitAmount().subtract(actual);
            BigDecimal progress = l.getLimitAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : actual.multiply(BigDecimal.valueOf(100)).divide(l.getLimitAmount(), 2, RoundingMode.HALF_UP);
            String status = progress.compareTo(BigDecimal.valueOf(100)) >= 0 ? "danger" : progress.compareTo(BigDecimal.valueOf(80)) >= 0 ? "warning" : "ok";
            expenseRows.add(new BudgetSummaryDto.ExpenseRow(l.getCategory(), l.getLimitAmount(), actual, remaining, progress, status));
        }

        BudgetSummaryDto.Summary summary = new BudgetSummaryDto.Summary(
                plan.getPlannedIncomeTotal(),
                actualIncome,
                plan.getPlannedExpenseTotal(),
                actualExpense,
                plan.getPlannedIncomeTotal().subtract(plan.getPlannedExpenseTotal()),
                actualIncome.subtract(actualExpense)
        );

        List<BudgetSummaryDto.WarningRow> warnings = warningEngine.evaluate(new BudgetComputationContext(summary, expenseRows));
        return new BudgetSummaryDto(summary, incomeRows, expenseRows, warnings);
    }
}
