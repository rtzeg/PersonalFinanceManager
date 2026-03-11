package com.pfm.budget.service;

import com.pfm.budget.entity.BudgetCategoryLimit;
import com.pfm.budget.entity.BudgetIncomePlanItem;
import com.pfm.budget.entity.BudgetPlan;
import com.pfm.budget.repository.BudgetCategoryLimitRepository;
import com.pfm.budget.repository.BudgetIncomePlanItemRepository;
import com.pfm.budget.repository.BudgetPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetPlanRepository planRepository;
    private final BudgetIncomePlanItemRepository incomeItemRepository;
    private final BudgetCategoryLimitRepository limitRepository;

    public BudgetPlan getOrCreatePlan(Long userId, String monthKey, String currency) {
        return planRepository.findByUserIdAndMonthKey(userId, monthKey)
                .orElseGet(() -> {
                    BudgetPlan bp = new BudgetPlan();
                    bp.setUserId(userId);
                    bp.setMonthKey(monthKey);
                    bp.setCurrency(currency);
                    return planRepository.save(bp);
                });
    }

    public BudgetPlan savePlan(BudgetPlan plan) { return planRepository.save(plan); }
    public List<BudgetIncomePlanItem> incomeItems(Long budgetId) { return incomeItemRepository.findByBudgetPlanId(budgetId); }
    public BudgetIncomePlanItem saveIncomeItem(BudgetIncomePlanItem item) { return incomeItemRepository.save(item); }
    public void deleteIncomeItem(Long id) { incomeItemRepository.deleteById(id); }
    public List<BudgetCategoryLimit> categoryLimits(Long budgetId) { return limitRepository.findByBudgetPlanId(budgetId); }
    public BudgetCategoryLimit saveCategoryLimit(BudgetCategoryLimit item) { return limitRepository.save(item); }
    public void deleteCategoryLimit(Long id) { limitRepository.deleteById(id); }
}
