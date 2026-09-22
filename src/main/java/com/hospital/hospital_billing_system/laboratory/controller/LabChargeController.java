package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import com.hospital.hospital_billing_system.laboratory.dto.LabChargeRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabChargeResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/laboratory/charges")
@RequiredArgsConstructor
public class LabChargeController {

    private final LabChargeService labChargeService;



    // Create Charge
    @PostMapping
    public ResponseEntity<LabChargeResponseDTO> createCharge(
            @Valid @RequestBody LabChargeRequestDTO request) {

        LabChargeResponseDTO response =
                labChargeService.createCharge(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // Get Charge By ID
    @GetMapping("/{id}")
    public ResponseEntity<LabChargeResponseDTO> getChargeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                labChargeService.getChargeById(id)
        );
    }


    // Get Charge By Charge Number
    @GetMapping("/number/{chargeNumber}")
    public ResponseEntity<LabChargeResponseDTO>
    getChargeByChargeNumber(
            @PathVariable String chargeNumber) {

        return ResponseEntity.ok(
                labChargeService
                        .getChargeByChargeNumber(chargeNumber)
        );
    }


    // Get Charges By Lab Order Item
    @GetMapping("/order-item/{labOrderItemId}")
    public ResponseEntity<List<LabChargeResponseDTO>>
    getChargesByLabOrderItemId(
            @PathVariable Long labOrderItemId) {

        return ResponseEntity.ok(
                labChargeService
                        .getChargesByLabOrderItemId(
                                labOrderItemId
                        )
        );
    }


    // Get Charges By Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LabChargeResponseDTO>>
    getChargesByStatus(
            @PathVariable LabChargeStatus status) {

        return ResponseEntity.ok(
                labChargeService
                        .getChargesByStatus(status)
        );
    }


    // Get Charges By Billing Type
    @GetMapping("/billing-type/{billingType}")
    public ResponseEntity<List<LabChargeResponseDTO>>
    getChargesByBillingType(
            @PathVariable BillingType billingType) {

        return ResponseEntity.ok(
                labChargeService
                        .getChargesByBillingType(billingType)
        );
    }


    // Total Provider Charge

    @GetMapping("/order/{labOrderId}/total-provider-charge")
    public ResponseEntity<BigDecimal>
    getTotalProviderChargeByLabOrderId(
            @PathVariable Long labOrderId) {

        return ResponseEntity.ok(
                labChargeService
                        .getTotalProviderChargeByLabOrderId(
                                labOrderId
                        )
        );
    }


    // Total Medicare Benefit

    @GetMapping("/order/{labOrderId}/total-medicare-benefit")
    public ResponseEntity<BigDecimal>
    getTotalMedicareBenefitByLabOrderId(
            @PathVariable Long labOrderId) {

        return ResponseEntity.ok(
                labChargeService
                        .getTotalMedicareBenefitByLabOrderId(
                                labOrderId
                        )
        );
    }


    // Total Patient Amount
    @GetMapping("/order/{labOrderId}/total-patient-amount")
    public ResponseEntity<BigDecimal>
    getTotalPatientAmountByLabOrderId(
            @PathVariable Long labOrderId) {

        return ResponseEntity.ok(
                labChargeService
                        .getTotalPatientAmountByLabOrderId(
                                labOrderId
                        )
        );
    }


    // Update Charge

    @PutMapping("/{id}")
    public ResponseEntity<LabChargeResponseDTO> updateCharge(
            @PathVariable Long id,
            @Valid @RequestBody LabChargeRequestDTO request) {

        return ResponseEntity.ok(
                labChargeService.updateCharge(id, request)
        );
    }


    // Delete Charge
        @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharge(
            @PathVariable Long id) {

        labChargeService.deleteCharge(id);

        return ResponseEntity.noContent().build();
    }
}