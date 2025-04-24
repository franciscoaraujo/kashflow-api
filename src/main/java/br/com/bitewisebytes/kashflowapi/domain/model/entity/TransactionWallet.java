package br.com.bitewisebytes.kashflowapi.domain.model.entity;

import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(name = "data_transaction")
    private LocalDate dataTransaction = LocalDate.now();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public TransactionWallet() {
    }

    public TransactionWallet(
            Long id,
            Wallet wallet,
            TransactionType type,
            BigDecimal amount,
            String description,
            LocalDate dataTransaction,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.wallet = wallet;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.dataTransaction = dataTransaction;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDataTransaction() {
        return dataTransaction;
    }

    public void setDataTransaction(LocalDate dataTransaction) {
        this.dataTransaction = dataTransaction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Builder pattern implementation
    public static class Builder {
        private Long id;
        private Wallet wallet;
        private TransactionType type;
        private BigDecimal amount;
        private String description;
        private LocalDate dataTransaction = LocalDate.now();
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder wallet(Wallet wallet) {
            this.wallet = wallet;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder dataTransaction(LocalDate dataTransaction) {
            this.dataTransaction = dataTransaction;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TransactionWallet build() {
            return new TransactionWallet(id, wallet, type, amount, description, dataTransaction, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}