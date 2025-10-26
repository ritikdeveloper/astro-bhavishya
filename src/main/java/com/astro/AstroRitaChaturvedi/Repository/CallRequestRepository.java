package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.CallRequestModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CallRequestRepository extends JpaRepository<CallRequestModel, UUID> {
    List<CallRequestModel> findByUser(UserModel user);
    List<CallRequestModel> findByAstrologer(AstrologerModel astrologer);
    List<CallRequestModel> findByAstrologerAndStatus(AstrologerModel astrologer, CallRequestModel.CallStatus status);
}
