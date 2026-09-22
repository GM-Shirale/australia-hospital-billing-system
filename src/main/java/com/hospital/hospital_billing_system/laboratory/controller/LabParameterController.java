package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.laboratory.dto.LabParameterRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabParameterResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabParameterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/laboratory/parameters")
public class LabParameterController {

    private final LabParameterService labParameterService;

    @PostMapping
    public ResponseEntity<LabParameterResponseDTO> createParameter(
            @Valid @RequestBody LabParameterRequestDTO request
            ){
        LabParameterResponseDTO response=
                labParameterService.createParameter(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabParameterResponseDTO> getParameterById(@PathVariable Long id
    ){
        return ResponseEntity.ok(
                labParameterService.getParameterById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LabParameterResponseDTO>> getAllParameters(){
        return ResponseEntity.ok(
                labParameterService.getAllParameters()
        );
    }

    @GetMapping("/lab-test/{labTestId}")
    public ResponseEntity<List<LabParameterResponseDTO>> getParametersByLabTest(
            @PathVariable Long labTestId
    ){
        return ResponseEntity.ok(
                labParameterService.getParametersByLabTest(labTestId)
        );
    }


     // here we delete the record by soft delete parameters
    @PutMapping("/{id}")
    public ResponseEntity<LabParameterResponseDTO> updateParameter(
            @PathVariable Long id,
            @Valid @RequestBody LabParameterRequestDTO request){


        return ResponseEntity.ok(
                labParameterService.updateParameters(id,request)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParameter(
            @PathVariable Long id) {

        labParameterService.deleteParameters(id);

        return ResponseEntity.noContent().build();
    }



}
