package com.hospital.hospital_billing_system.laboratory.controller;

import com.hospital.hospital_billing_system.laboratory.dto.LabOrderItemRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabOrderItemResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabOrderItem;
import com.hospital.hospital_billing_system.laboratory.service.LabOrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory/order-items")
@RequiredArgsConstructor
public class LabOrderItemController {

    private final LabOrderItemService labOrderItemService;

    @PostMapping
    public ResponseEntity<LabOrderItemResponseDTO> createOrderItem(
            @Valid @RequestBody LabOrderItemRequestDTO request){

        LabOrderItemResponseDTO response=
                labOrderItemService.createOrderItem(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabOrderItemResponseDTO> getOrderItemById(
            @PathVariable Long id){
        return ResponseEntity.ok(
                labOrderItemService.getOrderItemById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<LabOrderItemResponseDTO>> getAllOrderItems(){
        return ResponseEntity.ok(
                labOrderItemService.getAllOrderItems()
        );
    }

    @GetMapping("/order/{labOrderId}")
    public ResponseEntity<List<LabOrderItemResponseDTO>> getOrderItemsByLabOrder(
            @PathVariable Long labOrderId
    ){
        return ResponseEntity.ok(
                labOrderItemService.getOrderItemsByLabOrder(labOrderId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabOrderItemResponseDTO> updateOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody LabOrderItemRequestDTO request){

        return ResponseEntity.ok(
                labOrderItemService.updateOrderItem(id, request)
        );
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable Long id ){
        labOrderItemService.deleteOrderItem(id);

        return ResponseEntity.noContent().build();
    }



}
