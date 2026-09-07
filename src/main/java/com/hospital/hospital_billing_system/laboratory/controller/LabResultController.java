package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.laboratory.dto.LabResultRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/laboratory/results")
public class LabResultController {


    private final LabResultService labResultService;

    @PostMapping
    public ResponseEntity<LabResultResponseDTO> createResult(
            @Valid @RequestBody LabResultRequestDTO request
            ){
        LabResultResponseDTO response=
                labResultService.createResult(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LabResultResponseDTO> getResultById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                labResultService.getResultById(id)
        );
    }


    @GetMapping
    public ResponseEntity<List<LabResultResponseDTO>> getAllResults(){

        return ResponseEntity.ok(
                labResultService.getAllResults()
        );
    }

    @GetMapping("/sample/{labSampleId}")
    public ResponseEntity<List<LabResultResponseDTO>>
       getResultsBySample(@PathVariable Long labSampleId){

        return ResponseEntity.ok(
                labResultService.getResultsByTest(labSampleId)
        );
    }

    @GetMapping("/test/{labTestId}")
    public ResponseEntity<List<LabResultResponseDTO>>
       getResultsByTest(@PathVariable Long labTestId){

        return ResponseEntity.ok(
                labResultService.getResultsByTest(labTestId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabResultResponseDTO> updatedResult(
            @PathVariable Long id,
            @Valid @RequestBody LabResultRequestDTO request
    ){

        return ResponseEntity.ok(labResultService.updateResult(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(
            @PathVariable Long id
    ){

        labResultService.deleteResult(id);

        return ResponseEntity.noContent().build();
    }
}
