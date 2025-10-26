package com.astro.AstroRitaChaturvedi.DTO;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AstrologerDTO {
    private String astrologerId;
    private String expertise;
    private String experience;
    private String qualifications;
    private String bio;
    private String profilePicture;
    private String approvalStatus; // Enum as String
    private double chatRatePerMinute;
    private double callRatePerMinute;
    private boolean available;

    // Flattened user details
    private String userId; // This is the user's customerId (UUID as String)
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;

    public AstrologerDTO(AstrologerModel astrologer) {
        this.astrologerId = astrologer.getAstrologerId().toString();
        this.expertise = astrologer.getExpertise();
        this.experience = astrologer.getExperience();
        this.qualifications = astrologer.getQualifications();
        this.bio = astrologer.getBio();
        this.profilePicture = astrologer.getProfilePicture();
        if (astrologer.getApprovalStatus() != null) {
            this.approvalStatus = astrologer.getApprovalStatus().name(); // Crucial: .name() for enum
        }
        this.chatRatePerMinute = astrologer.getChatRatePerMinute();
        this.callRatePerMinute = astrologer.getCallRatePerMinute();
        this.available = astrologer.isAvailable(); // Crucial: isAvailable() for boolean

        if (astrologer.getUser() != null) {
            this.userId = astrologer.getUser().getCustomerId().toString(); // Crucial: map UserModel's customerId
            this.username = astrologer.getUser().getUsername();
            this.email = astrologer.getUser().getEmail();
            this.fullName = astrologer.getUser().getFullName();
            this.phoneNumber = astrologer.getUser().getPhoneNumber();
        } else if (astrologer.getUserid() != null && !astrologer.getUserid().isEmpty()){
            // Fallback if user object is somehow not loaded but userid string is present
            // This case should ideally not happen for a fully loaded astrologer
            this.userId = astrologer.getUserid();
        }
    }

    public String getAstrologerId() {
        return astrologerId;
    }

    public void setAstrologerId(String astrologerId) {
        this.astrologerId = astrologerId;
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

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
