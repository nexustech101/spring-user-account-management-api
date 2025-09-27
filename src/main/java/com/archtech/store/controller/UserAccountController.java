package com.archtech.store.controller;

import com.archtech.store.model.*;
import com.archtech.store.services.*;
import com.archtech.store.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final com.archtech.store.services.UserAccountService service;

    public UserAccountController(UserAccountService service) {
        this.service = service;
    }

    // Create user account
    @PostMapping
    public ResponseEntity<UserAccount> createUser(@RequestBody UserAccount user) {
        UserAccount created = service.createUser(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get all user
    @GetMapping
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long id) {
        return service.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update email by id
    @PutMapping("/{id}/email")
    public ResponseEntity<UserAccount> updateEmail(
        @PathVariable Long id,
        @RequestParam String email
    ) throws RuntimeException {
        try {
            UserAccount updated = service.updateEmail(id, email);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update user password
    @PutMapping("/{id}/password")
    public ResponseEntity<UserAccount> updatePassword(
        @PathVariable Long id,
        @RequestParam String password
    ) throws RuntimeException {
        try {
            UserAccount updated = service.updatePassword(id, password);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete user account
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Check user password
    @PostMapping("/{id}/check-password")
    public ResponseEntity<Boolean> checkPassword(@PathVariable Long id, @RequestParam String password) {
        boolean matches = service.checkPassword(id, password);
        return ResponseEntity.ok(matches);
    }

    // Check user email
    @PostMapping("/{id}/check-email")
    public ResponseEntity<Boolean> checkEmail(@PathVariable Long id, @RequestParam String email) {
        boolean matches = service.checkEmail(id, email);
        return ResponseEntity.ok(matches);
    }
    
    // Authentication endpoints
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            UserAccount user = service.signup(signupRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signin(@Valid @RequestBody SigninRequest signinRequest) {
        Optional<UserAccount> userOpt = service.signin(signinRequest);
        
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", userOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid username/email or password"));
        }
    }
    
    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            UserAccount user = service.changePassword(id, changePasswordRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    @GetMapping("/check-username/{username}")
    public ResponseEntity<ApiResponse> checkUsernameAvailability(@PathVariable String username) {
        boolean isAvailable = !service.existsByUsername(username);
        String message = isAvailable ? "Username is available" : "Username is already taken";
        return ResponseEntity.ok(new ApiResponse(isAvailable, message));
    }
    
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse> checkEmailAvailability(@PathVariable String email) {
        boolean isAvailable = !service.existsByEmail(email);
        String message = isAvailable ? "Email is available" : "Email is already in use";
        return ResponseEntity.ok(new ApiResponse(isAvailable, message));
    }
}
