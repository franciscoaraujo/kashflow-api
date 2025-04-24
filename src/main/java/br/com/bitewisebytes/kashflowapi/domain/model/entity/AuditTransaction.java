package br.com.bitewisebytes.kashflowapi.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_transaction")
public class AuditTransaction {

    @Id
    private String transactionId;
    private Long walletId;
    private String type;
    private BigDecimal amount;
    private String status;
    private LocalDateTime timestamp;
    private Long walletIdTo;
    private Long walletIdFrom;

    public AuditTransaction() {
    }

    public AuditTransaction(
            String transactionId,
            Long walletId,
            String type,
            BigDecimal amount,
            String status,
            LocalDateTime timestamp,
            Long walletIdTo,
            Long walletIdFrom
    ) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
        this.walletIdTo = walletIdTo;
        this.walletIdFrom = walletIdFrom;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getWalletIdTo() {
        return walletIdTo;
    }

    public void setWalletIdTo(Long walletIdTo) {
        this.walletIdTo = walletIdTo;
    }

    public Long getWalletIdFrom() {
        return walletIdFrom;
    }

    public void setWalletIdFrom(Long walletIdFrom) {
        this.walletIdFrom = walletIdFrom;
    }

    // Builder pattern implementation
    public static class Builder {
        private String transactionId;
        private Long walletId;
        private String type;
        private BigDecimal amount;
        private String status;
        private LocalDateTime timestamp;
        private Long walletIdTo;
        private Long walletIdFrom;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder walletId(Long walletId) {
            this.walletId = walletId;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder walletIdTo(Long walletIdTo) {
            this.walletIdTo = walletIdTo;
            return this;
        }

        public Builder walletIdFrom(Long walletIdFrom) {
            this.walletIdFrom = walletIdFrom;
            return this;
        }

        public AuditTransaction build() {
            return new AuditTransaction(transactionId, walletId, type, amount, status, timestamp, walletIdTo, walletIdFrom);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
