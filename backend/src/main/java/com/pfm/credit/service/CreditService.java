package com.pfm.credit.service;

import com.pfm.credit.entity.Credit;
import com.pfm.credit.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository repository;
    public List<Credit> findAll(Long userId){return repository.findByUserId(userId);}    
    public Credit save(Long userId, Credit credit){credit.setUserId(userId);return repository.save(credit);}    
    public Credit incrementInstallments(Long id){Credit c=repository.findById(id).orElseThrow();c.setPaidInstallments(c.getPaidInstallments()+1);return repository.save(c);}    
    public void delete(Long id){repository.deleteById(id);}    
}
