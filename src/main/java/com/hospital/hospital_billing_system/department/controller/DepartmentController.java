package com.hospital.hospital_billing_system.department.controller;

import com.hospital.hospital_billing_system.department.dto.DepartmentRequest;
import com.hospital.hospital_billing_system.department.dto.DepartmentResponse;
import com.hospital.hospital_billing_system.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing endpoints for hospital department management.
 * Enforces tenant-based access headers for multi-hospital isolation.
 */
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // Create a new clinical department under a hospital tenant
    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse response = departmentService.createDepartment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Retrieve all departments belonging to the authenticated hospital tenant
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        List<DepartmentResponse> response = departmentService.getDepartmentsByTenant(tenantId);
        return ResponseEntity.ok(response);
    }

    // Retrieve a single department by its ID ensuring it matches the tenant
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(
            @PathVariable UUID departmentId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        DepartmentResponse response = departmentService.getDepartmentById(departmentId, tenantId);
        return ResponseEntity.ok(response);
    }

    // Update department information
    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable UUID departmentId,
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse response = departmentService.updateDepartment(departmentId, tenantId, request);
        return ResponseEntity.ok(response);
    }

    // Remove a department record
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable UUID departmentId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        departmentService.deleteDepartment(departmentId, tenantId);
        return ResponseEntity.noContent().build();
    }
}