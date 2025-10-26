package com.astro.AstroRitaChaturvedi.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "userdata")
@Getter
@Setter
@JsonIgnoreProperties(
        value = {
                "password", "otp", "otpExpiry", // Sensitive and internal data
                "sentMessages", "receivedMessages", "transactions", // Collections that can cause deep serialization / lazy loading issues
                "hibernateLazyInitializer", "handler" // Hibernate proxy internal fields
        },
        allowSetters = true, // Important if you use this model for request bodies too
        ignoreUnknown = true // Good practice
)
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerId;

    @Column(unique = true, nullable = false)
    private String username;

    // Password will be ignored during serialization due to class-level @JsonIgnoreProperties
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    private String email;
    private String fullName;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Helps with bidirectional relationship with WalletModel
    // WalletModel itself should use @JsonBackReference for its 'user' field
    // If wallet is still an issue, add "wallet" to class-level @JsonIgnoreProperties
    private WalletModel wallet;

    // These collections will be ignored during serialization due to class-level @JsonIgnoreProperties
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionModel> transactions;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> receivedMessages;

    // For OTP verification - otp & otpExpiry ignored by class-level annotation
    private String otp;
    private LocalDate otpExpiry;
    private boolean verified = false; // Consider if 'verified' should be serialized

    public enum UserRole {
        USER, ASTROLOGER, ADMIN
    }

    // UserDetails methods - these are not properties so usually not an issue for Jackson
    // but explicitly listing them in @JsonIgnoreProperties if they were can be done.
    // For now, relying on them not being standard bean properties.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verified;
    }

    // Getters and setters... (Lombok's @Getter @Setter handle this)
    // Ensure explicit getters/setters if not using Lombok or if specific logic is needed.
    public UUID getId() { // Alias for customerId often used in frontend or other contexts
        return customerId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public WalletModel getWallet() {
        return wallet;
    }

    public void setWallet(WalletModel wallet) {
        this.wallet = wallet;
    }

    public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }

    public List<ChatMessage> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<ChatMessage> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<ChatMessage> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<ChatMessage> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDate getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDate otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
