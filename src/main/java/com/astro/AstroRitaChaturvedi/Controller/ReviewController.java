package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.Model.ReviewModel;
import com.astro.AstroRitaChaturvedi.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<ReviewModel> createReview(@RequestBody Map<String, Object> request) {
        UUID userId = UUID.fromString((String) request.get("userId"));
        UUID astrologerId = UUID.fromString((String) request.get("astrologerId"));
        int rating = (Integer) request.get("rating");
        String comment = (String) request.get("comment");
        
        ReviewModel review = reviewService.createReview(userId, astrologerId, rating, comment);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/astrologer/{astrologerId}")
    public ResponseEntity<List<ReviewModel>> getAstrologerReviews(@PathVariable UUID astrologerId) {
        List<ReviewModel> reviews = reviewService.getAstrologerReviews(astrologerId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewModel>> getUserReviews(@PathVariable UUID userId) {
        List<ReviewModel> reviews = reviewService.getUserReviews(userId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/astrologer/{astrologerId}/rating")
    public ResponseEntity<Map<String, Object>> getAstrologerRating(@PathVariable UUID astrologerId) {
        Double avgRating = reviewService.getAstrologerAverageRating(astrologerId);
        long reviewCount = reviewService.getAstrologerReviewCount(astrologerId);
        
        return ResponseEntity.ok(Map.of(
            "averageRating", avgRating != null ? avgRating : 0.0,
            "reviewCount", reviewCount
        ));
    }
}