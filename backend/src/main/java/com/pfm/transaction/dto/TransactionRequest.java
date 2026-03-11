package com.pfm.transaction.dto;

import com.pfm.transaction.enumtype.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(@NotNull TransactionType type,
                                 @NotNull BigDecimal amount,
                                 @NotBlank String currency,
                                 @NotBlank String category,
                                 @NotBlank String description,
                                 String note,
                                 @NotNull Long accountId,
                                 Long toAccountId,
                                 String toCurrency, BigDecimal toAmount, LocalDateTime occurredAt) {
}
