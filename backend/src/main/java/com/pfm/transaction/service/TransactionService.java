package com.pfm.transaction.service;

import com.pfm.account.entity.Account;
import com.pfm.account.repository.AccountRepository;
import com.pfm.common.exception.ApiException;
import com.pfm.exchange.service.ExchangeRateService;
import com.pfm.transaction.dto.TransactionRequest;
import com.pfm.transaction.entity.Transaction;
import com.pfm.transaction.enumtype.TransactionType;
import com.pfm.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;
    private final AccountBalanceService balanceService;
    private final ExchangeRateService exchangeRateService;

    public List<Transaction> findAll(Long userId, TransactionType type, Long accountId, String month) {
        List<Transaction> list = type == null ? txRepo.findByUserIdAndDeletedAtIsNull(userId) : txRepo.findByUserIdAndTypeAndDeletedAtIsNull(userId, type);
        return list.stream().filter(t -> accountId == null || t.getAccountId().equals(accountId)).filter(t -> {
            if (month == null) return true;
            LocalDate d = LocalDate.parse(month + "-01");
            LocalDateTime s = d.atStartOfDay();
            LocalDateTime e = d.plusMonths(1).atStartOfDay();
            return !t.getOccurredAt().isBefore(s) && t.getOccurredAt().isBefore(e);
        }).toList();
    }

    public Transaction findById(Long userId, Long id) { return txRepo.findByIdAndUserIdAndDeletedAtIsNull(id, userId).orElseThrow(() -> new ApiException("Transaction not found")); }

    @Transactional
    public Transaction createTransaction(Long userId, TransactionRequest req) {
        Account from = accountRepo.findByIdAndUserIdAndDeletedAtIsNull(req.accountId(), userId).orElseThrow(() -> new ApiException("Account not found"));
        if (!from.getCurrency().equals(req.currency())) throw new ApiException("Transaction currency must match source account currency");
        Transaction tx = map(userId, req, new Transaction());
        apply(tx, from, userId);
        accountRepo.save(from);
        return txRepo.save(tx);
    }

    @Transactional
    public Transaction updateTransaction(Long userId, Long id, TransactionRequest req) {
        Transaction existing = findById(userId, id);
        Account oldFrom = accountRepo.findByIdAndUserIdAndDeletedAtIsNull(existing.getAccountId(), userId).orElseThrow();
        Account oldTo = existing.getToAccountId() == null ? null : accountRepo.findByIdAndUserIdAndDeletedAtIsNull(existing.getToAccountId(), userId).orElse(null);
        balanceService.rollbackTransactionEffect(existing, oldFrom, oldTo);
        accountRepo.save(oldFrom);
        if (oldTo != null) accountRepo.save(oldTo);

        Transaction updated = map(userId, req, existing);
        Account newFrom = accountRepo.findByIdAndUserIdAndDeletedAtIsNull(updated.getAccountId(), userId).orElseThrow();
        apply(updated, newFrom, userId);
        accountRepo.save(newFrom);
        return txRepo.save(updated);
    }

    @Transactional
    public void deleteTransaction(Long userId, Long id) {
        Transaction tx = findById(userId, id);
        Account from = accountRepo.findByIdAndUserIdAndDeletedAtIsNull(tx.getAccountId(), userId).orElseThrow();
        Account to = tx.getToAccountId() == null ? null : accountRepo.findByIdAndUserIdAndDeletedAtIsNull(tx.getToAccountId(), userId).orElse(null);
        balanceService.rollbackTransactionEffect(tx, from, to);
        accountRepo.save(from);
        if (to != null) accountRepo.save(to);
        tx.setDeletedAt(LocalDateTime.now());
        txRepo.save(tx);
    }

    private Transaction map(Long userId, TransactionRequest req, Transaction tx) {
        tx.setUserId(userId);
        tx.setType(req.type());
        tx.setAmount(req.amount());
        tx.setCurrency(req.currency());
        tx.setCategory(req.category());
        tx.setDescription(req.description());
        tx.setNote(req.note());
        tx.setAccountId(req.accountId());
        tx.setToAccountId(req.toAccountId());
        tx.setToCurrency(req.toCurrency());
        tx.setToAmount(req.toAmount());
        tx.setOccurredAt(req.occurredAt());
        return tx;
    }

    private void apply(Transaction tx, Account from, Long userId) {
        if (tx.getType() == TransactionType.expense) {
            balanceService.applyExpense(from, tx.getAmount());
        } else if (tx.getType() == TransactionType.income) {
            balanceService.applyIncome(from, tx.getAmount());
        } else {
            Account to = accountRepo.findByIdAndUserIdAndDeletedAtIsNull(tx.getToAccountId(), userId).orElseThrow(() -> new ApiException("Target account not found"));
            BigDecimal toAmount = tx.getToAmount();
            if (!from.getCurrency().equals(to.getCurrency())) {
                BigDecimal rate = exchangeRateService.getRate(from.getCurrency(), to.getCurrency());
                toAmount = tx.getAmount().multiply(rate);
                tx.setToCurrency(to.getCurrency());
                tx.setToAmount(toAmount);
            } else if (toAmount == null) {
                toAmount = tx.getAmount();
                tx.setToAmount(toAmount);
            }
            balanceService.applyTransfer(from, to, tx.getAmount(), toAmount);
            accountRepo.save(to);
        }
    }
}
