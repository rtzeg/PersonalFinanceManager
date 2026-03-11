package com.pfm.account.dto;

import java.math.BigDecimal;

public record AccountRequest(String name, String type, String currency, BigDecimal balance, String color,
                             String cardNetwork, String cardNumberMasked, String cardNumberFull, String expiryDate,
                             Boolean includedInBalance) {
}
