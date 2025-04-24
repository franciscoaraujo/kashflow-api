package br.com.bitewisebytes.kashflowapi.domain.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", nullable = false)
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id", nullable = false)
    private Wallet receiverWallet;

    @Column(nullable = false)
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "sender_transaction_id", nullable = false, unique = true)
    private TransactionWallet senderTransactionWallet;

    @OneToOne
    @JoinColumn(name = "receiver_transaction_id", nullable = false, unique = true)
    private TransactionWallet receiverTransactionWallet;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Transfer() {
    }

    public Transfer(
            Long id,
            Wallet senderWallet,
            Wallet receiverWallet,
            BigDecimal amount,
            TransactionWallet senderTransactionWallet,
            TransactionWallet receiverTransactionWallet,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.amount = amount;
        this.senderTransactionWallet = senderTransactionWallet;
        this.receiverTransactionWallet = receiverTransactionWallet;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getSenderWallet() {
        return senderWallet;
    }

    public void setSenderWallet(Wallet senderWallet) {
        this.senderWallet = senderWallet;
    }

    public Wallet getReceiverWallet() {
        return receiverWallet;
    }

    public void setReceiverWallet(Wallet receiverWallet) {
        this.receiverWallet = receiverWallet;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionWallet getSenderTransactionWallet() {
        return senderTransactionWallet;
    }

    public void setSenderTransactionWallet(TransactionWallet senderTransactionWallet) {
        this.senderTransactionWallet = senderTransactionWallet;
    }

    public TransactionWallet getReceiverTransactionWallet() {
        return receiverTransactionWallet;
    }

    public void setReceiverTransactionWallet(TransactionWallet receiverTransactionWallet) {
        this.receiverTransactionWallet = receiverTransactionWallet;
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
        private Wallet senderWallet;
        private Wallet receiverWallet;
        private BigDecimal amount;
        private TransactionWallet senderTransactionWallet;
        private TransactionWallet receiverTransactionWallet;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder senderWallet(Wallet senderWallet) {
            this.senderWallet = senderWallet;
            return this;
        }

        public Builder receiverWallet(Wallet receiverWallet) {
            this.receiverWallet = receiverWallet;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder senderTransactionWallet(TransactionWallet senderTransactionWallet) {
            this.senderTransactionWallet = senderTransactionWallet;
            return this;
        }

        public Builder receiverTransactionWallet(TransactionWallet receiverTransactionWallet) {
            this.receiverTransactionWallet = receiverTransactionWallet;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Transfer build() {
            return new Transfer(id, senderWallet, receiverWallet, amount, senderTransactionWallet, receiverTransactionWallet, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

