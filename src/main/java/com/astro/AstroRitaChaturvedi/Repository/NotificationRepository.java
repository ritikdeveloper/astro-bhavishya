package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {
    
    List<NotificationModel> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    List<NotificationModel> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(UUID userId);
    
    long countByUserIdAndIsReadFalse(UUID userId);
    
    @Modifying
    @Query("UPDATE NotificationModel n SET n.isRead = true WHERE n.userId = :userId")
    void markAllAsReadByUserId(@Param("userId") UUID userId);
    
    @Modifying
    @Query("DELETE FROM NotificationModel n WHERE n.createdAt < :cutoffDate")
    void deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);
}