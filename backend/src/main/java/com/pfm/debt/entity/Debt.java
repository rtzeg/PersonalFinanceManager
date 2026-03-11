package com.pfm.debt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "debts")
public class Debt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    private String name;
    private BigDecimal amount;
    private String currency;
    private String type;
    private String status;
    private String description;
    @Column(name = "debt_date")
    private LocalDate debtDate;
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
}
