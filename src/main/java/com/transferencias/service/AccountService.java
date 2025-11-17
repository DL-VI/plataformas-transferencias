package com.transferencias.service;

import com.transferencias.model.Account;
import com.transferencias.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Mono<Account> createAccount(Account account) {
        account.setCreatedAt(LocalDateTime.now());
        account.setCurrency("COP"); // Forzar solo pesos colombianos
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    public Flux<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Mono<Account> getAccountById(Long id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found")));
    }

    public Mono<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found")));
    }

    public Mono<Account> updateAccount(Long id, Account account) {
        return accountRepository.findById(id)
                .flatMap(existingAccount -> {
                    existingAccount.setOwnerName(account.getOwnerName());
                    // No permitir cambio de moneda, siempre COP
                    existingAccount.setCurrency("COP");
                    return accountRepository.save(existingAccount);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found")));
    }

    public Mono<Void> deleteAccount(Long id) {
        return accountRepository.deleteById(id);
    }
}