package com.astro.AstroRitaChaturvedi.DTO;

import com.astro.AstroRitaChaturvedi.Model.WalletModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class WalletDTO {
    private UUID walletId;
    private double balance;
    private UUID userId;
    private List<TransactionDTO> transactions;

    public WalletDTO(WalletModel wallet) {
        if (wallet == null) return;
        this.walletId = wallet.getWalletId();
        this.balance = wallet.getBalance();
        if (wallet.getUser() != null) {
            this.userId = wallet.getUser().getCustomerId();
        }
        if (wallet.getTransactions() != null) {
            this.transactions = wallet.getTransactions().stream()
                                    .map(TransactionDTO::new)
                                    .collect(Collectors.toList());
        } else {
            this.transactions = Collections.emptyList();
        }
    }

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
