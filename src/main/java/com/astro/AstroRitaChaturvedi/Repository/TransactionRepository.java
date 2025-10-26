package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.TransactionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Model.WalletModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {
    List<TransactionModel> findByUser(UserModel user);
    List<TransactionModel> findByWallet(WalletModel wallet);
    Optional<TransactionModel> findByRazorpayOrderId(String razorpayOrderId);
    Optional<TransactionModel> findByRazorpayPaymentId(String razorpayPaymentId);
    
    @Query("SELECT SUM(t.amount) FROM TransactionModel t WHERE t.astrologerId = :astrologerId AND t.status = 'SUCCESS'")
    Double sumEarningsByAstrologerId(@Param("astrologerId") UUID astrologerId);
    
    @Query("SELECT SUM(t.amount) FROM TransactionModel t WHERE t.status = 'SUCCESS'")
    Double sumSuccessfulTransactions();
}
