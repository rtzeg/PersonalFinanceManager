package com.pfm.credit.repository;

import com.pfm.credit.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findByUserId(Long userId);
    Optional<Credit> findByIdAndUserId(Long id, Long userId);
}
