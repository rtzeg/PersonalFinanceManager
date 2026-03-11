package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BudgetWarningEngine {
    private final List<BudgetWarningRule> rules;

    public List<BudgetSummaryDto.WarningRow> evaluate(BudgetComputationContext context) {
        List<BudgetSummaryDto.WarningRow> all = new ArrayList<>();
        for (BudgetWarningRule rule : rules) {
            all.addAll(rule.evaluate(context));
        }
        return all;
    }
}
