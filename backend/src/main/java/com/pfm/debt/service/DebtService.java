package com.pfm.debt.service;

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
    public List<Debt> findAll(Long userId){return repository.findByUserId(userId);}    
    public Debt save(Long userId, Debt debt){debt.setUserId(userId);return repository.save(debt);}    
    public Debt close(Long id){Debt d=repository.findById(id).orElseThrow();d.setStatus("closed");d.setClosedAt(LocalDateTime.now());return repository.save(d);}    
    public void delete(Long id){repository.deleteById(id);}    
}
