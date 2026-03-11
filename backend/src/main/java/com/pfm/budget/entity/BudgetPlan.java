package com.pfm.budget.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budget_plans", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "month_key"}))
public class BudgetPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "month_key", nullable = false)
    private String monthKey;
    private String currency;
    @Column(name = "planned_income_total")
    private BigDecimal plannedIncomeTotal = BigDecimal.ZERO;
    @Column(name = "planned_expense_total")
    private BigDecimal plannedExpenseTotal = BigDecimal.ZERO;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
}
