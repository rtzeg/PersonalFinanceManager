package com.pfm.debt.service;

import com.pfm.common.exception.ApiException;
import com.pfm.debt.entity.Debt;
import com.pfm.debt.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtService {
    private final DebtRepository repository;

    public List<Debt> findAll(Long userId) {
        return repository.findByUserId(userId);
    }

    public Debt save(Long userId, Debt debt) {
        debt.setUserId(userId);
        return repository.save(debt);
    }

    public Debt close(Long userId, Long id) {
        Debt debt = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new ApiException("Debt not found"));
        debt.setStatus("closed");
        debt.setClosedAt(LocalDateTime.now());
        return repository.save(debt);
    }

    public void delete(Long userId, Long id) {
        Debt debt = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new ApiException("Debt not found"));
        repository.delete(debt);
    }
}
