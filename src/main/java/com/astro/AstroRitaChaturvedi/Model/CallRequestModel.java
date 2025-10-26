package com.astro.AstroRitaChaturvedi.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "call_requests")
@Getter
@Setter
public class CallRequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"wallet", "transactions", "sentMessages", "receivedMessages"})
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astrologer_id", nullable = false)
    @JsonIgnoreProperties({"user", "sessions"})
    private AstrologerModel astrologer;

    private String userPhoneNumber;

    @Enumerated(EnumType.STRING)
    private CallStatus status;

    private LocalDateTime requestTime;
    private LocalDateTime callbackTime;

    @OneToOne(mappedBy = "callRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("callRequest")
    private SessionModel session;

    @PrePersist
    protected void onCreate() {
        requestTime = LocalDateTime.now();
    }

    public enum CallStatus {
        REQUESTED, ACCEPTED, REJECTED, COMPLETED
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
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

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(LocalDateTime callbackTime) {
        this.callbackTime = callbackTime;
    }

    public SessionModel getSession() {
        return session;
    }

    public void setSession(SessionModel session) {
        this.session = session;
    }
}
