package com.pfm.account.service;

import com.pfm.account.dto.AccountRequest;
import com.pfm.account.entity.Account;
import com.pfm.account.repository.AccountRepository;
import com.pfm.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> findAll(Long userId) { return accountRepository.findByUserIdAndDeletedAtIsNull(userId); }

    public Account findById(Long userId, Long id) { return accountRepository.findByIdAndUserIdAndDeletedAtIsNull(id, userId).orElseThrow(() -> new ApiException("Account not found")); }

    public Account create(Long userId, AccountRequest req) {
        Account a = new Account();
        a.setUserId(userId);
        patch(a, req);
        return accountRepository.save(a);
    }

    public Account update(Long userId, Long id, AccountRequest req) {
        Account a = findById(userId, id);
        patch(a, req);
        return accountRepository.save(a);
    }

    public void delete(Long userId, Long id) {
        Account a = findById(userId, id);
        a.setDeletedAt(LocalDateTime.now());
        accountRepository.save(a);
    }

    public Account toggleInBalance(Long userId, Long id) {
        Account a = findById(userId, id);
        a.setIncludedInBalance(!a.isIncludedInBalance());
        return accountRepository.save(a);
    }

    private void patch(Account a, AccountRequest req) {
        if (req.name() != null) a.setName(req.name());
        if (req.type() != null) a.setType(req.type());
        if (req.currency() != null) a.setCurrency(req.currency());
        if (req.balance() != null) a.setBalance(req.balance());
        if (req.color() != null) a.setColor(req.color());
        if (req.cardNetwork() != null) a.setCardNetwork(req.cardNetwork());
        if (req.cardNumberMasked() != null) a.setCardNumberMasked(req.cardNumberMasked());
        if (req.cardNumberFull() != null) a.setCardNumberFull(req.cardNumberFull());
        if (req.expiryDate() != null) a.setExpiryDate(req.expiryDate());
        if (req.includedInBalance() != null) a.setIncludedInBalance(req.includedInBalance());
    }
}
