package com.pfm.account.controller;

import com.pfm.account.dto.AccountRequest;
import com.pfm.account.entity.Account;
import com.pfm.account.service.AccountService;
import com.pfm.common.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private final CurrentUserService current;

    @GetMapping public List<Account> all(){return service.findAll(current.getUserId());}
    @GetMapping("/{id}") public Account byId(@PathVariable Long id){return service.findById(current.getUserId(),id);}    
    @PostMapping public Account create(@RequestBody AccountRequest req){return service.create(current.getUserId(),req);}    
    @PutMapping("/{id}") public Account update(@PathVariable Long id,@RequestBody AccountRequest req){return service.update(current.getUserId(),id,req);}    
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){service.delete(current.getUserId(),id);}    
    @PatchMapping("/{id}/toggle-in-balance") public Account toggle(@PathVariable Long id){return service.toggleInBalance(current.getUserId(),id);}    
}
