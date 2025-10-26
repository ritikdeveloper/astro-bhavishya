package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.DTO.AstrologerDTO;
// Assuming AstrologerApplicationPayload equivalent DTO exists for registration if needed,
// or AstrologerModel is used for simplicity in request body for now.
import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Service.AstrologerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/astrologers")
public class AstrologerController {

    @Autowired
    private AstrologerService astrologerService;

    @GetMapping
    public ResponseEntity<List<AstrologerDTO>> getAllAstrologers() {
        List<AstrologerModel> models = astrologerService.getAllAstrologers();
        List<AstrologerDTO> dtos = models.stream().map(AstrologerDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AstrologerDTO>> getAvailableAstrologers() {
        List<AstrologerModel> models = astrologerService.getAvailableAstrologers();
        List<AstrologerDTO> dtos = models.stream().map(AstrologerDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AstrologerDTO> getAstrologerById(@PathVariable UUID id) {
        AstrologerModel model = astrologerService.getAstrologerById(id);
        AstrologerDTO dto = new AstrologerDTO(model);
        return ResponseEntity.ok(dto);
    }

    // For registration, the request body might be a simpler DTO like AstrologerApplicationPayload
    // For now, using AstrologerModel as request body for simplicity, but it contains fields like approvalStatus
    // which shouldn't be set by the user. Consider creating a specific DTO for the request.
    @PostMapping("/register/{userId}")
    public ResponseEntity<AstrologerDTO> registerAstrologer(
            @PathVariable UUID userId,
            @RequestBody AstrologerModel astrologerDetails) { // Renamed for clarity, or use a specific DTO
        AstrologerModel registeredAstrologer = astrologerService.registerAstrologer(astrologerDetails, userId);
        return ResponseEntity.ok(new AstrologerDTO(registeredAstrologer));
    }

    @PutMapping("/{id}/availability")
    @PreAuthorize("hasRole('ASTROLOGER') or hasRole('ADMIN')") // Allow admin to change too if needed
    public ResponseEntity<AstrologerDTO> updateAvailability(
            @PathVariable UUID id,
            @RequestBody boolean available) {
        AstrologerModel updatedAstrologer = astrologerService.updateAstrologerAvailability(id, available);
        return ResponseEntity.ok(new AstrologerDTO(updatedAstrologer));
    }

    // Similar to registration, the request body for profile update should ideally be a specific DTO.
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ASTROLOGER') or hasRole('ADMIN')")
    public ResponseEntity<AstrologerDTO> updateProfile(
            @PathVariable UUID id,
            @RequestBody AstrologerModel astrologerProfileDetails) { // Renamed for clarity, or use a specific DTO
        AstrologerModel updatedAstrologer = astrologerService.updateAstrologerProfile(id, astrologerProfileDetails);
        return ResponseEntity.ok(new AstrologerDTO(updatedAstrologer));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AstrologerDTO>> getPendingAstrologers() {
        List<AstrologerModel> models = astrologerService.getPendingAstrologers();
        List<AstrologerDTO> dtos = models.stream().map(AstrologerDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AstrologerDTO> approveAstrologer(@PathVariable UUID id) {
        AstrologerModel approvedAstrologer = astrologerService.approveAstrologer(id);
        return ResponseEntity.ok(new AstrologerDTO(approvedAstrologer));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AstrologerDTO> rejectAstrologer(@PathVariable UUID id) {
        AstrologerModel rejectedAstrologer = astrologerService.rejectAstrologer(id);
        return ResponseEntity.ok(new AstrologerDTO(rejectedAstrologer));
    }
}
