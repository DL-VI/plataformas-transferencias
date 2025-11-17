package com.transferencias.controller;

import com.transferencias.dto.TransferRequest;
import com.transferencias.model.Transfer;
import com.transferencias.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transfer> createTransfer(@Valid @RequestBody TransferRequest request) {
        return transferService.createTransfer(request);
    }

    @GetMapping
    public Flux<Transfer> getAllTransfers() {
        return transferService.getAllTransfers();
    }

    @GetMapping("/{id}")
    public Mono<Transfer> getTransferById(@PathVariable Long id) {
        return transferService.getTransferById(id);
    }

    @GetMapping("/account/{accountId}")
    public Flux<Transfer> getTransfersByAccountId(@PathVariable Long accountId) {
        return transferService.getTransfersByAccountId(accountId);
    }
}