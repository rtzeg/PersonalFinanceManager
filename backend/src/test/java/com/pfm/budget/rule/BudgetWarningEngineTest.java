package com.pfm.budget.rule;

import com.pfm.budget.dto.BudgetSummaryDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class BudgetWarningEngineTest {

    @Test
    void engineShouldCollectWarningsFromRules() {
        BudgetWarningEngine engine = new BudgetWarningEngine(List.of(
                new CategoryNearLimitRule(),
                new CategoryExceededRule(),
                new TotalExpenseExceededRule(),
                new IncomeBelowPlanRule()
        ));

        BudgetSummaryDto.Summary summary = new BudgetSummaryDto.Summary(
                new BigDecimal("1000"),
                new BigDecimal("900"),
                new BigDecimal("500"),
                new BigDecimal("600"),
                new BigDecimal("500"),
                new BigDecimal("300")
        );

        List<BudgetSummaryDto.ExpenseRow> rows = List.of(
                new BudgetSummaryDto.ExpenseRow("Food", new BigDecimal("100"), new BigDecimal("90"), new BigDecimal("10"), new BigDecimal("90"), "warning"),
                new BudgetSummaryDto.ExpenseRow("Transport", new BigDecimal("100"), new BigDecimal("120"), new BigDecimal("-20"), new BigDecimal("120"), "danger")
        );

        List<BudgetSummaryDto.WarningRow> warnings = engine.evaluate(new BudgetComputationContext(summary, rows));

        assertFalse(warnings.isEmpty());
    }
}
