package com.astro.AstroRitaChaturvedi.DTO;

import com.astro.AstroRitaChaturvedi.Model.SessionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel; // Ensure UserModel is imported
import com.astro.AstroRitaChaturvedi.Model.AstrologerModel; // Ensure AstrologerModel is imported

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionDTO {
    private String sessionId;
    private String userId; // UserModel PK of the user
    private String userFullName;
    private String userUsername; // Username of the user

    private String astrologerUserId; // UserModel PK of the astrologer
    private String astrologerModelId; // AstrologerModel PK
    private String astrologerFullName;
    private String astrologerUsername; // Username of the astrologer

    private String sessionType;
    private String status;
    private String startTime;
    private String endTime;
    private Integer durationMinutes;
    private Double totalAmount;
    private String callRequestId;

    public SessionDTO(SessionModel session) {
        this.sessionId = session.getSessionId().toString();

        UserModel userEntity = session.getUser();
        if (userEntity != null) {
            this.userId = userEntity.getCustomerId().toString();
            this.userFullName = userEntity.getFullName();
            this.userUsername = userEntity.getUsername();
        }

        AstrologerModel astrologerEntity = session.getAstrologer();
        if (astrologerEntity != null) {
            this.astrologerModelId = astrologerEntity.getAstrologerId().toString();
            UserModel astrologerUserEntity = astrologerEntity.getUser();
            if (astrologerUserEntity != null) {
                this.astrologerUserId = astrologerUserEntity.getCustomerId().toString();
                this.astrologerFullName = astrologerUserEntity.getFullName();
                this.astrologerUsername = astrologerUserEntity.getUsername();
            }
        }

        this.sessionType = session.getSessionType().name();
        this.status = session.getStatus().name();
        this.startTime = session.getStartTime() != null ? session.getStartTime().toString() : null;
        this.endTime = session.getEndTime() != null ? session.getEndTime().toString() : null;
        this.durationMinutes = session.getDurationMinutes();
        this.totalAmount = session.getTotalAmount();
        if (session.getCallRequest() != null) {
            this.callRequestId = session.getCallRequest().getRequestId().toString();
        }
    }

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public String getUserUsername() { return userUsername; }
    public void setUserUsername(String userUsername) { this.userUsername = userUsername; }
    public String getAstrologerUserId() { return astrologerUserId; }
    public void setAstrologerUserId(String astrologerUserId) { this.astrologerUserId = astrologerUserId; }
    public String getAstrologerModelId() { return astrologerModelId; }
    public void setAstrologerModelId(String astrologerModelId) { this.astrologerModelId = astrologerModelId; }
    public String getAstrologerFullName() { return astrologerFullName; }
    public void setAstrologerFullName(String astrologerFullName) { this.astrologerFullName = astrologerFullName; }
    public String getAstrologerUsername() { return astrologerUsername; }
    public void setAstrologerUsername(String astrologerUsername) { this.astrologerUsername = astrologerUsername; }
    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getCallRequestId() { return callRequestId; }
    public void setCallRequestId(String callRequestId) { this.callRequestId = callRequestId; }
}
