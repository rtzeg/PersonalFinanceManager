package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;

import java.util.List;

public record BudgetComputationContext(
        BudgetSummaryDto.Summary summary,
        List<BudgetSummaryDto.ExpenseRow> expenseRows
) {}
