package com.hospital.hospital_billing_system.pharmacy.controller;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import com.hospital.hospital_billing_system.pharmacy.dto.PharmacyChargeRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PharmacyChargeResponseDto;
import com.hospital.hospital_billing_system.pharmacy.service.PharmacyChargeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/charges")
@RequiredArgsConstructor
@Validated
public class PharmacyChargeController {

    private final PharmacyChargeService pharmacyChargeService;

    @PostMapping
    public ResponseEntity<PharmacyChargeResponseDto> createPharmacyCharge(
            @Valid @RequestBody PharmacyChargeRequestDto requestDto) {

        PharmacyChargeResponseDto response =
                pharmacyChargeService.createPharmacyCharge(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{pharmacyChargeId}")
    public ResponseEntity<PharmacyChargeResponseDto> getById(
            @PathVariable
            @Positive(message = "Pharmacy charge ID must be positive")
            Long pharmacyChargeId) {

        return ResponseEntity.ok(
                pharmacyChargeService
                        .getPharmacyChargeById(pharmacyChargeId)
        );
    }

    @GetMapping("/number/{chargeNumber}")
    public ResponseEntity<PharmacyChargeResponseDto> getByChargeNumber(
            @PathVariable
            @Size(min = 1, max = 50,
                    message = "Charge number must be between 1 and 50 characters")
            String chargeNumber) {

        return ResponseEntity.ok(
                pharmacyChargeService
                        .getPharmacyChargeByChargeNumber(chargeNumber)
        );
    }

    @GetMapping("/dispensing/{dispensingId}")
    public ResponseEntity<PharmacyChargeResponseDto> getByDispensingId(
            @PathVariable
            @Positive(message = "Dispensing ID must be positive")
            Long dispensingId) {

        return ResponseEntity.ok(
                pharmacyChargeService
                        .getPharmacyChargeByDispensingId(dispensingId)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PharmacyChargeResponseDto>> getByPatientId(
            @PathVariable
            @Positive(message = "Patient ID must be positive")
            Long patientId) {

        return ResponseEntity.ok(
                pharmacyChargeService
                        .getPharmacyChargesByPatientId(patientId)
        );
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<PharmacyChargeResponseDto>> getByMedicineId(
            @PathVariable
            @Positive(message = "Medicine ID must be positive")
            Long medicineId) {

        return ResponseEntity.ok(
                pharmacyChargeService
                        .getPharmacyChargesByMedicineId(medicineId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PharmacyChargeResponseDto>> getByStatus(
            @PathVariable LabChargeStatus status) {

        return ResponseEntity.ok(
                pharmacyChargeService
                        .getPharmacyChargesByStatus(status)
        );
    }

    @GetMapping("/billing-type/{billingType}")
    public ResponseEntity<List<PharmacyChargeResponseDto>>
    getByBillingType(
            @PathVariable BillingType billingType) {

        return ResponseEntity.ok(
                pharmacyChargeService
                        .getPharmacyChargesByBillingType(billingType)
        );
    }
}