package br.com.itau.eventplatform.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String transactionId;

    private String accountFrom;

    private String accountTo;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Instant createdAt;

    public Transaction() {
    }

    public Transaction(
            String transactionId,
            String accountFrom,
            String accountTo,
            BigDecimal amount,
            TransactionStatus status,
            Instant createdAt) {

        this.transactionId = transactionId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

}
