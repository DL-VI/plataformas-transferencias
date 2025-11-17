package com.transferencias.controller;

import com.transferencias.model.Account;
import com.transferencias.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public Flux<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Mono<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/number/{accountNumber}")
    public Mono<Account> getAccountByNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    @PutMapping("/{id}")
    public Mono<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return accountService.updateAccount(id, account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id);
    }
}