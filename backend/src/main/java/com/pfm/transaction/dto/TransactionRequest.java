package com.pfm.transaction.dto;

import com.pfm.transaction.enumtype.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(TransactionType type, BigDecimal amount, String currency, String category,
                                 String description, String note, Long accountId, Long toAccountId,
                                 String toCurrency, BigDecimal toAmount, LocalDateTime occurredAt) {
}
