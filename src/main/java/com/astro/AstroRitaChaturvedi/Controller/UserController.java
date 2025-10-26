package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Security.JwtUtil;
import com.astro.AstroRitaChaturvedi.Service.UserService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            
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
            logger.info("User registered successfully: {}", sanitizeForLog(user.getUsername()));
            
            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
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
    @GetMapping("/details/{id}")
    public ResponseEntity<?> userdetails(@PathVariable UUID id) {
        try {
            UserModel user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid user ID provided: {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid user ID"));
        } catch (Exception e) {
            logger.error("Error retrieving user details for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve user details"));
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable UUID id, @RequestBody UserModel userUpdateRequest) {
        try {
            if (userUpdateRequest == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Update request body is required"));
            }
            
            UserModel updatedUser = userService.updateUserProfile(id, userUpdateRequest);
            logger.info("User profile updated successfully for ID: {}", id);
            return ResponseEntity.ok(updatedUser);
        } catch (UsernameNotFoundException e) {
            logger.warn("Attempt to update non-existent user: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid update request for user {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating user profile for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update user profile"));
        }
    }
    
    private String sanitizeForLog(String input) {
        if (input == null) {
            return "null";
        }
        // Remove potential log injection characters
        return input.replaceAll("[\r\n\t]", "_");
    }


}
