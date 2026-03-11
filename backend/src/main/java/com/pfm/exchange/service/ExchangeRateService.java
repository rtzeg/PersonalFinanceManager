package com.pfm.exchange.service;

import com.pfm.exchange.entity.ExchangeRate;
import com.pfm.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository repository;

    public List<ExchangeRate> findAll() { return repository.findAll(); }

    public ExchangeRate upsert(String from, String to, BigDecimal rate) {
        ExchangeRate er = repository.findByFromCurrencyAndToCurrency(from, to).orElseGet(ExchangeRate::new);
        er.setFromCurrency(from);
        er.setToCurrency(to);
        er.setRate(rate);
        return repository.save(er);
    }

    public BigDecimal getRate(String from, String to) {
        if (from.equals(to)) return BigDecimal.ONE;
        return repository.findByFromCurrencyAndToCurrency(from, to).map(ExchangeRate::getRate).orElseThrow();
    }
}
