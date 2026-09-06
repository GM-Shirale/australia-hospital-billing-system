package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.laboratory.dto.LabOrderRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabOrderResponseDTO;
import com.hospital.hospital_billing_system.laboratory.service.LabOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/laboratory/order")
@RestController
public class LabOrderController {
    private final LabOrderService labOrderService;

    @PostMapping
    public ResponseEntity<LabOrderResponseDTO> createLabOrder(
            @Valid @RequestBody LabOrderRequestDTO request){

        LabOrderResponseDTO response=
                labOrderService.createLabOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LabOrderResponseDTO> getLabOrderById(
            @PathVariable Long id){

        return ResponseEntity.ok(
                labOrderService.getLabOrderById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabOrderResponseDTO> updateLabOrder(
            @PathVariable Long id,
            @Valid @RequestBody LabOrderRequestDTO request
    ){
        return ResponseEntity.ok(
                labOrderService.updateLabOrder(id, request)
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLabOrder(
            @PathVariable Long id
    ){
        labOrderService.deleteLabOrder(id);

        return ResponseEntity.noContent().build();
    }



    @GetMapping
    public ResponseEntity<List<LabOrderResponseDTO>> getAllLabOrders() {

        return ResponseEntity.ok(
                labOrderService.getAllLabOrders()
        );
    }


}
