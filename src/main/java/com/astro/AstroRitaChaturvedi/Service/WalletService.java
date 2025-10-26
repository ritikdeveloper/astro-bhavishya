package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.TransactionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Model.WalletModel;
import com.astro.AstroRitaChaturvedi.Repository.TransactionRepository;
import com.astro.AstroRitaChaturvedi.Repository.UserRepository;
import com.astro.AstroRitaChaturvedi.Repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RazorpayService razorpayService;

    public WalletModel getWalletByUserId(UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Transactional
    public TransactionModel createRechargeTransaction(UUID userId, double amount) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WalletModel wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Create Razorpay order
        String orderId = razorpayService.createOrder(amount);

        // Create transaction
        TransactionModel transaction = new TransactionModel();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionModel.TransactionType.RECHARGE);
        transaction.setStatus(TransactionModel.TransactionStatus.PENDING);
        transaction.setDescription("Wallet recharge");
        transaction.setTransactionRef(UUID.randomUUID().toString());
        transaction.setRazorpayOrderId(orderId);
        transaction.setWallet(wallet);
        transaction.setUser(user);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public TransactionModel completeRecharge(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) {
        boolean isValid = razorpayService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
        if (!isValid) {
            TransactionModel transaction = transactionRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElse(null);
            if (transaction != null) {
                transaction.setStatus(TransactionModel.TransactionStatus.FAILED);
                transaction.setDescription("Payment verification failed.");
                transactionRepository.save(transaction);
            }
            throw new RuntimeException("Razorpay payment verification failed.");
        }

        TransactionModel transaction = transactionRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Transaction not found after successful verification."));

        // If already marked success, skip
        if (transaction.getStatus() == TransactionModel.TransactionStatus.SUCCESS) {
            return transaction;
        }

        // Mark as success
        transaction.setStatus(TransactionModel.TransactionStatus.SUCCESS);
        transaction.setRazorpayPaymentId(razorpayPaymentId);

        // Update wallet balance
        WalletModel wallet = transaction.getWallet();
        wallet.setBalance(wallet.getBalance() + transaction.getAmount());
        walletRepository.save(wallet);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public boolean deductBalance(UUID userId, double amount, String description) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WalletModel wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Check if wallet has sufficient balance
        if (wallet.getBalance() < amount) {
            return false;
        }

        // Deduct balance
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);

        // Create transaction
        TransactionModel transaction = new TransactionModel();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionModel.TransactionType.PAYMENT);
        transaction.setStatus(TransactionModel.TransactionStatus.SUCCESS);
        transaction.setDescription(description);
        transaction.setTransactionRef(UUID.randomUUID().toString());
        transaction.setWallet(wallet);
        transaction.setUser(user);

        transactionRepository.save(transaction);

        return true;
    }

    public List<TransactionModel> getUserTransactions(UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findByUser(user);
    }
}
