package com.astro.AstroRitaChaturvedi.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "astrologers")
@Getter
@Setter
@JsonIgnoreProperties({"sessions"})
public class AstrologerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID astrologerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserModel user;
    private String userid;
    private String expertise;
    private String experience;
    private String qualifications;
    private String bio;
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    private double chatRatePerMinute = 0.83; // ₹50/hour = ₹0.83/minute
    private double callRatePerMinute = 0.83; // ₹50/hour = ₹0.83/minute

    private boolean available = false;
    @Column(name = "average_rating")
    private double averageRating = 0.0;
    
    @Column(name = "total_reviews")
    private int totalReviews = 0;

    @OneToMany(mappedBy = "astrologer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SessionModel> sessions;

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public UUID getAstrologerId() {
        return astrologerId;
    }

    public void setAstrologerId(UUID astrologerId) {
        this.astrologerId = astrologerId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public double getChatRatePerMinute() {
        return chatRatePerMinute;
    }

    public void setChatRatePerMinute(double chatRatePerMinute) {
        this.chatRatePerMinute = chatRatePerMinute;
    }

    public double getCallRatePerMinute() {
        return callRatePerMinute;
    }

    public void setCallRatePerMinute(double callRatePerMinute) {
        this.callRatePerMinute = callRatePerMinute;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<SessionModel> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionModel> sessions) {
        this.sessions = sessions;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }
}
