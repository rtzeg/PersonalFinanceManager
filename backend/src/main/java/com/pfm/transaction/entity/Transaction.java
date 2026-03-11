package com.pfm.transaction.entity;

import com.pfm.transaction.enumtype.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false, length = 10)
    private String currency;
    @Column(nullable = false, length = 100)
    private String category;
    @Column(nullable = false, length = 255)
    private String description;
    @Column(columnDefinition = "text")
    private String note;
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    @Column(name = "to_account_id")
    private Long toAccountId;
    @Column(name = "to_currency")
    private String toCurrency;
    @Column(name = "to_amount")
    private BigDecimal toAmount;
    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PreUpdate
    void preUpdate() { updatedAt = LocalDateTime.now(); }
}
