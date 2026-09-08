package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.common.enums.ReportStatus;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory/reports")
@RequiredArgsConstructor
public class LabReportController {

    private final LabReportService labReportService;

    // create lab report
    @PostMapping
    public ResponseEntity<LabReportResponseDTO> createReport(
            @Valid @RequestBody LabReportRequestDTO request
            ){

        LabReportResponseDTO response=
                labReportService.createReport(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LabReportResponseDTO> getReportById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                labReportService.getReportById(id)
        );
    }

    // Get report by report number
    @GetMapping("/number/{reportNumber}")
    public ResponseEntity<LabReportResponseDTO> getReportByReportNumber(
            @PathVariable String reportNumber
    ){
        return ResponseEntity.ok(
                labReportService.getReportByReportNumber(reportNumber));

    }

    // Get reports by lab order
    @GetMapping("/order/{labOrderId}")
    public ResponseEntity<List<LabReportResponseDTO>> getReportByLabOrderId(
            @PathVariable Long labOrderId
    ){
        return ResponseEntity.ok(
                labReportService.getReportsByLabOrderId(labOrderId));

    }

    // Get reports by patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabReportResponseDTO>> getReportsByPatientId(
            @PathVariable Long patientId
    ){

        return ResponseEntity.ok(
                labReportService.getReportsByPatientId(patientId));

    }


    // Get reports by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LabReportResponseDTO>> getReportsByStatus(
            @PathVariable ReportStatus status
            ){
        return ResponseEntity.ok(
                labReportService.getReportsByStatus(status)
        );
    }

    // Update report
    @PutMapping("/{id}")
    public ResponseEntity<LabReportResponseDTO> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody LabReportRequestDTO request) {

        return ResponseEntity.ok(
                labReportService.updateReport(id, request)
        );
    }

    // Delete report
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @PathVariable Long id) {

        labReportService.deleteReport(id);

        return ResponseEntity.noContent().build();
    }

}
