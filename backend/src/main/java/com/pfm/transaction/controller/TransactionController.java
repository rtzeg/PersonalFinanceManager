package com.pfm.transaction.controller;

import com.pfm.common.util.CurrentUserService;
import com.pfm.transaction.dto.TransactionRequest;
import com.pfm.transaction.entity.Transaction;
import com.pfm.transaction.enumtype.TransactionType;
import com.pfm.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;
    private final CurrentUserService current;

    @GetMapping
    public List<Transaction> all(@RequestParam(required = false) TransactionType type,
                                 @RequestParam(required = false) Long accountId,
                                 @RequestParam(required = false) String month) {
        return service.findAll(current.getUserId(), type, accountId, month);
    }
    @GetMapping("/{id}") public Transaction byId(@PathVariable Long id){return service.findById(current.getUserId(),id);}    
    @PostMapping public Transaction create(@RequestBody @Valid TransactionRequest req){return service.createTransaction(current.getUserId(),req);}    
    @PutMapping("/{id}") public Transaction update(@PathVariable Long id,@RequestBody @Valid TransactionRequest req){return service.updateTransaction(current.getUserId(),id,req);}    
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){service.deleteTransaction(current.getUserId(),id);}    
}
