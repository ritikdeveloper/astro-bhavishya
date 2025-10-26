package com.astro.AstroRitaChaturvedi.Model;

import com.astro.AstroRitaChaturvedi.Convertor.BalanceConvertor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@JsonIgnoreProperties({"transactions","hibernateLazyInitializer", "handler"})
public class WalletModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID walletId;

    private double balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserModel user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionModel> transactions;

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }

}
