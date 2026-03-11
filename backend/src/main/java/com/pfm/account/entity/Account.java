package com.pfm.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, length = 20)
    private String type;
    @Column(nullable = false, length = 10)
    private String currency;
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
    private String color;
    @Column(name = "card_network")
    private String cardNetwork;
    @Column(name = "card_number_masked")
    private String cardNumberMasked;
    @Column(name = "card_number_full")
    private String cardNumberFull;
    @Column(name = "expiry_date")
    private String expiryDate;
    @Column(name = "included_in_balance", nullable = false)
    private boolean includedInBalance = true;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PreUpdate
    void preUpdate() { updatedAt = LocalDateTime.now(); }
}
