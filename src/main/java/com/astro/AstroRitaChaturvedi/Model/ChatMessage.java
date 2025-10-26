package com.astro.AstroRitaChaturvedi.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@JsonIgnoreProperties(
        value = {"hibernateLazyInitializer", "handler"},
        allowSetters = true,
        ignoreUnknown = true
)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @JsonIgnoreProperties({ // Ensure these properties of UserModel are not serialized when ChatMessage.sender is serialized
            "wallet", "transactions", "sentMessages", "receivedMessages",
            "password", "otp", "otpExpiry", "authorities",
            "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled",
            "hibernateLazyInitializer", "handler"
    })
    private UserModel sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @JsonIgnoreProperties({ // Ensure these properties of UserModel are not serialized when ChatMessage.receiver is serialized
            "wallet", "transactions", "sentMessages", "receivedMessages",
            "password", "otp", "otpExpiry", "authorities",
            "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled",
            "hibernateLazyInitializer", "handler"
    })
    private UserModel receiver;

    @Column(nullable = false)
    private String content;

    private LocalDateTime timestamp;

    private boolean read;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    @JsonIgnoreProperties({ // Ensure these properties of SessionModel are not serialized
            "user", "astrologer", "messages", "transactions",
            "hibernateLazyInitializer", "handler"
    })
    private SessionModel session;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public SessionModel getSession() {
        return session;
    }

    public void setSession(SessionModel session) {
        this.session = session;
    }

    // Getters and Setters... (Lombok's @Getter @Setter handle this)
}
