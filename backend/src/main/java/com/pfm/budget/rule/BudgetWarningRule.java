package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;

import java.util.List;

public interface BudgetWarningRule {
    List<BudgetSummaryDto.WarningRow> evaluate(BudgetComputationContext context);
}
