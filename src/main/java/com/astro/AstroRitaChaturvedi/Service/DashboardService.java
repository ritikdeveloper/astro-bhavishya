package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.SessionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DashboardService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AstrologerRepository astrologerRepository;
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private CallRepository callRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private WalletRepository walletRepository;
    
    public Map<String, Object> getUserDashboardStats(UUID userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // Wallet balance
        userRepository.findById(userId).ifPresent(user -> {
            walletRepository.findByUser(user).ifPresent(wallet -> 
                stats.put("walletBalance", wallet.getBalance())
            );
        });
        
        // Total sessions (chats + calls)
        long totalChats = chatRepository.countByUserCustomerIdAndSessionType(userId, SessionModel.SessionType.CHAT);
        long totalCalls = callRepository.countByUserCustomerIdAndSessionType(userId, SessionModel.SessionType.CALL);
        stats.put("totalSessions", totalChats + totalCalls);
        
        // Active chats
        stats.put("activeChats", chatRepository.countByUserCustomerIdAndSessionTypeAndStatus(userId, SessionModel.SessionType.CHAT, SessionModel.SessionStatus.ACTIVE));
        
        // Pending calls
        stats.put("pendingCalls", callRepository.countByUserCustomerIdAndSessionTypeAndStatus(userId, SessionModel.SessionType.CALL, SessionModel.SessionStatus.SCHEDULED));
        
        // Favorite astrologers count (if you have favorites table)
        stats.put("favoriteAstrologers", 0); // Placeholder
        
        return stats;
    }
    
    public Map<String, Object> getAstrologerDashboardStats(UUID astrologerId) {
        Map<String, Object> stats = new HashMap<>();
        
        // Total earnings
        Double totalEarnings = transactionRepository.sumEarningsByAstrologerId(astrologerId);
        stats.put("totalEarnings", totalEarnings != null ? totalEarnings : 0.0);
        
        // Total sessions
        long totalChats = chatRepository.countByAstrologerAstrologerIdAndSessionType(astrologerId, SessionModel.SessionType.CHAT);
        long totalCalls = callRepository.countByAstrologerAstrologerIdAndSessionType(astrologerId, SessionModel.SessionType.CALL);
        stats.put("totalSessions", totalChats + totalCalls);
        
        // Active sessions
        stats.put("activeSessions", chatRepository.countByAstrologerAstrologerIdAndSessionTypeAndStatus(astrologerId, SessionModel.SessionType.CHAT, SessionModel.SessionStatus.ACTIVE) +
                                   callRepository.countByAstrologerAstrologerIdAndSessionTypeAndStatus(astrologerId, SessionModel.SessionType.CALL, SessionModel.SessionStatus.ACTIVE));
        
        // Average rating
        Double avgRating = astrologerRepository.findById(astrologerId)
            .map(AstrologerModel::getAverageRating)
            .orElse(0.0);
        stats.put("averageRating", avgRating);
        
        return stats;
    }
    
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total users
        stats.put("totalUsers", userRepository.countByRole(UserModel.UserRole.USER));
        
        // Total astrologers
        stats.put("totalAstrologers", astrologerRepository.countByApprovalStatus(AstrologerModel.ApprovalStatus.APPROVED));
        
        // Pending applications
        stats.put("pendingApplications", astrologerRepository.countByApprovalStatus(AstrologerModel.ApprovalStatus.PENDING));
        
        // Total transactions
        stats.put("totalTransactions", transactionRepository.count());
        
        // Total revenue
        Double totalRevenue = transactionRepository.sumSuccessfulTransactions();
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        
        return stats;
    }
    
    public Map<String, Object> getHomePageStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Happy clients (total users)
        stats.put("happyClients", userRepository.countByRole(UserModel.UserRole.USER));
        
        // Total astrologers
        stats.put("astrologers", astrologerRepository.countByApprovalStatus(AstrologerModel.ApprovalStatus.APPROVED));
        
        // Total consultations (using session repository)
        long totalConsultations = chatRepository.count();
        stats.put("consultations", totalConsultations);
        
        // Average rating
        Double avgRating = astrologerRepository.getAverageRatingOfAllAstrologers();
        stats.put("rating", avgRating != null ? String.format("%.1f", avgRating) : "4.8");
        
        return stats;
    }
}