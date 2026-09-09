package com.hospital.hospital_billing_system.pharmacy.controller;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineResponseDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStatusRequestDto;
import com.hospital.hospital_billing_system.pharmacy.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;


    public ResponseEntity<MedicineResponseDto> createMedicine(
            @Valid @RequestBody MedicineRequestDto request
            ){
        log.info(
                "Received request to create medicine with code : {}",
                request.getMedicineCode()
        );

        MedicineResponseDto response=
                medicineService.createMedicine(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get medicine by ID.
    @GetMapping("/{medicineId}")
    public ResponseEntity<MedicineResponseDto> getMedicineById(
            @PathVariable Long medicineId) {

        log.debug(
                "Received request to fetch medicine with ID: {}",
                medicineId
        );

        MedicineResponseDto response =
                medicineService.getMedicineById(medicineId);

        return ResponseEntity.ok(response);
    }

//Get all medicines or search medicines.
    @GetMapping
    public ResponseEntity<List<MedicineResponseDto>> getMedicines(
            @RequestParam(required = false) String search) {

        log.debug(
                "Received request to fetch medicines. Search: {}",
                search
        );

        List<MedicineResponseDto> response;

        if (search == null || search.trim().isEmpty()) {
            response = medicineService.getAllMedicines();
        } else {
            response = medicineService.searchMedicines(search);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{medicineId}")
    public ResponseEntity<MedicineResponseDto> updateMedicine(
            @PathVariable Long medicineId,
            @Valid @RequestBody MedicineRequestDto request) {

        log.info(
                "Received request to update medicine with ID: {}",
                medicineId
        );

        MedicineResponseDto response =
                medicineService.updateMedicine(
                        medicineId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // Activate or deactivate a medicine.

    @PatchMapping("/{medicineId}/status")
    public ResponseEntity<MedicineResponseDto> updateMedicineStatus(
            @PathVariable Long medicineId,
            @Valid @RequestBody MedicineStatusRequestDto request) {

        log.info(
                "Received request to update medicine status. ID: {}, Active: {}",
                medicineId,
                request.getActive()
        );

        MedicineResponseDto response =
                medicineService.updateMedicineStatus(
                        medicineId,
                        request.getActive()
                );

        return ResponseEntity.ok(response);
    }

}
