package com.hospital.hospital_billing_system.pharmacy.controller;

import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionResponseDto;
import com.hospital.hospital_billing_system.pharmacy.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pharmacy/prescriptions")
@RequiredArgsConstructor
@Slf4j
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponseDto> createPrescription(
            @Valid @RequestBody PrescriptionRequestDto request
            ){
        log.info("Received request to create prescription. Patient Id: {},Doctor ID{}"
        ,request.getPatientId(),request.getDoctorId());
        PrescriptionResponseDto response=
                prescriptionService.createPrescription(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponseDto> getPrescriptionById(
            @PathVariable Long prescriptionId
    ){

        log.debug( "Received request to fetch prescription with ID: {}",
                prescriptionId);

        PrescriptionResponseDto response=
                prescriptionService.getPrescriptionById(prescriptionId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDto>> getAllPrescriptions(){
        log.debug("Received request to fetch all prescriptions");

        List<PrescriptionResponseDto> response=
                prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByDoctorId(
            @PathVariable UUID doctorId
            ){
        log.debug("Received request to fetch prescriptions for doctor ID: {}",
                doctorId);

        List<PrescriptionResponseDto> response=
                prescriptionService.getPrescriptionsByDoctorId(doctorId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponseDto> updatePrescription(
            @PathVariable Long prescriptionId,
            @Valid @RequestBody PrescriptionRequestDto request) {

        log.info(
                "Received request to update prescription with ID: {}",
                prescriptionId
        );

        PrescriptionResponseDto response =
                prescriptionService.updatePrescription(
                        prescriptionId,
                        request
                );

        return ResponseEntity.ok(response);
    }

}
