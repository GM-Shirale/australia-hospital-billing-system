package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import com.hospital.hospital_billing_system.laboratory.dto.VerificationRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.VerificationResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.VerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory/verifications")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    // Create verification
    @PostMapping
    public ResponseEntity<VerificationResponseDTO> createVerification(
            @Valid @RequestBody VerificationRequestDTO request) {

        VerificationResponseDTO response =
                verificationService.createVerification(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get verification by ID
    @GetMapping("/{id}")
    public ResponseEntity<VerificationResponseDTO> getVerificationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                verificationService.getVerificationById(id)
        );
    }

    // Get verification by lab result
    @GetMapping("/result/{labResultId}")
    public ResponseEntity<VerificationResponseDTO>
    getVerificationByLabResultId(
            @PathVariable Long labResultId) {

        return ResponseEntity.ok(
                verificationService
                        .getVerificationByLabResultId(labResultId)
        );
    }

    // Get verifications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VerificationResponseDTO>>
    getVerificationsByStatus(
            @PathVariable VerificationStatus status) {

        return ResponseEntity.ok(
                verificationService
                        .getVerificationsByStatus(status)
        );
    }

    // Get verifications by staff member
    @GetMapping("/verified-by/{verifiedBy}")
    public ResponseEntity<List<VerificationResponseDTO>>
    getVerificationsByVerifiedBy(
            @PathVariable Long verifiedBy) {

        return ResponseEntity.ok(
                verificationService
                        .getVerificationsByVerifiedBy(verifiedBy)
        );
    }

    // Update verification
    @PutMapping("/{id}")
    public ResponseEntity<VerificationResponseDTO> updateVerification(
            @PathVariable Long id,
            @Valid @RequestBody VerificationRequestDTO request) {

        return ResponseEntity.ok(
                verificationService
                        .updateVerification(id, request)
        );
    }

    // Delete verification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVerification(
            @PathVariable Long id) {

        verificationService.deleteVerification(id);

        return ResponseEntity.noContent().build();
    }
}