package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Repository.AstrologerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AstrologerAvailabilityService {
    
    @Autowired
    private AstrologerRepository astrologerRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public void updateAvailabilityStatus(UUID astrologerId, boolean isAvailable) {
        astrologerRepository.findById(astrologerId).ifPresent(astrologer -> {
            astrologer.setAvailable(isAvailable);
            astrologerRepository.save(astrologer);
            
            // Broadcast availability change to all users
            messagingTemplate.convertAndSend("/topic/astrologer-status", 
                new AvailabilityUpdate(astrologerId, isAvailable));
        });
    }
    
    public void updateLastActiveTime(UUID astrologerId) {
        // Method kept for compatibility but no action needed
    }
    
    public List<AstrologerModel> getAvailableAstrologers() {
        return astrologerRepository.findByAvailableAndApprovalStatus(true, AstrologerModel.ApprovalStatus.APPROVED);
    }
    
    public void setAstrologerBusy(UUID astrologerId, String reason) {
        astrologerRepository.findById(astrologerId).ifPresent(astrologer -> {
            astrologer.setAvailable(false);
            astrologerRepository.save(astrologer);
            
            messagingTemplate.convertAndSend("/topic/astrologer-status", 
                new AvailabilityUpdate(astrologerId, false, reason));
        });
    }
    
    // Inner class for WebSocket updates
    public static class AvailabilityUpdate {
        private UUID astrologerId;
        private boolean available;
        private String reason;
        
        public AvailabilityUpdate(UUID astrologerId, boolean available) {
            this.astrologerId = astrologerId;
            this.available = available;
        }
        
        public AvailabilityUpdate(UUID astrologerId, boolean available, String reason) {
            this.astrologerId = astrologerId;
            this.available = available;
            this.reason = reason;
        }
        
        // Getters
        public UUID getAstrologerId() { return astrologerId; }
        public boolean isAvailable() { return available; }
        public String getReason() { return reason; }
    }
}