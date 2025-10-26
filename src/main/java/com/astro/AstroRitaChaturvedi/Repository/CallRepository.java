package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CallRepository extends JpaRepository<SessionModel, UUID> {
    long countByUserCustomerIdAndSessionType(UUID userId, SessionModel.SessionType sessionType);
    long countByAstrologerAstrologerIdAndSessionType(UUID astrologerId, SessionModel.SessionType sessionType);
    long countByUserCustomerIdAndSessionTypeAndStatus(UUID userId, SessionModel.SessionType sessionType, SessionModel.SessionStatus status);
    long countByAstrologerAstrologerIdAndSessionTypeAndStatus(UUID astrologerId, SessionModel.SessionType sessionType, SessionModel.SessionStatus status);
}