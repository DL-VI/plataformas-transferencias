package com.transferencias.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("accounts")
public class Account {

    @Id
    private Long id;

    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private String currency = "COP"; // Solo pesos colombianos
    private LocalDateTime createdAt;

    public Account(String accountNumber, String ownerName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.currency = "COP";
        this.createdAt = LocalDateTime.now();
    }
}