package com.pfm.analytics.controller;

import com.pfm.analytics.service.AnalyticsService;
import com.pfm.budget.dto.BudgetSummaryDto;
import com.pfm.budget.service.BudgetSummaryService;
import com.pfm.common.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService service;
    private final BudgetSummaryService budgetSummaryService;
    private final CurrentUserService current;

    @GetMapping("/overview") public Map<String,Object> overview(@RequestParam(defaultValue = "month") String period){return service.overview(current.getUserId(), period);}    
    @GetMapping("/categories") public Object categories(@RequestParam(defaultValue = "month") String period){return service.categories(current.getUserId(), period);}    
    @GetMapping("/income-vs-expense") public Map<String,Object> incomeVsExpense(@RequestParam(defaultValue = "month") String period){return service.overview(current.getUserId(), period);}    
    @GetMapping("/balance-trend") public Map<String,Object> balanceTrend(@RequestParam(defaultValue = "month") String period){return Map.of("period",period,"points",service.categories(current.getUserId(), period));}    
    @GetMapping("/budget-vs-actual") public BudgetSummaryDto budgetVsActual(@RequestParam String month){return budgetSummaryService.buildSummary(current.getUserId(), month);}    
}
