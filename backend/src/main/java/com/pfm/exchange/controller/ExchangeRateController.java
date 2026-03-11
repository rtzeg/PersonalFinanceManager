package com.pfm.exchange.controller;

import com.pfm.exchange.entity.ExchangeRate;
import com.pfm.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService service;
    @GetMapping public List<ExchangeRate> all(){return service.findAll();}
    @PutMapping("/{from}/{to}") public ExchangeRate upsert(@PathVariable String from,@PathVariable String to,@RequestParam BigDecimal rate){return service.upsert(from,to,rate);}    
}
