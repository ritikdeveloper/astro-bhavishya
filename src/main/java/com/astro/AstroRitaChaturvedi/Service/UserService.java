package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Model.WalletModel;
import com.astro.AstroRitaChaturvedi.Repository.UserRepository;
import com.astro.AstroRitaChaturvedi.Repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository; // Keep for wallet creation

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false) // Make OtpService optional if it's being phased out
    private OtpService otpService;

    @Transactional
    public UserModel registerUserViaPhoneEmail(UserModel user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (user.getPhoneNumber() != null && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Phone number already registered");
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(true); // Phone.email has verified the phone number
        user.setRole(UserModel.UserRole.USER); // Set default role

        // Clear OTP fields if they were part of UserModel from request, as they are not needed
        user.setOtp(null);
        user.setOtpExpiry(null);

        WalletModel wallet = new WalletModel();
        wallet.setBalance(0.0);
        wallet.setUser(user); // Establish bidirectional relationship
        user.setWallet(wallet); // Wallet will be cascaded

        return userRepository.save(user);
    }

    public UserModel findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
    }

    public boolean userExistsByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    @Transactional
    public UserModel registerUser(UserModel user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Phone number already registered");
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserModel.UserRole.USER);

        // --- OTP Logic for old flow ---
        if (otpService != null && user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            String otp = otpService.generateOtp();
            user.setOtp(otp);
            user.setOtpExpiry(LocalDate.now().plusDays(1)); // OTP valid for 1 day
            user.setVerified(false); // Requires OTP verification for this flow
            // otpService.sendOtp(user.getPhoneNumber(), otp); // Send OTP
        } else {
            // If no phone or no OTP service, mark as verified or handle as per requirements
            // For now, if no phone, they can't use OTP flow. If phone.email is main, this is less critical.
            user.setVerified(true); // Or false if email verification is next
        }
        // --- End OTP Logic ---


        WalletModel wallet = new WalletModel();
        wallet.setBalance(0.0);
        wallet.setUser(user);
        user.setWallet(wallet);

        return userRepository.save(user);
    }

    @Transactional
    public boolean verifyOtp(String username, String otp) {
        Optional<UserModel> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            if (user.getOtp() != null && user.getOtp().equals(otp) &&
                    user.getOtpExpiry() != null && LocalDate.now().isBefore(user.getOtpExpiry())) {
                user.setVerified(true);
                user.setOtp(null); // Clear OTP after verification
                user.setOtpExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserModel getUserById(UUID id) {
        Optional<UserModel> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public UserModel getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserModel updateUserProfile(UUID id, UserModel userUpdateRequest) {
        UserModel existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        if (userUpdateRequest.getFullName() != null) {
            existingUser.setFullName(userUpdateRequest.getFullName());
        }

        if (userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.findByEmail(userUpdateRequest.getEmail()).filter(u -> !u.getCustomerId().equals(id)).isPresent()) {
                throw new RuntimeException("Email already registered by another user.");
            }
            existingUser.setEmail(userUpdateRequest.getEmail());
        }

        // Phone number update should ideally re-verify. For now, basic update.
        // If phone.email is used for login, changing phone number here needs careful consideration.
        if (userUpdateRequest.getPhoneNumber() != null && !userUpdateRequest.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            if (userRepository.findByPhoneNumber(userUpdateRequest.getPhoneNumber()).filter(u -> !u.getCustomerId().equals(id)).isPresent()) {
                throw new RuntimeException("Phone number already registered by another user.");
            }
            existingUser.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            // existingUser.setVerified(false); // Consider re-verification if phone changes
        }

        if (userUpdateRequest.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userUpdateRequest.getDateOfBirth());
        } else {
            existingUser.setDateOfBirth(null);
        }
        return userRepository.save(existingUser);
    }

    @Transactional
    public UserModel createAdminUser(UserModel user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        user.setRole(UserModel.UserRole.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(true);
        WalletModel wallet = new WalletModel();
        wallet.setBalance(0.0);
        wallet.setUser(user);
        user.setWallet(wallet);
        return userRepository.save(user);
    }
}
