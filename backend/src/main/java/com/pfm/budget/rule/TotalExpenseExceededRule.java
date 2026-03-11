package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TotalExpenseExceededRule implements BudgetWarningRule {
    @Override
    public List<BudgetSummaryDto.WarningRow> evaluate(BudgetComputationContext context) {
        if (context.summary().actualExpense().compareTo(context.summary().plannedExpense()) > 0) {
            return List.of(new BudgetSummaryDto.WarningRow("TOTAL_EXPENSE_EXCEEDED", "warning", "Фактические расходы превысили план"));
        }
        return List.of();
    }
}
