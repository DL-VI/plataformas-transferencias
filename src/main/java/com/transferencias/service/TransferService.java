package com.transferencias.service;

import com.transferencias.dto.TransferRequest;
import com.transferencias.model.Transfer;
import com.transferencias.repository.AccountRepository;
import com.transferencias.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Mono<Transfer> createTransfer(TransferRequest request) {
        return validateAccounts(request)
                .flatMap(valid -> {
                    Transfer transfer = new Transfer(
                            request.getSourceAccountId(),
                            request.getDestinationAccountId(),
                            request.getAmount(),
                            request.getDescription()
                    );

                    return transferRepository.save(transfer)
                            .flatMap(savedTransfer -> executeTransfer(savedTransfer));
                });
    }

    private Mono<Boolean> validateAccounts(TransferRequest request) {
        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            return Mono.error(new IllegalArgumentException("Cannot transfer to the same account"));
        }

        return accountRepository.findById(request.getSourceAccountId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Source account not found")))
                .flatMap(sourceAccount -> {
                    if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
                        return Mono.error(new IllegalArgumentException("Insufficient balance"));
                    }
                    return accountRepository.findById(request.getDestinationAccountId())
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("Destination account not found")))
                            .thenReturn(true);
                });
    }

    private Mono<Transfer> executeTransfer(Transfer transfer) {
        return accountRepository.findById(transfer.getSourceAccountId())
                .flatMap(source -> {
                    source.setBalance(source.getBalance().subtract(transfer.getAmount()));
                    return accountRepository.save(source);
                })
                .flatMap(source -> accountRepository.findById(transfer.getDestinationAccountId()))
                .flatMap(dest -> {
                    dest.setBalance(dest.getBalance().add(transfer.getAmount()));
                    return accountRepository.save(dest);
                })
                .flatMap(dest -> {
                    transfer.setStatus("COMPLETED");
                    return transferRepository.save(transfer);
                })
                .onErrorResume(error -> {
                    log.error("Transfer failed", error);
                    transfer.setStatus("FAILED");
                    return transferRepository.save(transfer);
                });
    }

    public Flux<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public Mono<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Transfer not found")));
    }

    public Flux<Transfer> getTransfersByAccountId(Long accountId) {
        return transferRepository.findBySourceAccountId(accountId)
                .mergeWith(transferRepository.findByDestinationAccountId(accountId));
    }
}