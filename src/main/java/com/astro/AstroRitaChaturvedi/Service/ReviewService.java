package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.ReviewModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Repository.AstrologerRepository;
import com.astro.AstroRitaChaturvedi.Repository.ReviewRepository;
import com.astro.AstroRitaChaturvedi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private AstrologerRepository astrologerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public ReviewModel createReview(UUID userId, UUID astrologerId, int rating, String comment) {
        ReviewModel review = new ReviewModel();
        
        // Set user and astrologer objects
        UserModel user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        AstrologerModel astrologer = astrologerRepository.findById(astrologerId)
            .orElseThrow(() -> new RuntimeException("Astrologer not found"));
            
        review.setUser(user);
        review.setAstrologer(astrologer);
        review.setRating(rating);
        review.setComment(comment);
        
        ReviewModel savedReview = reviewRepository.save(review);
        
        // Update astrologer's average rating
        updateAstrologerRating(astrologerId);
        
        return savedReview;
    }
    
    public List<ReviewModel> getAstrologerReviews(UUID astrologerId) {
        return reviewRepository.findByAstrologer_AstrologerIdOrderByCreatedAtDesc(astrologerId);
    }
    
    public List<ReviewModel> getUserReviews(UUID userId) {
        return reviewRepository.findByUser_CustomerIdOrderByCreatedAtDesc(userId);
    }
    
    public Double getAstrologerAverageRating(UUID astrologerId) {
        return reviewRepository.getAverageRatingByAstrologerId(astrologerId);
    }
    
    public long getAstrologerReviewCount(UUID astrologerId) {
        return reviewRepository.countByAstrologer_AstrologerId(astrologerId);
    }
    
    private void updateAstrologerRating(UUID astrologerId) {
        Double avgRating = reviewRepository.getAverageRatingByAstrologerId(astrologerId);
        long reviewCount = reviewRepository.countByAstrologer_AstrologerId(astrologerId);
        
        astrologerRepository.findById(astrologerId).ifPresent(astrologer -> {
            astrologer.setAverageRating(avgRating != null ? avgRating : 0.0);
            astrologer.setTotalReviews((int) reviewCount);
            astrologerRepository.save(astrologer);
        });
    }
}