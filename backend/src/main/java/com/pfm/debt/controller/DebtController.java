package com.pfm.debt.controller;

import com.pfm.common.util.CurrentUserService;
import com.pfm.debt.entity.Debt;
import com.pfm.debt.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
public class DebtController {
    private final DebtService service;
    private final CurrentUserService current;
    @GetMapping public List<Debt> all(){return service.findAll(current.getUserId());}
    @PostMapping public Debt create(@RequestBody Debt debt){return service.save(current.getUserId(), debt);}    
    @PutMapping("/{id}") public Debt update(@PathVariable Long id,@RequestBody Debt debt){debt.setId(id);return service.save(current.getUserId(), debt);}    
    @PatchMapping("/{id}/close") public Debt close(@PathVariable Long id){return service.close(id);}    
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){service.delete(id);}    
}
