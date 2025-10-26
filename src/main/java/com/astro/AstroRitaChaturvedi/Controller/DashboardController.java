package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserDashboardStats(@PathVariable UUID userId) {
        Map<String, Object> stats = dashboardService.getUserDashboardStats(userId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/astrologer/{astrologerId}/stats")
    public ResponseEntity<Map<String, Object>> getAstrologerDashboardStats(@PathVariable UUID astrologerId) {
        Map<String, Object> stats = dashboardService.getAstrologerDashboardStats(astrologerId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/admin/stats")
    public ResponseEntity<Map<String, Object>> getAdminDashboardStats() {
        Map<String, Object> stats = dashboardService.getAdminDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/home/stats")
    public ResponseEntity<Map<String, Object>> getHomePageStats() {
        Map<String, Object> stats = dashboardService.getHomePageStats();
        return ResponseEntity.ok(stats);
    }
}