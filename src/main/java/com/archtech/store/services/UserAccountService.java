package com.archtech.store.services;

import com.archtech.store.model.*;
import com.archtech.store.repository.*;
import com.archtech.store.dto.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserAccount> getAllUsers() {
        return this.repository.findAll();
    }

    public Optional<UserAccount> getUserById(Long id) {
        return this.repository.findById(id);
    }

    public UserAccount createUser(UserAccount user) {
        // Always hash the password before saving
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.repository.save(user);
    }

    public UserAccount updateEmail(Long id, String email) throws RuntimeException {
        return this.repository.findById(id)
                .map(user -> {
                    user.setEmail(email);
                    return this.repository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public UserAccount updatePassword(Long id, String newPassword) throws RuntimeException {
        return this.repository.findById(id)
                .map(user -> {
                    user.setPassword(this.passwordEncoder.encode(newPassword));
                    return this.repository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        this.repository.deleteById(id);
    }

    public boolean checkPassword(Long id, String rawPassword) {
        return this.repository.findById(id)
                .map(user -> this.passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    public boolean checkEmail(Long id, String email) {
        return this.repository.findById(id)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }

    // Authentication methods
    public UserAccount signup(SignupRequest signupRequest) {
        // Check if username already exists
        if (this.repository.existsByUserName(signupRequest.getUserName())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        // Check if email already exists
        if (this.repository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        
        // Create new user
        UserAccount user = new UserAccount(
            signupRequest.getName(),
            signupRequest.getUserName(),
            signupRequest.getEmail(),
            this.passwordEncoder.encode(signupRequest.getPassword())
        );
        
        return this.repository.save(user);
    }
    
    public Optional<UserAccount> signin(SigninRequest signinRequest) {
        // Try to find user by username first
        Optional<UserAccount> userOpt = this.repository.findByUserName(signinRequest.getUsernameOrEmail());
        
        // If not found by username, try email
        if (!userOpt.isPresent()) {
            userOpt = this.repository.findByEmail(signinRequest.getUsernameOrEmail());
        }
        
        // Validate password if user found
        if (userOpt.isPresent() && this.passwordEncoder.matches(signinRequest.getPassword(), userOpt.get().getPassword())) {
            return userOpt;
        }
        
        return Optional.empty();
    }
    
    public UserAccount changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        UserAccount user = this.repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        
        // Check if current password matches
        if (!this.passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect!");
        }
        
        // Update with new password
        user.setPassword(this.passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        return this.repository.save(user);
    }
    
    public Optional<UserAccount> findByUsername(String username) {
        return this.repository.findByUserName(username);
    }
    
    public Optional<UserAccount> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }
    
    public boolean existsByUsername(String username) {
        return this.repository.existsByUserName(username);
    }
    
    public boolean existsByEmail(String email) {
        return this.repository.existsByEmail(email);
    }
}
