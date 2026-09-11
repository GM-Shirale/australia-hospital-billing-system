package com.hospital.hospital_billing_system.pharmacy.controller;

import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionItemRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionItemResponseDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionResponseDto;
import com.hospital.hospital_billing_system.pharmacy.service.PrescriptionItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/prescription-items")
@RequiredArgsConstructor
public class PrescriptionItemController {

    private final PrescriptionItemService prescriptionItemService;

    @PostMapping
    public ResponseEntity<PrescriptionItemResponseDto> createPrescriptionItem(
            @Valid @RequestBody PrescriptionItemRequestDto requestDto
            ){

        PrescriptionItemResponseDto response=
                prescriptionItemService.createPrescriptionItem(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{prescriptionItemId}")
    public ResponseEntity<PrescriptionItemResponseDto> getPrescriptionItemById(
            @PathVariable
            @Positive(message = "Prescription item ID must be positive") Long prescriptionItemId
    ){
        return ResponseEntity.ok(
                prescriptionItemService.getPrescriptionItemById(prescriptionItemId)
        );
    }
    @GetMapping("/prescription/{prescriptionId}")
    public ResponseEntity<List<PrescriptionItemResponseDto>>
    getItemsByPrescriptionId(
            @PathVariable
            @Positive(message = "Prescription ID must be positive")
            Long prescriptionId) {

        return ResponseEntity.ok(
                prescriptionItemService.getItemsByPrescriptionId(
                        prescriptionId
                )
        );
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<PrescriptionItemResponseDto>>
    getItemsByMedicineId(
            @PathVariable
            @Positive(message = "Medicine ID must be positive")
            Long medicineId) {

        return ResponseEntity.ok(
                prescriptionItemService.getItemsByMedicineId(
                        medicineId
                )
        );
    }
    @PutMapping("/{prescriptionItemId}")
    public ResponseEntity<PrescriptionItemResponseDto>
    updatePrescriptionItem(
            @PathVariable
            @Positive(message = "Prescription item ID must be positive")
            Long prescriptionItemId,

            @Valid @RequestBody PrescriptionItemRequestDto requestDto) {

        return ResponseEntity.ok(
                prescriptionItemService.updatePrescriptionItem(
                        prescriptionItemId,
                        requestDto
                )
        );
    }

    @DeleteMapping("/{prescriptionItemId}")
    public ResponseEntity<Void> deletePrescriptionItem(
            @PathVariable
            @Positive(message = "Prescription item ID must be positive")
            Long prescriptionItemId) {

        prescriptionItemService.deletePrescriptionItem(
                prescriptionItemId
        );

        return ResponseEntity.noContent().build();
    }
}
