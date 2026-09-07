package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.laboratory.dto.LabResultValueRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultValueResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabResultValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory/result-values")
@RequiredArgsConstructor
public class LabResultValueController {

    private final LabResultValueService labResultValueService;

    // Create result value
    @PostMapping
    public ResponseEntity<LabResultValueResponseDTO> createResultValue(
            @Valid @RequestBody LabResultValueRequestDTO request) {

        LabResultValueResponseDTO response =
                labResultValueService.createResultValue(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get result value by ID
    @GetMapping("/{id}")
    public ResponseEntity<LabResultValueResponseDTO> getResultValueById(
            @PathVariable Long id) {

        LabResultValueResponseDTO response =
                labResultValueService.getResultValueById(id);

        return ResponseEntity.ok(response);
    }

    // Get all result values
    @GetMapping
    public ResponseEntity<List<LabResultValueResponseDTO>> getAllResultValues() {

        List<LabResultValueResponseDTO> response =
                labResultValueService.getAllResultValues();

        return ResponseEntity.ok(response);
    }

    // Get result values by Lab Result
    @GetMapping("/result/{labResultId}")
    public ResponseEntity<List<LabResultValueResponseDTO>> getResultValuesByResult(
            @PathVariable Long labResultId) {

        List<LabResultValueResponseDTO> response =
                labResultValueService
                        .getResultValuesByResult(labResultId);

        return ResponseEntity.ok(response);
    }

    // Get result values by Lab Parameter
    @GetMapping("/parameter/{labParameterId}")
    public ResponseEntity<List<LabResultValueResponseDTO>> getResultValuesByParameter(
            @PathVariable Long labParameterId) {

        List<LabResultValueResponseDTO> response =
                labResultValueService
                        .getResultValuesByParameter(labParameterId);

        return ResponseEntity.ok(response);
    }

    // Update result value
    @PutMapping("/{id}")
    public ResponseEntity<LabResultValueResponseDTO> updateResultValue(
            @PathVariable Long id,
            @Valid @RequestBody LabResultValueRequestDTO request) {

        LabResultValueResponseDTO response =
                labResultValueService
                        .updateResultValue(id, request);

        return ResponseEntity.ok(response);
    }

    // Delete result value
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResultValue(
            @PathVariable Long id) {

        labResultValueService.deleteResultValue(id);

        return ResponseEntity.noContent().build();
    }
}