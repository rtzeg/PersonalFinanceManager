package com.pfm.budget.repository;

import com.pfm.budget.entity.BudgetIncomePlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetIncomePlanItemRepository extends JpaRepository<BudgetIncomePlanItem, Long> {
    List<BudgetIncomePlanItem> findByBudgetPlanId(Long budgetPlanId);
}
