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
@Table("transfers")
public class Transfer {

    @Id
    private Long id;

    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private String description;
    private String status; // PENDING, COMPLETED, FAILED
    private LocalDateTime createdAt;

    public Transfer(Long sourceAccountId, Long destinationAccountId,
                    BigDecimal amount, String description) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.description = description;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
}