package com.hospital.hospital_billing_system.patient.controller;

import com.hospital.hospital_billing_system.patient.dto.PatientRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientResponse;
import com.hospital.hospital_billing_system.patient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    // constructor injection
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // create new patient
    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {

        PatientResponse response = patientService.createPatient(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // get patient by id
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long patientId) {

        PatientResponse response =
                patientService.getPatientById(patientId);

        return ResponseEntity.ok(response);
    }

    // get all patients
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {

        List<PatientResponse> response =
                patientService.getAllPatients();

        return ResponseEntity.ok(response);
    }

    // update patient
    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientRequest request) {

        PatientResponse response =
                patientService.updatePatient(patientId, request);

        return ResponseEntity.ok(response);
    }

    // delete patient
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long patientId) {

        patientService.deletePatient(patientId);

        return ResponseEntity.noContent().build();
    }
}