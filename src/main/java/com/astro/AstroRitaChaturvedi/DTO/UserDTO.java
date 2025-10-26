package com.astro.AstroRitaChaturvedi.DTO;

import com.astro.AstroRitaChaturvedi.Model.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private UUID customerId;
    private String username;
    // Excluded: password, otp, otpExpiry
    private String phoneNumber;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private String role; // String representation of UserRole enum
    private boolean verified;

    public UserDTO(UserModel user) {
        if (user == null) return;
        this.customerId = user.getCustomerId();
        this.username = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.dateOfBirth = user.getDateOfBirth();
        if (user.getRole() != null) {
            this.role = user.getRole().name();
        }
        this.verified = user.isVerified();
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
