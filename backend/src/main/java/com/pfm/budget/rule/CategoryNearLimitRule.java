package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryNearLimitRule implements BudgetWarningRule {
    @Override
    public List<BudgetSummaryDto.WarningRow> evaluate(BudgetComputationContext context) {
        List<BudgetSummaryDto.WarningRow> warnings = new ArrayList<>();
        for (BudgetSummaryDto.ExpenseRow r : context.expenseRows()) {
            if (r.limit().compareTo(BigDecimal.ZERO) > 0 && r.progress().compareTo(BigDecimal.valueOf(80)) >= 0 && r.progress().compareTo(BigDecimal.valueOf(100)) < 0) {
                warnings.add(new BudgetSummaryDto.WarningRow("CATEGORY_80", "warning", "Категория \"" + r.category() + "\" достигла 80% лимита"));
            }
        }
        return warnings;
    }
}
