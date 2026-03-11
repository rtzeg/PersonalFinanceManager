package com.pfm.analytics.service;

import com.pfm.transaction.enumtype.TransactionType;
import com.pfm.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final TransactionRepository txRepo;

    public Map<String, Object> overview(Long userId, String period) {
        DateRange r = range(period);
        return Map.of(
                "income", txRepo.sumAmountByTypeInPeriod(userId, TransactionType.income, r.start(), r.end()),
                "expense", txRepo.sumAmountByTypeInPeriod(userId, TransactionType.expense, r.start(), r.end())
        );
    }

    public List<Object[]> categories(Long userId, String period) {
        DateRange r = range(period);
        return txRepo.sumByCategory(userId, TransactionType.expense, r.start(), r.end());
    }

    private DateRange range(String period) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = "year".equals(period) ? now.withDayOfYear(1) : now.withDayOfMonth(1);
        return new DateRange(startDate.atStartOfDay(), startDate.plusMonths(1).atStartOfDay());
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {}
}
