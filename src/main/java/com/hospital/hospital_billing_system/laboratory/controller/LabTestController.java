package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.laboratory.dto.LabTestRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabTestResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory/tests")
@RequiredArgsConstructor
public class LabTestController {
    private final LabTestService labTestService;

    @PostMapping
    public ResponseEntity<LabTestResponseDTO> createLabTest(
            @Valid @RequestBody LabTestRequestDTO request){

        LabTestResponseDTO response=
                labTestService.createLabTest(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTestResponseDTO> getLabTestById(
            @PathVariable Long id){
        return ResponseEntity.ok(
                labTestService.getLabTestById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LabTestResponseDTO>> getAllLabTests(){

        return ResponseEntity.ok(
                labTestService.getAllLabTests()
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<LabTestResponseDTO> updateLanTest(
            @PathVariable Long id,
            @Valid @RequestBody LabTestRequestDTO requestDTO){


        return ResponseEntity.ok(
                labTestService.updateLabTest(id,requestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabTest(
            @PathVariable Long id){

        labTestService.deleteLabTest(id);
        return ResponseEntity.noContent().build();
    }

}
