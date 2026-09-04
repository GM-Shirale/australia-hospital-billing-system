package com.hospital.hospital_billing_system.patient.controller;


import com.hospital.hospital_billing_system.patient.dto.PatientAddressRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientAddressResponse;
import com.hospital.hospital_billing_system.patient.service.PatientAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientAddressController {

    private final PatientAddressService patientAddressService;

    // constructor injection
    public PatientAddressController(
            PatientAddressService patientAddressService) {
        this.patientAddressService = patientAddressService;
    }

    // create address for patient
    @PostMapping("/{patientId}/addresses")
    public ResponseEntity<PatientAddressResponse> createAddress(
            @PathVariable Long patientId,
            @RequestBody PatientAddressRequest request) {

        PatientAddressResponse response =
                patientAddressService.createAddress(patientId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // get address by id
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<PatientAddressResponse> getAddressById(@PathVariable Long addressId) {

        return ResponseEntity.ok(
                patientAddressService.getAddressById(addressId)
        );
    }

    // get all addresses of patient
    @GetMapping("/{patientId}/addresses")
    public ResponseEntity<List<PatientAddressResponse>> getAddressesByPatientId(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                patientAddressService.getAddressesByPatientId(patientId)
        );
    }

    // update patient address
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<PatientAddressResponse> updateAddress(
            @PathVariable Long addressId,
            @RequestBody PatientAddressRequest request) {

        return ResponseEntity.ok(
                patientAddressService.updateAddress(addressId, request)
        );
    }

    // delete patient address
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId) {

        patientAddressService.deleteAddress(addressId);

        return ResponseEntity.noContent().build();
    }
}
