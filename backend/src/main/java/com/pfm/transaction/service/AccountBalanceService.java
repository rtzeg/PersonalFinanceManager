package com.pfm.transaction.service;

import com.pfm.account.entity.Account;
import com.pfm.common.exception.ApiException;
import com.pfm.transaction.entity.Transaction;
import com.pfm.transaction.enumtype.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountBalanceService {

    public void applyExpense(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new ApiException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
    }

    public void applyIncome(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }

    public void applyTransfer(Account from, Account to, BigDecimal amount, BigDecimal toAmount) {
        applyExpense(from, amount);
        applyIncome(to, toAmount);
    }

    public void rollbackTransactionEffect(Transaction tx, Account from, Account to) {
        if (tx.getType() == TransactionType.expense) {
            from.setBalance(from.getBalance().add(tx.getAmount()));
        } else if (tx.getType() == TransactionType.income) {
            from.setBalance(from.getBalance().subtract(tx.getAmount()));
        } else {
            from.setBalance(from.getBalance().add(tx.getAmount()));
            if (to != null && tx.getToAmount() != null) {
                to.setBalance(to.getBalance().subtract(tx.getToAmount()));
            }
        }
    }
}
