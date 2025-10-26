package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Repository.AstrologerRepository;
import com.astro.AstroRitaChaturvedi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AstrologerService {

    @Autowired
    private AstrologerRepository astrologerRepository;

    @Autowired
    private UserRepository userRepository;

    public AstrologerModel registerAstrologer(AstrologerModel astrologer, UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set user role to ASTROLOGER
        user.setRole(UserModel.UserRole.ASTROLOGER);
        userRepository.save(user);
        astrologer.setUserid(userId.toString());
        // Link astrologer to user
        astrologer.setUser(user);
        astrologer.setApprovalStatus(AstrologerModel.ApprovalStatus.PENDING);

        return astrologerRepository.save(astrologer);
    }

    public AstrologerModel approveAstrologer(UUID astrologerId) {
        AstrologerModel astrologer = astrologerRepository.findById(astrologerId)
                .orElseThrow(() -> new RuntimeException("Astrologer not found"));

        astrologer.setApprovalStatus(AstrologerModel.ApprovalStatus.APPROVED);
        return astrologerRepository.save(astrologer);
    }


    public AstrologerModel rejectAstrologer(UUID astrologerId) {
        AstrologerModel astrologer = astrologerRepository.findById(astrologerId)
                .orElseThrow(() -> new RuntimeException("Astrologer not found"));

        astrologer.setApprovalStatus(AstrologerModel.ApprovalStatus.REJECTED);
        return astrologerRepository.save(astrologer);
    }

    public List<AstrologerModel> getAllAstrologers() {
        return astrologerRepository.findAll();
    }

    public List<AstrologerModel> getPendingAstrologers() {
        return astrologerRepository.findByApprovalStatus(AstrologerModel.ApprovalStatus.PENDING);
    }

    public List<AstrologerModel> getAvailableAstrologers() {
        return astrologerRepository.findByAvailableAndApprovalStatus(true, AstrologerModel.ApprovalStatus.APPROVED);
    }

    public AstrologerModel getAstrologerById(UUID id) {
        return astrologerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Astrologer not found"));
    }

    public AstrologerModel updateAstrologerAvailability(UUID astrologerId, boolean available) {
        AstrologerModel astrologer = astrologerRepository.findById(astrologerId)
                .orElseThrow(() -> new RuntimeException("Astrologer not found"));

        astrologer.setAvailable(available);
        return astrologerRepository.save(astrologer);
    }

    public AstrologerModel updateAstrologerProfile(UUID astrologerId, AstrologerModel updatedAstrologer) {
        AstrologerModel astrologer = astrologerRepository.findById(astrologerId)
                .orElseThrow(() -> new RuntimeException("Astrologer not found"));

        // Update fields
        astrologer.setExpertise(updatedAstrologer.getExpertise());
        astrologer.setExperience(updatedAstrologer.getExperience());
        astrologer.setQualifications(updatedAstrologer.getQualifications());
        astrologer.setBio(updatedAstrologer.getBio());
        astrologer.setProfilePicture(updatedAstrologer.getProfilePicture());
        astrologer.setChatRatePerMinute(updatedAstrologer.getChatRatePerMinute());
        astrologer.setCallRatePerMinute(updatedAstrologer.getCallRatePerMinute());

        return astrologerRepository.save(astrologer);
    }
}
