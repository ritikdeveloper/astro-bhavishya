package com.astro.AstroRitaChaturvedi.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;
    
    @Column(nullable = false)
    private UUID userId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Column(nullable = false)
    private boolean isRead = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private String actionUrl;
    private String metadata; // JSON string for additional data
    
    public enum NotificationType {
        CALL_REQUEST, CALL_ACCEPTED, CALL_REJECTED, CALL_COMPLETED,
        CHAT_REQUEST, CHAT_MESSAGE, CHAT_ENDED,
        PAYMENT_SUCCESS, PAYMENT_FAILED, WALLET_RECHARGED,
        ASTROLOGER_APPROVED, ASTROLOGER_REJECTED,
        SYSTEM_ANNOUNCEMENT, PROFILE_UPDATE
    }
    
    // Constructors
    public NotificationModel() {}
    
    public NotificationModel(UUID userId, String title, String message, NotificationType type) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
    }
    
    // Getters and Setters
    public UUID getNotificationId() { return notificationId; }
    public void setNotificationId(UUID notificationId) { this.notificationId = notificationId; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
    
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}