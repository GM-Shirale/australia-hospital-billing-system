package com.hospital.hospital_billing_system.pharmacy.controller;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchResponseDto;
import com.hospital.hospital_billing_system.pharmacy.service.MedicineBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pharmacy/medicine-batches")
@RequiredArgsConstructor
public class MedicineBatchController {

    private final MedicineBatchService medicineBatchService;

    // Create medicine batch
    @PostMapping
    public ResponseEntity<MedicineBatchResponseDto> createMedicineBatch(
            @Valid @RequestBody MedicineBatchRequestDto request) {

        log.info(
                "Received request to create medicine batch. Medicine ID: {}, Batch Number: {}",
                request.getMedicineId(),
                request.getBatchNumber()
        );

        MedicineBatchResponseDto response =
                medicineBatchService.createMedicineBatch(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get medicine batch by ID
    @GetMapping("/{batchId}")
    public ResponseEntity<MedicineBatchResponseDto> getMedicineBatchById(
            @PathVariable Long batchId) {

        log.debug(
                "Received request to fetch medicine batch with ID: {}",
                batchId
        );

        MedicineBatchResponseDto response =
                medicineBatchService.getMedicineBatchById(batchId);

        return ResponseEntity.ok(response);
    }

    // Get all medicine batches
    @GetMapping
    public ResponseEntity<List<MedicineBatchResponseDto>> getAllMedicineBatches() {

        log.debug("Received request to fetch all medicine batches");

        List<MedicineBatchResponseDto> response =
                medicineBatchService.getAllMedicineBatches();

        return ResponseEntity.ok(response);
    }

    // Get all batches for a medicine
    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<MedicineBatchResponseDto>> getBatchesByMedicineId(
            @PathVariable Long medicineId) {

        log.debug(
                "Received request to fetch batches for medicine ID: {}",
                medicineId
        );

        List<MedicineBatchResponseDto> response =
                medicineBatchService.getBatchesByMedicineId(medicineId);

        return ResponseEntity.ok(response);
    }

    // Update medicine batch
    @PutMapping("/{batchId}")
    public ResponseEntity<MedicineBatchResponseDto> updateMedicineBatch(
            @PathVariable Long batchId,
            @Valid @RequestBody MedicineBatchRequestDto request) {

        log.info(
                "Received request to update medicine batch with ID: {}",
                batchId
        );

        MedicineBatchResponseDto response =
                medicineBatchService.updateMedicineBatch(
                        batchId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}