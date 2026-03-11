package com.pfm.transaction.repository;

import com.pfm.transaction.entity.Transaction;
import com.pfm.transaction.enumtype.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);
    List<Transaction> findByUserIdAndDeletedAtIsNull(Long userId);
    List<Transaction> findByUserIdAndTypeAndDeletedAtIsNull(Long userId, TransactionType type);

    @Query("""
        select coalesce(sum(t.amount),0) from Transaction t
        where t.userId=:userId and t.type=:type and t.deletedAt is null
          and t.occurredAt >= :start and t.occurredAt < :end
    """)
    BigDecimal sumAmountByTypeInPeriod(@Param("userId") Long userId,
                                       @Param("type") TransactionType type,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query("""
        select t.category, coalesce(sum(t.amount),0) from Transaction t
        where t.userId=:userId and t.type=:type and t.deletedAt is null
          and t.occurredAt >= :start and t.occurredAt < :end
        group by t.category
    """)
    List<Object[]> sumByCategory(@Param("userId") Long userId,
                                 @Param("type") TransactionType type,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);
}
