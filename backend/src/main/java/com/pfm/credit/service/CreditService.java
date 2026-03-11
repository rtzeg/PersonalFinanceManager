package com.pfm.credit.service;

import com.pfm.common.exception.ApiException;
import com.pfm.credit.entity.Credit;
import com.pfm.credit.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository repository;

    public List<Credit> findAll(Long userId) {
        return repository.findByUserId(userId);
    }

    public Credit save(Long userId, Credit credit) {
        credit.setUserId(userId);
        return repository.save(credit);
    }

    public Credit incrementInstallments(Long userId, Long id) {
        Credit credit = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new ApiException("Credit not found"));
        credit.setPaidInstallments(credit.getPaidInstallments() + 1);
        return repository.save(credit);
    }

    public void delete(Long userId, Long id) {
        Credit credit = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new ApiException("Credit not found"));
        repository.delete(credit);
    }
}
