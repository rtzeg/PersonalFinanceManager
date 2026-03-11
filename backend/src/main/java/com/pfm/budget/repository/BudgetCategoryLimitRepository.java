package com.pfm.budget.repository;

import com.pfm.budget.entity.BudgetCategoryLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetCategoryLimitRepository extends JpaRepository<BudgetCategoryLimit, Long> {
    List<BudgetCategoryLimit> findByBudgetPlanId(Long budgetPlanId);
}
