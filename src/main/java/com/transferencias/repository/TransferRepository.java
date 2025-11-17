package com.transferencias.repository;

import com.transferencias.model.Transfer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransferRepository extends ReactiveCrudRepository<Transfer, Long> {

    Flux<Transfer> findBySourceAccountId(Long sourceAccountId);
    Flux<Transfer> findByDestinationAccountId(Long destinationAccountId);
}