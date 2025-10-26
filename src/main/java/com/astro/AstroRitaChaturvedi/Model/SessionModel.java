package com.astro.AstroRitaChaturvedi.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@JsonIgnoreProperties({"messages", "transactions"})
public class SessionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"wallet", "transactions", "sentMessages", "receivedMessages"})
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astrologer_id", nullable = false)
    @JsonIgnoreProperties({"user", "sessions"})
    private AstrologerModel astrologer;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private double totalAmount;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> messages;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionModel> transactions;

    // Add this field to match the mappedBy in CallRequestModel
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_request_id")
    @JsonIgnoreProperties("session")
    private CallRequestModel callRequest;

    public enum SessionType {
        CHAT, CALL
    }

    public enum SessionStatus {
        SCHEDULED, ACTIVE, COMPLETED, CANCELLED
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public AstrologerModel getAstrologer() {
        return astrologer;
    }

    public void setAstrologer(AstrologerModel astrologer) {
        this.astrologer = astrologer;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }

    public CallRequestModel getCallRequest() {
        return callRequest;
    }

    public void setCallRequest(CallRequestModel callRequest) {
        this.callRequest = callRequest;
    }
}
