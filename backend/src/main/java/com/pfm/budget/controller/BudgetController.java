package com.pfm.budget.controller;

import com.pfm.budget.dto.BudgetSummaryDto;
import com.pfm.budget.entity.BudgetCategoryLimit;
import com.pfm.budget.entity.BudgetIncomePlanItem;
import com.pfm.budget.entity.BudgetPlan;
import com.pfm.budget.service.BudgetService;
import com.pfm.budget.service.BudgetSummaryService;
import com.pfm.common.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService service;
    private final BudgetSummaryService summaryService;
    private final CurrentUserService current;

    @GetMapping("/api/budgets")
    public BudgetPlan get(@RequestParam String month) { return service.getOrCreatePlan(current.getUserId(), month, "USD"); }
    @PostMapping("/api/budgets")
    public BudgetPlan create(@RequestBody BudgetPlan plan){plan.setUserId(current.getUserId());return service.savePlan(plan);}    
    @PutMapping("/api/budgets/{id}")
    public BudgetPlan update(@PathVariable Long id,@RequestBody BudgetPlan plan){plan.setId(id);plan.setUserId(current.getUserId());return service.savePlan(plan);}    

    @GetMapping("/api/budgets/{budgetId}/income-items")
    public List<BudgetIncomePlanItem> incomeItems(@PathVariable Long budgetId){return service.incomeItems(budgetId);}    
    @PostMapping("/api/budgets/{budgetId}/income-items")
    public BudgetIncomePlanItem createIncomeItem(@PathVariable Long budgetId,@RequestBody BudgetIncomePlanItem item){item.setBudgetPlanId(budgetId);return service.saveIncomeItem(item);}    
    @PutMapping("/api/budget-income-items/{id}")
    public BudgetIncomePlanItem updateIncomeItem(@PathVariable Long id,@RequestBody BudgetIncomePlanItem item){item.setId(id);return service.saveIncomeItem(item);}    
    @DeleteMapping("/api/budget-income-items/{id}")
    public void deleteIncomeItem(@PathVariable Long id){service.deleteIncomeItem(id);}    

    @GetMapping("/api/budgets/{budgetId}/category-limits")
    public List<BudgetCategoryLimit> limits(@PathVariable Long budgetId){return service.categoryLimits(budgetId);}    
    @PostMapping("/api/budgets/{budgetId}/category-limits")
    public BudgetCategoryLimit createLimit(@PathVariable Long budgetId,@RequestBody BudgetCategoryLimit limit){limit.setBudgetPlanId(budgetId);return service.saveCategoryLimit(limit);}    
    @PutMapping("/api/budget-category-limits/{id}")
    public BudgetCategoryLimit updateLimit(@PathVariable Long id,@RequestBody BudgetCategoryLimit limit){limit.setId(id);return service.saveCategoryLimit(limit);}    
    @DeleteMapping("/api/budget-category-limits/{id}")
    public void deleteLimit(@PathVariable Long id){service.deleteCategoryLimit(id);}    

    @GetMapping("/api/budgets/summary")
    public BudgetSummaryDto summary(@RequestParam String month){return summaryService.buildSummary(current.getUserId(), month);}    
}
