package com.pfm.budget.repository;

import com.pfm.budget.entity.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long> {
    Optional<BudgetPlan> findByUserIdAndMonthKey(Long userId, String monthKey);
}
