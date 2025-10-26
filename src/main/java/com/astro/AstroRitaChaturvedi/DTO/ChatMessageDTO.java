package com.astro.AstroRitaChaturvedi.DTO;

import com.astro.AstroRitaChaturvedi.Model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ChatMessageDTO {
    private UUID id;
    private UUID senderId;
    private String senderUsername;
    private String senderFullName;
    private UUID receiverId;
    private String receiverUsername;
    private String receiverFullName;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
    private UUID sessionId;

    public ChatMessageDTO(ChatMessage message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
        this.read = message.isRead();

        if (message.getSession() != null) {
            this.sessionId = message.getSession().getSessionId();
        }

        if (message.getSender() != null) {
            this.senderId = message.getSender().getCustomerId();
            this.senderUsername = message.getSender().getUsername();
            this.senderFullName = message.getSender().getFullName();
        }

        if (message.getReceiver() != null) {
            this.receiverId = message.getReceiver().getCustomerId();
            this.receiverUsername = message.getReceiver().getUsername();
            this.receiverFullName = message.getReceiver().getFullName();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
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

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
