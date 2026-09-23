package com.hospital.hospital_billing_system.pharmacy.controller;

import com.hospital.hospital_billing_system.pharmacy.dto.DispensingRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.DispensingResponseDto;
import com.hospital.hospital_billing_system.pharmacy.service.DispensingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/dispensing")
@RequiredArgsConstructor
@Validated
public class DispensingController {

    private final DispensingService dispensingService;

    @PostMapping
    public ResponseEntity<DispensingResponseDto> createDispensing(
            @Valid @RequestBody DispensingRequestDto requestDto) {

        DispensingResponseDto response =
                dispensingService.createDispensing(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{dispensingId}")
    public ResponseEntity<DispensingResponseDto> getDispensingById(
            @PathVariable
            @Positive(message = "Dispensing ID must be positive")
            Long dispensingId) {

        return ResponseEntity.ok(
                dispensingService.getDispensingById(dispensingId)
        );
    }

    @GetMapping("/prescription-item/{prescriptionItemId}")
    public ResponseEntity<List<DispensingResponseDto>>
    getDispensingByPrescriptionItemId(
            @PathVariable
            @Positive(message = "Prescription item ID must be positive")
            Long prescriptionItemId) {

        return ResponseEntity.ok(
                dispensingService
                        .getDispensingByPrescriptionItemId(
                                prescriptionItemId
                        )
        );
    }

    @GetMapping("/stock/{stockId}")
    public ResponseEntity<List<DispensingResponseDto>>
    getDispensingByStockId(
            @PathVariable
            @Positive(message = "Stock ID must be positive")
            Long stockId) {

        return ResponseEntity.ok(
                dispensingService.getDispensingByStockId(stockId)
        );
    }

    @GetMapping("/prescription/{prescriptionId}")
    public ResponseEntity<List<DispensingResponseDto>>
    getDispensingByPrescriptionId(
            @PathVariable
            @Positive(message = "Prescription ID must be positive")
            Long prescriptionId) {

        return ResponseEntity.ok(
                dispensingService
                        .getDispensingByPrescriptionId(
                                prescriptionId
                        )
        );
    }
}