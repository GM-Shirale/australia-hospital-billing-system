package com.hospital.hospital_billing_system.admin.controller;

import com.hospital.hospital_billing_system.admin.dto.UserRequest;
import com.hospital.hospital_billing_system.admin.dto.UserResponse;
import com.hospital.hospital_billing_system.admin.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService adminService;

    public UserController(UserService adminService) {
        this.adminService = adminService;
    }

    // create user
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest request) {

        UserResponse response = adminService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // get user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                adminService.getUserById(userId));
    }

    // get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                adminService.getAllUsers());
    }

    // update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequest request) {

        return ResponseEntity.ok(
                adminService.updateUser(userId, request));
    }

    // activate user
    @PutMapping("/{userId}/activate")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                adminService.activateUser(userId));
    }

    // deactivate user
    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                adminService.deactivateUser(userId));
    }

    // delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId) {

        adminService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}