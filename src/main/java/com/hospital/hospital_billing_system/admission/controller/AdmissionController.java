package com.hospital.hospital_billing_system.admission.controller;

import com.hospital.hospital_billing_system.admission.dto.AdmissionRequest;
import com.hospital.hospital_billing_system.admission.dto.AdmissionResponse;
import com.hospital.hospital_billing_system.admission.service.AdmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    // create admission for patient
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<AdmissionResponse> createAdmission(
            @PathVariable Long patientId,
            @RequestBody AdmissionRequest request) {

        AdmissionResponse response =
                admissionService.createAdmission(patientId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // get admission by id
    @GetMapping("/{admissionId}")
    public ResponseEntity<AdmissionResponse> getAdmissionById(
            @PathVariable Long admissionId) {

        return ResponseEntity.ok(
                admissionService.getAdmissionById(admissionId));
    }

    // get all admissions
    @GetMapping
    public ResponseEntity<List<AdmissionResponse>> getAllAdmissions() {

        return ResponseEntity.ok(
                admissionService.getAllAdmissions());
    }

    // get all admissions of a patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AdmissionResponse>> getAdmissionsByPatientId(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                admissionService.getAdmissionsByPatientId(patientId));
    }

    // update admission
    @PutMapping("/{admissionId}")
    public ResponseEntity<AdmissionResponse> updateAdmission(
            @PathVariable Long admissionId,
            @RequestBody AdmissionRequest request) {

        return ResponseEntity.ok(
                admissionService.updateAdmission(admissionId, request));
    }

    // discharge admission
    @PutMapping("/{admissionId}/discharge")
    public ResponseEntity<AdmissionResponse> dischargeAdmission(
            @PathVariable Long admissionId) {

        return ResponseEntity.ok(
                admissionService.dischargeAdmission(admissionId));
    }

    // delete admission
    @DeleteMapping("/{admissionId}")
    public ResponseEntity<Void> deleteAdmission(
            @PathVariable Long admissionId) {

        admissionService.deleteAdmission(admissionId);

        return ResponseEntity.noContent().build();
    }
}