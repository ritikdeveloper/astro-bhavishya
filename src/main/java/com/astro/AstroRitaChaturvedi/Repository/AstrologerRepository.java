package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AstrologerRepository extends JpaRepository<AstrologerModel, UUID> {
    List<AstrologerModel> findByApprovalStatus(AstrologerModel.ApprovalStatus status);
    List<AstrologerModel> findByAvailableAndApprovalStatus(boolean available, AstrologerModel.ApprovalStatus status);
    Optional<AstrologerModel> findByUser(UserModel user);
    long countByApprovalStatus(AstrologerModel.ApprovalStatus status);

    // Find astrologer by their associated UserModel's customerId
    @Query("SELECT a FROM AstrologerModel a WHERE a.user.customerId = :userCustomerId")
    Optional<AstrologerModel> findByUserCustomerId(@Param("userCustomerId") UUID userCustomerId);
    
    @Query("SELECT AVG(a.averageRating) FROM AstrologerModel a WHERE a.approvalStatus = 'APPROVED' AND a.averageRating > 0")
    Double getAverageRatingOfAllAstrologers();
}
