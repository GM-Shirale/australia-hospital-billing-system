package com.hospital.hospital_billing_system.admin.controller;

import com.hospital.hospital_billing_system.admin.dto.AdminRequest;
import com.hospital.hospital_billing_system.admin.dto.AdminResponse;
import com.hospital.hospital_billing_system.admin.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Create a new Admin
    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(
            @RequestBody AdminRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminService.createAdmin(request));
    }

    // Get Admin by ID
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminResponse> getAdminById(
            @PathVariable Long adminId) {

        return ResponseEntity.ok(
                adminService.getAdminById(adminId)
        );
    }

    // Update Admin
    @PutMapping("/{adminId}")
    public ResponseEntity<AdminResponse> updateAdmin(
            @PathVariable Long adminId,
            @RequestBody AdminRequest request) {

        return ResponseEntity.ok(
                adminService.updateAdmin(adminId, request)
        );
    }

    // Activate Admin
    @PutMapping("/{adminId}/activate")
    public ResponseEntity<AdminResponse> activateAdmin(
            @PathVariable Long adminId) {

        return ResponseEntity.ok(
                adminService.activateAdmin(adminId)
        );
    }

    // Deactivate Admin
    @PutMapping("/{adminId}/deactivate")
    public ResponseEntity<AdminResponse> deactivateAdmin(
            @PathVariable Long adminId) {

        return ResponseEntity.ok(
                adminService.deactivateAdmin(adminId)
        );
    }
}