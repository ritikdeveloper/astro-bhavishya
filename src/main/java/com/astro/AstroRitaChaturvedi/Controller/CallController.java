package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.DTO.CallRequestDTO;
import com.astro.AstroRitaChaturvedi.Model.CallRequestModel;
import com.astro.AstroRitaChaturvedi.Service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/call")
public class CallController {

    @Autowired
    private CallService callService;

    @PostMapping("/requestcall")
    public ResponseEntity<CallRequestDTO> requestCall(@RequestBody Map<String, String> request) {
        UUID userId = UUID.fromString(request.get("userId"));
        UUID astrologerId = UUID.fromString(request.get("astrologerId"));
        CallRequestModel callRequest = callService.requestCall(userId, astrologerId);
        return ResponseEntity.ok(new CallRequestDTO(callRequest));
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestingcall(@RequestBody Map<String,String> request){
        try {
            UUID userId = UUID.fromString(request.get("userId"));
            UUID astrologerId = UUID.fromString(request.get("astrologerId"));
            CallRequestModel callRequest = callService.requestCall(userId, astrologerId);
            return ResponseEntity.ok(new CallRequestDTO(callRequest));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }


    @PostMapping("/accept/{requestId}")
    @PreAuthorize("hasRole('ASTROLOGER')")
    public ResponseEntity<CallRequestDTO> acceptCall(@PathVariable UUID requestId) {
        CallRequestModel callRequest = callService.acceptCallRequest(requestId);
        return ResponseEntity.ok(new CallRequestDTO(callRequest));
    }

    @PostMapping("/reject/{requestId}")
    @PreAuthorize("hasRole('ASTROLOGER')")
    public ResponseEntity<CallRequestDTO> rejectCall(@PathVariable UUID requestId) {
        CallRequestModel callRequest = callService.rejectCallRequest(requestId);
        return ResponseEntity.ok(new CallRequestDTO(callRequest));
    }

    @PostMapping("/complete/{requestId}")
    @PreAuthorize("hasRole('ASTROLOGER')")
    public ResponseEntity<CallRequestDTO> completeCall(
            @PathVariable UUID requestId,
            @RequestBody Map<String, Integer> request) {
        int durationMinutes = request.get("durationMinutes");
        System.out.println(durationMinutes);
        CallRequestModel callRequest = callService.completeCall(requestId, durationMinutes);
        return ResponseEntity.ok(new CallRequestDTO(callRequest));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CallRequestDTO>> getUserCalls(@PathVariable UUID userId) {
        List<CallRequestModel> calls = callService.getUserCallRequests(userId);
        List<CallRequestDTO> callDTOs = calls.stream()
                .map(CallRequestDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(callDTOs);
    }

    @GetMapping("/astrologer/{astrologerId}")
    @PreAuthorize("hasRole('ASTROLOGER') or hasRole('ADMIN')")
    public ResponseEntity<List<CallRequestDTO>> getAstrologerCalls(@PathVariable UUID astrologerId) {
        List<CallRequestModel> calls = callService.getAstrologerCallRequests(astrologerId);
        List<CallRequestDTO> callDTOs = calls.stream()
                .map(CallRequestDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(callDTOs);
    }

    @GetMapping("/pending/{astrologerId}")
    @PreAuthorize("hasRole('ASTROLOGER') or hasRole('ADMIN')")
    public ResponseEntity<List<CallRequestDTO>> getPendingCalls(@PathVariable UUID astrologerId) {
        System.out.println("Getting pending calls for astrologer: " + astrologerId);
        List<CallRequestModel> calls = callService.getPendingCallRequests(astrologerId);
        List<CallRequestDTO> callDTOs = calls.stream()
                .map(CallRequestDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(callDTOs);
    }
}
