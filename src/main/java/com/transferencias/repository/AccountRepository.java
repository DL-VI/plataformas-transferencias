package com.transferencias.repository;

import com.transferencias.model.Account;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

    Mono<Account> findByAccountNumber(String accountNumber);

    @Query("UPDATE accounts SET balance = balance - :amount WHERE id = :id AND balance >= :amount")
    Mono<Integer> debitAccount(Long id, java.math.BigDecimal amount);

    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :id")
    Mono<Integer> creditAccount(Long id, java.math.BigDecimal amount);
}