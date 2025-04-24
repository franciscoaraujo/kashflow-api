package br.com.bitewisebytes.kashflowapi.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String documentNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeCreated = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Wallet> wallets = new ArrayList<>();

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(
            Long id,
            String name,
            String documentNumber,
            String email,
            LocalDateTime dateTimeCreated,
            List<Wallet> wallets
    ) {
        this.id = id;
        this.name = name;
        this.documentNumber = documentNumber;
        this.email = email;
        this.dateTimeCreated = dateTimeCreated;
        this.wallets = wallets;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(LocalDateTime createdAt) {
        this.dateTimeCreated = createdAt;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    // Builder pattern implementation
    public static class Builder {
        private Long id;
        private String name;
        private String documentNumber;
        private String email;
        private LocalDateTime createdAt = LocalDateTime.now();
        private List<Wallet> wallets = new ArrayList<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder documentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder wallets(List<Wallet> wallets) {
            this.wallets = wallets;
            return this;
        }

        public User build() {
            return new User(id, name, documentNumber, email, createdAt, wallets);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
