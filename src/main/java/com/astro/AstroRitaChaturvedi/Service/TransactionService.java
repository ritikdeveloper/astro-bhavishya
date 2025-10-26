package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.DTO.TransactionDTO;
import com.astro.AstroRitaChaturvedi.Model.TransactionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Repository.TransactionRepository;
import com.astro.AstroRitaChaturvedi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<TransactionModel> getUserTransactions(UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return transactionRepository.findByUser(user);
    }
    
    public TransactionModel getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
    
    public List<TransactionModel> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<TransactionDTO> getAllTransactionDTOs() {
        return transactionRepository.findAll().stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }

}
