package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryExceededRule implements BudgetWarningRule {
    @Override
    public List<BudgetSummaryDto.WarningRow> evaluate(BudgetComputationContext context) {
        List<BudgetSummaryDto.WarningRow> warnings = new ArrayList<>();
        for (BudgetSummaryDto.ExpenseRow r : context.expenseRows()) {
            if (r.remaining().compareTo(BigDecimal.ZERO) < 0) {
                warnings.add(new BudgetSummaryDto.WarningRow("CATEGORY_EXCEEDED", "warning", "Превышен лимит по категории \"" + r.category() + "\""));
            }
        }
        return warnings;
    }
}
