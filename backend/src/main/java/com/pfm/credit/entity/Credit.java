package com.pfm.credit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "credits")
public class Credit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    private String title;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    private String currency;
    private String kind;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    private Integer months;
    @Column(name = "paid_installments")
    private Integer paidInstallments = 0;
    private String status;
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
}
