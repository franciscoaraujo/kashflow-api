package br.com.bitewisebytes.kashflowapi.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String walletNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeCreated = LocalDateTime.now();

    @Column(nullable = false)
    private boolean status = true;

    @JsonIgnore
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionWallet> transactionWallets = new ArrayList<>();

    // Default constructor
    public Wallet() {}

    // Parameterized constructor
    public Wallet(
            Long id,
            String walletNumber,
            User user,
            BigDecimal balance,
            LocalDateTime dateTimeCreated,
            boolean status,
            List<TransactionWallet> transactionWallets
    ) {
        this.id = id;
        this.walletNumber = walletNumber;
        this.user = user;
        this.balance = balance;
        this.dateTimeCreated = dateTimeCreated;
        this.status = status;
        this.transactionWallets = transactionWallets;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(LocalDateTime createdAt) {
        this.dateTimeCreated = createdAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<TransactionWallet> getTransactionWallets() {
        return transactionWallets;
    }

    public void setTransactionWallets(List<TransactionWallet> transactionWallets) {
        this.transactionWallets = transactionWallets;
    }

    // Builder pattern implementation
    public static class Builder {
        private Long id;
        private String walletNumber;
        private User user;
        private BigDecimal balance;
        private LocalDateTime createdAt = LocalDateTime.now();
        private boolean status = true;
        private List<TransactionWallet> transactionWallets = new ArrayList<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder walletNumber(String walletNumber) {
            this.walletNumber = walletNumber;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder status(boolean status) {
            this.status = status;
            return this;
        }


        public Builder transactionWallets(List<TransactionWallet> transactionWallets) {
            this.transactionWallets = transactionWallets;
            return this;
        }

        public Wallet build() {
            return new Wallet(id, walletNumber, user, balance, createdAt, status, transactionWallets);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}