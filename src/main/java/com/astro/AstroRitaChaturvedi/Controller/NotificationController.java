package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.Model.NotificationModel;
import com.astro.AstroRitaChaturvedi.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/{userId}")
    @PreAuthorize("authentication.principal.customerId == #userId or hasRole('ADMIN')")
    public ResponseEntity<List<NotificationModel>> getUserNotifications(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }
    
    @GetMapping("/{userId}/unread")
    @PreAuthorize("authentication.principal.customerId == #userId or hasRole('ADMIN')")
    public ResponseEntity<List<NotificationModel>> getUnreadNotifications(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }
    
    @GetMapping("/{userId}/count")
    @PreAuthorize("authentication.principal.customerId == #userId or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable UUID userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
    
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{userId}/read-all")
    @PreAuthorize("authentication.principal.customerId == #userId or hasRole('ADMIN')")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}