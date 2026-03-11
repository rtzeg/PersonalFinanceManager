package com.pfm.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetSummaryDto(
        Summary summary,
        List<IncomeRow> incomeRows,
        List<ExpenseRow> expenseRows,
        List<WarningRow> warnings
) {
    public record Summary(BigDecimal plannedIncome, BigDecimal actualIncome, BigDecimal plannedExpense,
                          BigDecimal actualExpense, BigDecimal projectedSavings, BigDecimal actualNet) {}
    public record IncomeRow(String category, BigDecimal planned, BigDecimal actual, BigDecimal diff) {}
    public record ExpenseRow(String category, BigDecimal limit, BigDecimal actual, BigDecimal remaining,
                             BigDecimal progress, String status) {}
    public record WarningRow(String code, String severity, String message) {}
}
