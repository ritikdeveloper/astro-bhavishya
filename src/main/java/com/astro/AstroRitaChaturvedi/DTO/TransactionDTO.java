package com.astro.AstroRitaChaturvedi.DTO;

import com.astro.AstroRitaChaturvedi.Model.TransactionModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {
    private UUID transactionId;
    private double amount;
    private String transactionType; // Enum to String
    private String status; // Enum to String
    private String description;
    private String transactionRef;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private LocalDateTime timestamp;
    private UUID walletId;
    private UUID userId;
    private UUID sessionId;

    public TransactionDTO(TransactionModel transaction) {
        if (transaction == null) return;
        this.transactionId = transaction.getTransactionId();
        this.amount = transaction.getAmount();
        if (transaction.getTransactionType() != null) {
            this.transactionType = transaction.getTransactionType().name();
        }
        if (transaction.getStatus() != null) {
            this.status = transaction.getStatus().name();
        }
        this.description = transaction.getDescription();
        this.transactionRef = transaction.getTransactionRef();
        this.razorpayOrderId = transaction.getRazorpayOrderId();
        this.razorpayPaymentId = transaction.getRazorpayPaymentId();
        this.timestamp = transaction.getTimestamp();
        if (transaction.getWallet() != null) {
            this.walletId = transaction.getWallet().getWalletId();
        }
        if (transaction.getUser() != null) {
            this.userId = transaction.getUser().getCustomerId();
        }
        if (transaction.getSession() != null) {
            this.sessionId = transaction.getSession().getSessionId();
        }
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
