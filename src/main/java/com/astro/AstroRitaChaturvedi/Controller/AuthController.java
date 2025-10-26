package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Security.JwtUtil;
import com.astro.AstroRitaChaturvedi.Service.UserService;
import com.astro.AstroRitaChaturvedi.Service.PhoneEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PhoneEmailService phoneEmailService;
    
    public AuthController(UserService userService, AuthenticationManager authenticationManager, 
                         JwtUtil jwtUtil, PhoneEmailService phoneEmailService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.phoneEmailService = phoneEmailService;
    }

    // DTO for phone.email registration
    public static class PhoneEmailRegisterRequest extends UserModel {
        private String userJsonUrl;

        public String getUserJsonUrl() {
            return userJsonUrl;
        }

        public void setUserJsonUrl(String userJsonUrl) {
            this.userJsonUrl = userJsonUrl;
        }
    }

    @PostMapping("/register-with-phone-email")
    public ResponseEntity<?> registerWithPhoneEmail(@RequestBody PhoneEmailRegisterRequest request) {
        try {
            if (!isValidRequest(request)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid request parameters"));
            }

            if (!isValidUrl(request.getUserJsonUrl())) {
                logger.warn("Invalid URL provided for phone/email registration: {}", sanitizeForLog(request.getUserJsonUrl()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid URL provided"));
            }

            PhoneEmailService.PhoneEmailUserDetails phoneDetails = phoneEmailService.fetchUserDetails(request.getUserJsonUrl());

            if (phoneDetails == null || phoneDetails.fullPhoneNumber == null) {
                logger.warn("Failed to verify phone number via Phone.email service");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Could not verify phone number via Phone.email service."));
            }

            if (userService.userExistsByPhoneNumber(phoneDetails.fullPhoneNumber)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "This phone number is already registered. Please try logging in."));
            }

            if (userService.userExists(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Username already exists"));
            }

            UserModel userToRegister = createUserFromRequest(request, phoneDetails);
            UserModel registeredUser = userService.registerUserViaPhoneEmail(userToRegister);

            final UserDetails userDetails = userService.loadUserByUsername(registeredUser.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully with verified phone number.");
            response.put("token", jwt);
            response.put("role", registeredUser.getRole());
            response.put("userId", registeredUser.getCustomerId());

            logger.info("User registered successfully via phone/email: {}", sanitizeForLog(registeredUser.getUsername()));
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid registration request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error during phone/email registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed. Please try again."));
        }
    }

    @PostMapping("/login-with-phone-email")
    public ResponseEntity<?> loginWithPhoneEmail(@RequestBody Map<String, String> payload) {
        try {
            String userJsonUrl = payload.get("userJsonUrl");
            if (userJsonUrl == null || userJsonUrl.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "userJsonUrl is required"));
            }

            if (!isValidUrl(userJsonUrl)) {
                logger.warn("Invalid URL provided for phone/email login: {}", sanitizeForLog(userJsonUrl));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid URL provided"));
            }

            PhoneEmailService.PhoneEmailUserDetails phoneDetails = phoneEmailService.fetchUserDetails(userJsonUrl);

            if (phoneDetails == null || phoneDetails.fullPhoneNumber == null) {
                logger.warn("Failed to verify phone number via Phone.email service");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Could not verify phone number via Phone.email service."));
            }

            UserModel user = userService.findUserByPhoneNumber(phoneDetails.fullPhoneNumber);
            if (user == null || !user.isVerified()) {
                logger.warn("Login attempt with unregistered or unverified phone number");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User with this phone number not found or not verified. Please register."));
            }

            final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("role", user.getRole());
            response.put("userId", user.getCustomerId());
            response.put("message", "Login successful.");

            logger.info("Successful phone/email login for user: {}", sanitizeForLog(user.getUsername()));
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            logger.warn("Login attempt with non-existent user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User with this phone number not found. Please register."));
        } catch (Exception e) {
            logger.error("Error during phone/email login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed. Please try again."));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel loginRequest) {
        try {
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username and password are required"));
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("role", ((UserModel) userDetails).getRole());
            response.put("userId", ((UserModel) userDetails).getCustomerId());

            logger.info("Successful login for user: {}", sanitizeForLog(loginRequest.getUsername()));
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for user: {}", sanitizeForLog(loginRequest.getUsername()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        } catch (UsernameNotFoundException e) {
            logger.warn("Login attempt with non-existent user: {}", sanitizeForLog(loginRequest.getUsername()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
        } catch (Exception e) {
            logger.error("Error during login for user: {}", sanitizeForLog(loginRequest.getUsername()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed. Please try again."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody UserModel user) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username is required"));
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Password is required"));
            }

            if (userService.userExists(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "User already exists"));
            }

            UserModel registeredUser = userService.registerUser(user);
            logger.info("User registration initiated for: {}", sanitizeForLog(user.getUsername()));
            
            return ResponseEntity.ok(Map.of(
                    "message", "User registration initiated. Please verify with OTP sent to your phone (if provided and old flow used).",
                    "userId", registeredUser.getCustomerId()
            ));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid registration request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed. Please try again."));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String otp = request.get("otp");

            if (username == null || otp == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username and OTP are required"));
            }

            if (userService.verifyOtp(username, otp)) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                final String jwt = jwtUtil.generateToken(userDetails);

                Map<String, Object> response = new HashMap<>();
                response.put("token", jwt);
                response.put("role", ((UserModel) userDetails).getRole());
                response.put("userId", ((UserModel) userDetails).getCustomerId());

                logger.info("Successful OTP verification for user: {}", sanitizeForLog(username));
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Invalid OTP attempt for user: {}", sanitizeForLog(username));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid OTP"));
            }
        } catch (UsernameNotFoundException e) {
            logger.warn("OTP verification attempt for non-existent user: {}", sanitizeForLog(request.get("username")));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "User not found"));
        } catch (Exception e) {
            logger.error("Error during OTP verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Verification failed. Please try again."));
        }
    }

    // ... other existing methods (register-admin, test, etc.)
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(Map.of("message", "Auth controller is working"));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserModel user) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username is required"));
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Password is required"));
            }

            if (userService.userExists(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "User already exists"));
            }

            UserModel registeredAdmin = userService.createAdminUser(user);
            final UserDetails userDetails = userService.loadUserByUsername(registeredAdmin.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin user created successfully");
            response.put("userId", registeredAdmin.getCustomerId());
            response.put("token", jwt);
            response.put("role", registeredAdmin.getRole());

            logger.info("Admin user created successfully: {}", sanitizeForLog(registeredAdmin.getUsername()));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid admin registration request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error during admin registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Admin registration failed. Please try again."));
        }
    }
    
    private boolean isValidRequest(PhoneEmailRegisterRequest request) {
        return request.getUserJsonUrl() != null && !request.getUserJsonUrl().trim().isEmpty() &&
               request.getUsername() != null && !request.getUsername().trim().isEmpty() &&
               request.getPassword() != null && !request.getPassword().trim().isEmpty();
    }
    
    private boolean isValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol().toLowerCase();
            String host = url.getHost().toLowerCase();
            
            // Only allow HTTPS URLs from trusted domains
            if (!"https".equals(protocol)) {
                return false;
            }
            
            // Add domain whitelist validation if needed
            // For now, just ensure it's a valid HTTPS URL
            return host != null && !host.isEmpty();
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
    private UserModel createUserFromRequest(PhoneEmailRegisterRequest request, PhoneEmailService.PhoneEmailUserDetails phoneDetails) {
        UserModel userToRegister = new UserModel();
        userToRegister.setUsername(request.getUsername());
        userToRegister.setPassword(request.getPassword());
        userToRegister.setEmail(request.getEmail());
        
        String fullName = request.getFullName();
        if (fullName == null || fullName.trim().isEmpty()) {
            String firstName = Objects.toString(phoneDetails.firstName, "");
            String lastName = Objects.toString(phoneDetails.lastName, "");
            fullName = (firstName + " " + lastName).trim();
        }
        userToRegister.setFullName(fullName);
        userToRegister.setPhoneNumber(phoneDetails.fullPhoneNumber);
        userToRegister.setDateOfBirth(request.getDateOfBirth());
        
        return userToRegister;
    }
    
    private String sanitizeForLog(String input) {
        if (input == null) {
            return "null";
        }
        // Remove potential log injection characters
        return input.replaceAll("[\r\n\t]", "_");
    }
}
