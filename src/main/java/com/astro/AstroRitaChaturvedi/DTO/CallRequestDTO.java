package com.astro.AstroRitaChaturvedi.DTO;

import com.astro.AstroRitaChaturvedi.Model.CallRequestModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CallRequestDTO {
    private UUID requestId;
    private UUID userId;
    private String userFullName;
    private String userPhoneNumber;
    // private String userUsername;
    private UUID astrologerId;
    private String astrologerFullName;
    // private String astrologerUsername;
    private String status; 
    private LocalDateTime requestTime;
    private LocalDateTime callbackTime;
    private Integer durationMinutes; 
    private UUID sessionId;

    public CallRequestDTO(CallRequestModel callRequest) {
        if (callRequest == null) return;
        this.requestId = callRequest.getRequestId();
        if (callRequest.getUser() != null) {
            this.userId = callRequest.getUser().getCustomerId();
            this.userFullName = callRequest.getUser().getFullName();
            this.userPhoneNumber = callRequest.getUser().getPhoneNumber();
            // this.userUsername = callRequest.getUser().getUsername();
        }
        if (callRequest.getAstrologer() != null) {
            this.astrologerId = callRequest.getAstrologer().getAstrologerId();
            if (callRequest.getAstrologer().getUser() != null) {
                this.astrologerFullName = callRequest.getAstrologer().getUser().getFullName();
                // this.astrologerUsername = callRequest.getAstrologer().getUser().getUsername();
            }
        }
        if (callRequest.getStatus() != null) {
            this.status = callRequest.getStatus().name();
        }
        this.requestTime = callRequest.getRequestTime();
        this.callbackTime = callRequest.getCallbackTime();

        if (callRequest.getSession() != null) {
            this.sessionId = callRequest.getSession().getSessionId();
            this.durationMinutes = callRequest.getSession().getDurationMinutes();
        }
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public UUID getAstrologerId() {
        return astrologerId;
    }

    public void setAstrologerId(UUID astrologerId) {
        this.astrologerId = astrologerId;
    }

    public String getAstrologerFullName() {
        return astrologerFullName;
    }

    public void setAstrologerFullName(String astrologerFullName) {
        this.astrologerFullName = astrologerFullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
