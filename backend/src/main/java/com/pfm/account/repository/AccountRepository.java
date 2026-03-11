package com.pfm.account.repository;

import com.pfm.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserIdAndDeletedAtIsNull(Long userId);
    Optional<Account> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);
}
