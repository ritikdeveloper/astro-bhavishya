package com.astro.AstroRitaChaturvedi.Service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.HashMap;

@Service
public class PhoneEmailService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneEmailService.class);
    private final RestTemplate restTemplate;

    public PhoneEmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class PhoneEmailUserDetails {
        public String countryCode;
        public String phoneNumber;
        public String firstName;
        public String lastName;
        public String fullPhoneNumber; // e.g., +11234567890

        public PhoneEmailUserDetails(String countryCode, String phoneNumber, String firstName, String lastName) {
            this.countryCode = countryCode;
            this.phoneNumber = phoneNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            this.fullPhoneNumber = (countryCode != null && phoneNumber != null) ? countryCode + phoneNumber : null;
        }
    }

    public PhoneEmailUserDetails fetchUserDetails(String userJsonUrl) {
        try {
            if (!isValidUrl(userJsonUrl)) {
                logger.warn("Invalid or unsafe URL provided: {}", sanitizeForLog(userJsonUrl));
                return null;
            }

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userJsonUrl, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                JSONObject jsonObject = new JSONObject(responseEntity.getBody());
                String userCountryCode = jsonObject.optString("user_country_code", null);
                String userPhoneNumber = jsonObject.optString("user_phone_number", null);
                String userFirstName = jsonObject.optString("user_first_name", null);
                String userLastName = jsonObject.optString("user_last_name", null);

                logger.info("Successfully fetched user details from Phone.email service");

                return new PhoneEmailUserDetails(userCountryCode, userPhoneNumber, userFirstName, userLastName);
            } else {
                logger.error("Failed to fetch user details from Phone.email. Status: {}", responseEntity.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching or parsing user details from Phone.email service", e);
            return null;
        }
    }
    
    private boolean isValidUrl(String urlString) {
        try {
            java.net.URL url = new java.net.URL(urlString);
            String protocol = url.getProtocol().toLowerCase();
            String host = url.getHost().toLowerCase();
            
            // Only allow HTTPS URLs
            if (!"https".equals(protocol)) {
                logger.warn("Non-HTTPS URL rejected: {}", sanitizeForLog(urlString));
                return false;
            }
            
            // Prevent access to internal/private networks
            if (isInternalHost(host)) {
                logger.warn("Internal/private host rejected: {}", sanitizeForLog(host));
                return false;
            }
            
            // Add domain whitelist if needed for Phone.email service
            // For now, just ensure it's a valid HTTPS URL with external host
            return host != null && !host.isEmpty();
        } catch (java.net.MalformedURLException e) {
            logger.warn("Malformed URL: {}", sanitizeForLog(urlString));
            return false;
        }
    }
    
    private boolean isInternalHost(String host) {
        // Check for localhost, private IP ranges, and internal domains
        return host.equals("localhost") ||
               host.equals("127.0.0.1") ||
               host.startsWith("192.168.") ||
               host.startsWith("10.") ||
               host.startsWith("172.16.") ||
               host.startsWith("172.17.") ||
               host.startsWith("172.18.") ||
               host.startsWith("172.19.") ||
               host.startsWith("172.2") ||
               host.startsWith("172.30.") ||
               host.startsWith("172.31.") ||
               host.equals("0.0.0.0") ||
               host.contains("internal") ||
               host.contains("local");
    }
    
    private String sanitizeForLog(String input) {
        if (input == null) {
            return "null";
        }
        // Remove potential log injection characters
        return input.replaceAll("[\r\n\t]", "_");
    }
}
