package com.hospital.hospital_billing_system.admin.controller;

import com.hospital.hospital_billing_system.admin.dto.AdminLoginRequest;
import com.hospital.hospital_billing_system.admin.dto.LoginResponse;
import com.hospital.hospital_billing_system.admin.dto.UserLoginRequest;
import com.hospital.hospital_billing_system.admin.service.AuthenticationService;
import com.hospital.hospital_billing_system.config.TokenBlacklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthenticationController(
            AuthenticationService authenticationService,
            TokenBlacklistService tokenBlacklistService) {

        this.authenticationService = authenticationService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // Admin login
    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> adminLogin(
            @RequestBody AdminLoginRequest request) {

        return ResponseEntity.ok(
                authenticationService.adminLogin(request)
        );
    }

    // User login
    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> userLogin(
            @RequestBody UserLoginRequest request) {

        return ResponseEntity.ok(
                authenticationService.userLogin(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authHeader) {

        // Check whether Bearer token is present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body("Authorization token is required");
        }

        // Extract JWT from Bearer token
        String token = authHeader.substring(7);

        // Add token to blacklist
        tokenBlacklistService.blacklistToken(token);

        return ResponseEntity.ok("Logout successful");
    }
}