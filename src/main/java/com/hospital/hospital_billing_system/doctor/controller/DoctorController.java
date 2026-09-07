package com.hospital.hospital_billing_system.doctor.controller;

import com.hospital.hospital_billing_system.doctor.dto.DoctorConsultationChargeRequest;
import com.hospital.hospital_billing_system.doctor.dto.DoctorConsultationChargeResponse;
import com.hospital.hospital_billing_system.doctor.dto.DoctorRequest;
import com.hospital.hospital_billing_system.doctor.dto.DoctorResponse;
import com.hospital.hospital_billing_system.doctor.service.DoctorService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller managing doctor and medical provider operations.
 */
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // Register a doctor under the hospital tenant
    @PostMapping
    public ResponseEntity<DoctorResponse> registerDoctor(
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse response = doctorService.registerDoctor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Retrieve a doctor by ID scoped to tenant
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable UUID doctorId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        DoctorResponse response = doctorService.getDoctorById(doctorId, tenantId);
        return ResponseEntity.ok(response);
    }

    // Lookup doctor by Medicare Provider Number (Used by Billing & Claims modules)
    @GetMapping("/by-provider-number")
    public ResponseEntity<DoctorResponse> getDoctorByProviderNumber(
            @RequestParam("providerNo") String providerNo) {
        DoctorResponse response = doctorService.getDoctorByProviderNumber(providerNo);
        return ResponseEntity.ok(response);
    }

    // Fetch all doctors for a specific hospital tenant
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctorsByTenant(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        List<DoctorResponse> response = doctorService.getDoctorsByTenant(tenantId);
        return ResponseEntity.ok(response);
    }

    // Filter doctors by clinical department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByDepartment(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID departmentId) {
        List<DoctorResponse> response = doctorService.getDoctorsByDepartment(tenantId, departmentId);
        return ResponseEntity.ok(response);
    }

    // Update doctor details
    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable UUID doctorId,
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse response = doctorService.updateDoctor(doctorId, tenantId, request);
        return ResponseEntity.ok(response);
    }

    // Remove doctor record
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable UUID doctorId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        doctorService.deleteDoctor(doctorId, tenantId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint consumed by Admissions / Billing to verify doctor eligibility and calculate consultation charge
    @PostMapping("/consultation-charge")
    public ResponseEntity<DoctorConsultationChargeResponse> verifyConsultationCharge(
            @Valid @RequestBody DoctorConsultationChargeRequest request) {
        DoctorConsultationChargeResponse response = doctorService.verifyAndCalculateConsultationCharge(request);
        return ResponseEntity.ok(response);
    }
}