package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.DTO.TransactionDTO;
import com.astro.AstroRitaChaturvedi.Model.TransactionModel;
import com.astro.AstroRitaChaturvedi.Model.WalletModel;
import com.astro.AstroRitaChaturvedi.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    @PreAuthorize("authentication.principal.customerId == #userId or hasRole('ADMIN')")
    public ResponseEntity<WalletModel> getWallet(@PathVariable UUID userId) {
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }

    @PostMapping("/{userId}/recharge")
    public ResponseEntity<TransactionModel> rechargeWallet(
            @PathVariable UUID userId,
            @RequestBody Map<String, Double> request) {
        double amount = request.get("amount");
        return ResponseEntity.ok(walletService.createRechargeTransaction(userId, amount));
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<TransactionDTO> verifyPayment(@RequestBody Map<String, String> request) {
        String paymentId = request.get("razorpayPaymentId");
        String orderId = request.get("razorpayOrderId");
        String signature = request.get("razorpaySignature");

        TransactionModel transaction = walletService.completeRecharge(paymentId, orderId, signature);
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }
    @GetMapping("/{userId}/transactions")
    @PreAuthorize("authentication.principal.customerId == #userId or hasRole('ADMIN')")
    public ResponseEntity<List<TransactionModel>> getTransactions(@PathVariable UUID userId) {
        return ResponseEntity.ok(walletService.getUserTransactions(userId));
    }
}
