package com.pfm.credit.controller;

import com.pfm.common.util.CurrentUserService;
import com.pfm.credit.entity.Credit;
import com.pfm.credit.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService service;
    private final CurrentUserService current;
    @GetMapping public List<Credit> all(){return service.findAll(current.getUserId());}
    @PostMapping public Credit create(@RequestBody Credit credit){return service.save(current.getUserId(), credit);}    
    @PutMapping("/{id}") public Credit update(@PathVariable Long id,@RequestBody Credit credit){credit.setId(id);return service.save(current.getUserId(), credit);}    
    @PatchMapping("/{id}/installments") public Credit inc(@PathVariable Long id){return service.incrementInstallments(id);}    
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){service.delete(id);}    
}
