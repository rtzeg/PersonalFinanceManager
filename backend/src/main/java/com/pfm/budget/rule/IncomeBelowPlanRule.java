package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncomeBelowPlanRule implements BudgetWarningRule {
    @Override
    public List<BudgetSummaryDto.WarningRow> evaluate(BudgetComputationContext context) {
        if (context.summary().actualIncome().compareTo(context.summary().plannedIncome()) < 0) {
            return List.of(new BudgetSummaryDto.WarningRow("INCOME_BELOW_PLAN", "info", "Фактический доход ниже плана"));
        }
        return List.of();
    }
}
