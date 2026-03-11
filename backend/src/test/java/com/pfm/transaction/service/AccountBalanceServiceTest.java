package com.pfm.transaction.service;

import com.pfm.account.entity.Account;
import com.pfm.common.exception.ApiException;
import com.pfm.transaction.entity.Transaction;
import com.pfm.transaction.enumtype.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountBalanceServiceTest {

    private final AccountBalanceService service = new AccountBalanceService();

    @Test
    void applyExpenseShouldDecreaseBalance() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));

        service.applyExpense(account, new BigDecimal("25.00"));

        assertEquals(new BigDecimal("75.00"), account.getBalance());
    }

    @Test
    void applyExpenseShouldFailWhenInsufficientFunds() {
        Account account = new Account();
        account.setBalance(new BigDecimal("10.00"));

        assertThrows(ApiException.class, () -> service.applyExpense(account, new BigDecimal("10.01")));
    }

    @Test
    void rollbackTransferShouldRestoreBalances() {
        Account from = new Account();
        from.setBalance(new BigDecimal("80.00"));
        Account to = new Account();
        to.setBalance(new BigDecimal("130.00"));

        Transaction tx = new Transaction();
        tx.setType(TransactionType.transfer);
        tx.setAmount(new BigDecimal("20.00"));
        tx.setToAmount(new BigDecimal("30.00"));

        service.rollbackTransactionEffect(tx, from, to);

        assertEquals(new BigDecimal("100.00"), from.getBalance());
        assertEquals(new BigDecimal("100.00"), to.getBalance());
    }
}
