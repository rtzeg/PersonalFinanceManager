package com.pfm.budget.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budget_category_limits", uniqueConstraints = @UniqueConstraint(columnNames = {"budget_plan_id", "category"}))
public class BudgetCategoryLimit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "budget_plan_id")
    private Long budgetPlanId;
    private String category;
    @Column(name = "limit_amount")
    private BigDecimal limitAmount = BigDecimal.ZERO;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
}
