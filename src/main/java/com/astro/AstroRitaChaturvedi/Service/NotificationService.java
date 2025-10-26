package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.NotificationModel;
import com.astro.AstroRitaChaturvedi.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public NotificationModel createNotification(UUID userId, String title, String message, 
                                              NotificationModel.NotificationType type) {
        NotificationModel notification = new NotificationModel(userId, title, message, type);
        notification = notificationRepository.save(notification);
        
        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSendToUser(
            userId.toString(), 
            "/queue/notifications", 
            notification
        );
        
        return notification;
    }
    
    public NotificationModel createNotificationWithAction(UUID userId, String title, String message, 
                                                        NotificationModel.NotificationType type, String actionUrl) {
        NotificationModel notification = new NotificationModel(userId, title, message, type);
        notification.setActionUrl(actionUrl);
        notification = notificationRepository.save(notification);
        
        messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/notifications", notification);
        return notification;
    }
    
    public List<NotificationModel> getUserNotifications(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<NotificationModel> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }
    
    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
    
    public void markAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
    
    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }
    
    // Notification helpers for different events
    public void notifyCallRequest(UUID astrologerId, UUID userId, String userName) {
        createNotificationWithAction(
            astrologerId,
            "New Call Request",
            userName + " wants to start a call with you",
            NotificationModel.NotificationType.CALL_REQUEST,
            "/astrologer/calls"
        );
    }
    
    public void notifyCallAccepted(UUID userId, String astrologerName) {
        createNotification(
            userId,
            "Call Accepted",
            astrologerName + " accepted your call request",
            NotificationModel.NotificationType.CALL_ACCEPTED
        );
    }
    
    public void notifyCallRejected(UUID userId, String astrologerName) {
        createNotification(
            userId,
            "Call Declined",
            astrologerName + " declined your call request",
            NotificationModel.NotificationType.CALL_REJECTED
        );
    }
    
    public void notifyChatMessage(UUID userId, String senderName, String message) {
        createNotificationWithAction(
            userId,
            "New Message from " + senderName,
            message.length() > 50 ? message.substring(0, 50) + "..." : message,
            NotificationModel.NotificationType.CHAT_MESSAGE,
            "/chat"
        );
    }
    
    public void notifyPaymentSuccess(UUID userId, double amount) {
        createNotification(
            userId,
            "Payment Successful",
            "Your payment of ₹" + amount + " was processed successfully",
            NotificationModel.NotificationType.PAYMENT_SUCCESS
        );
    }
    
    public void notifyWalletRecharge(UUID userId, double amount) {
        createNotificationWithAction(
            userId,
            "Wallet Recharged",
            "₹" + amount + " has been added to your wallet",
            NotificationModel.NotificationType.WALLET_RECHARGED,
            "/user/wallet"
        );
    }
    
    public void notifyAstrologerApproval(UUID astrologerId, boolean approved) {
        String title = approved ? "Application Approved" : "Application Rejected";
        String message = approved ? 
            "Congratulations! Your astrologer application has been approved" :
            "Your astrologer application has been rejected. Please contact support for details";
        
        NotificationModel.NotificationType type = approved ? 
            NotificationModel.NotificationType.ASTROLOGER_APPROVED :
            NotificationModel.NotificationType.ASTROLOGER_REJECTED;
            
        createNotification(astrologerId, title, message, type);
    }
    
    @Transactional
    public void cleanupOldNotifications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteOldNotifications(cutoffDate);
    }
}