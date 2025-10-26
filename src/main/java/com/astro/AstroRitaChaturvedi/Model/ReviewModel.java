package com.astro.AstroRitaChaturvedi.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class ReviewModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reviewId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astrologer_id", nullable = false)
    private AstrologerModel astrologer;
    
    @Column(nullable = false)
    private int rating; // 1-5 stars
    
    @Column(length = 1000)
    private String comment;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    
    public enum ServiceType {
        CHAT, CALL, BOTH
    }
    
    // Constructors
    public ReviewModel() {}
    
    // Getters and Setters
    public UUID getReviewId() { return reviewId; }
    public void setReviewId(UUID reviewId) { this.reviewId = reviewId; }
    
    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }
    
    public AstrologerModel getAstrologer() { return astrologer; }
    public void setAstrologer(AstrologerModel astrologer) { this.astrologer = astrologer; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
}