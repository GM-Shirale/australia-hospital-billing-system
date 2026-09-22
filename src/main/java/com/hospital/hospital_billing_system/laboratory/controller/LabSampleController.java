package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.laboratory.dto.LabSampleRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabSampleResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabSampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory/samples")
@RequiredArgsConstructor
public class LabSampleController {
    private final LabSampleService labSampleService;

    @PostMapping
    public ResponseEntity<LabSampleResponseDTO> createSample(
            @Valid @RequestBody LabSampleRequestDTO request) {

        LabSampleResponseDTO response =
                labSampleService.createSample(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabSampleResponseDTO> getSampleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                labSampleService.getSampleById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LabSampleResponseDTO>> getAllSamples() {

        return ResponseEntity.ok(
                labSampleService.getAllSamples()
        );
    }

    @GetMapping("/order/{labOrderId}")
    public ResponseEntity<List<LabSampleResponseDTO>>
    getSamplesByLabOrder(
            @PathVariable Long labOrderId) {

        return ResponseEntity.ok(
                labSampleService
                        .getSamplesByLabOrder(labOrderId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabSampleResponseDTO> updateSample(
            @PathVariable Long id,
            @Valid @RequestBody LabSampleRequestDTO request) {

        return ResponseEntity.ok(
                labSampleService.updateSample(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSample(
            @PathVariable Long id) {

        labSampleService.deleteSample(id);

        return ResponseEntity.noContent().build();
    }
}
