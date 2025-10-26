package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewModel, UUID> {
    List<ReviewModel> findByAstrologer_AstrologerIdOrderByCreatedAtDesc(UUID astrologerId);
    List<ReviewModel> findByUser_CustomerIdOrderByCreatedAtDesc(UUID userId);
    long countByAstrologer_AstrologerId(UUID astrologerId);
    
    @Query("SELECT AVG(r.rating) FROM ReviewModel r WHERE r.astrologer.astrologerId = :astrologerId")
    Double getAverageRatingByAstrologerId(@Param("astrologerId") UUID astrologerId);
}