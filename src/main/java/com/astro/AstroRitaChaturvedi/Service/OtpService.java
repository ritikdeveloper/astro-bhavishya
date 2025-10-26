package com.astro.AstroRitaChaturvedi.Service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {
    
    public String generateOtp() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    public void sendOtp(String phoneNumber, String otp) {
        // In a real application, this would send an SMS
        // For now, we'll just log it
        System.out.println("Sending OTP " + otp + " to " + phoneNumber);
        
        // Implementation would use an SMS gateway service
        // Example: Twilio, MSG91, etc.
    }
}
