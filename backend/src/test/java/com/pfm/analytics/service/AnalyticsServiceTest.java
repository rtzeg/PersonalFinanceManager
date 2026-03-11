package com.pfm.analytics.service;

import com.pfm.transaction.enumtype.TransactionType;
import com.pfm.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private AnalyticsService service;

    @Test
    void overviewYearUsesYearRange() {
        when(repository.sumAmountByTypeInPeriod(any(), any(), any(), any())).thenReturn(BigDecimal.ZERO);

        service.overview(1L, "year");

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(repository).sumAmountByTypeInPeriod(any(), org.mockito.ArgumentMatchers.eq(TransactionType.income), startCaptor.capture(), endCaptor.capture());

        LocalDateTime start = startCaptor.getValue();
        LocalDateTime end = endCaptor.getValue();

        assertTrue(start.getMonthValue() == 1 && start.getDayOfMonth() == 1);
        assertTrue(end.minusYears(1).getMonthValue() == 1 && end.minusYears(1).getDayOfMonth() == 1);
    }
}
