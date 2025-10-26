package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.DTO.TransactionDTO;
import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.TransactionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Service.AstrologerService;
import com.astro.AstroRitaChaturvedi.Service.TransactionService;
import com.astro.AstroRitaChaturvedi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AstrologerService astrologerService;
    
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/astrologers")
    public ResponseEntity<List<AstrologerModel>> getAllAstrologers() {
        return ResponseEntity.ok(astrologerService.getAllAstrologers());
    }
    
    @GetMapping("/astrologers/pending")
    public ResponseEntity<List<AstrologerModel>> getPendingAstrologers() {
        return ResponseEntity.ok(astrologerService.getPendingAstrologers());
    }
    
    @PutMapping("/astrologers/{id}/approve")
    public ResponseEntity<AstrologerModel> approveAstrologer(@PathVariable UUID id) {
        return ResponseEntity.ok(astrologerService.approveAstrologer(id));
    }
    
    @PutMapping("/astrologers/{id}/reject")
    public ResponseEntity<AstrologerModel> rejectAstrologer(@PathVariable UUID id) {
        return ResponseEntity.ok(astrologerService.rejectAstrologer(id));
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() { // Return DTO
        return ResponseEntity.ok(transactionService.getAllTransactionDTOs());
    }
    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionModel> getTransactionById(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
