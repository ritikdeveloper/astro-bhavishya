package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.SessionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<SessionModel, UUID> {
    List<SessionModel> findByUser(UserModel user);
    List<SessionModel> findByAstrologer(AstrologerModel astrologer);
    List<SessionModel> findByUserAndStatus(UserModel user, SessionModel.SessionStatus status);
    List<SessionModel> findByAstrologerAndStatus(AstrologerModel astrologer, SessionModel.SessionStatus status);
    List<SessionModel> findByUserOrderByStartTimeDesc(UserModel user);
    List<SessionModel> findByAstrologerOrderByStartTimeDesc(AstrologerModel astrologer);

    // Method to find sessions by AstrologerModel and status, ordered
    List<SessionModel> findByAstrologerAndStatusOrderByStartTimeDesc(AstrologerModel astrologer, SessionModel.SessionStatus status);

    // Method to find sessions by the UserModel associated with the Astrologer, and status, ordered
    @Query("SELECT s FROM SessionModel s WHERE s.astrologer.user = :astrologerUser AND s.status = :status ORDER BY s.startTime DESC")
    List<SessionModel> findByAstrologerUserAndStatusOrderByStartTimeDesc(@Param("astrologerUser") UserModel astrologerUser, @Param("status") SessionModel.SessionStatus status);

    // Method to find all sessions by the UserModel associated with the Astrologer, ordered
    @Query("SELECT s FROM SessionModel s WHERE s.astrologer.user = :astrologerUser ORDER BY s.startTime DESC")
    List<SessionModel> findByAstrologerUserOrderByStartTimeDesc(@Param("astrologerUser") UserModel astrologerUser);
}
