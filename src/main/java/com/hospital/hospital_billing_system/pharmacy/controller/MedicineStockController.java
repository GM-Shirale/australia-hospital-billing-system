package com.hospital.hospital_billing_system.pharmacy.controller;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStockRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStockResponseDto;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineStockRepository;
import com.hospital.hospital_billing_system.pharmacy.service.MedicineStockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/stocks")
@RequiredArgsConstructor
@Validated
public class MedicineStockController {

    private final MedicineStockService medicineStockService;

    // Create new medicine stock
    @PostMapping
    public ResponseEntity<MedicineStockResponseDto> createStock(@Valid @RequestBody MedicineStockRequestDto request){
        MedicineStockResponseDto response=medicineStockService.createStock(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    // Get stock by ID
    @GetMapping("/{stockId}")
    public ResponseEntity<MedicineStockResponseDto> getStockById(
            @PathVariable@Positive(message ="Stock ID must be positive" )
            Long stockId
    ){
        return ResponseEntity.ok(
                medicineStockService.getStockById(stockId)
        );
    }

    // Get stock by medicine ID
    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<MedicineStockResponseDto>> getStockByMedicineId(
            @PathVariable
            @Positive(message = "Medicine ID must be positive")
            Long medicineId
    ){

        return ResponseEntity.ok(
                medicineStockService.getStockByMedicineId(medicineId)
        );
    }

    // Get stock by batch ID
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<MedicineStockResponseDto>> getStockByBatchId(
            @PathVariable
            @Positive(message = "Batch ID must be positive")
            Long batchId) {

        return ResponseEntity.ok(
                medicineStockService.getStockByBatchId(batchId)
        );
    }

    // Get all active stock
    @GetMapping("/active")
    public ResponseEntity<List<MedicineStockResponseDto>> getActiveStock() {

        return ResponseEntity.ok(
                medicineStockService.getActiveStock()
        );
    }

    // Get low stock
    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicineStockResponseDto>> getLowStock() {

        return ResponseEntity.ok(
                medicineStockService.getLowStock()
        );
    }

    // Update stock
    @PutMapping("/{stockId}")
    public ResponseEntity<MedicineStockResponseDto> updateStock(
            @PathVariable
            @Positive(message = "Stock ID must be positive")
            Long stockId,

            @Valid @RequestBody MedicineStockRequestDto requestDto) {

        return ResponseEntity.ok(
                medicineStockService.updateStock(
                        stockId,
                        requestDto
                )
        );
    }

    // Deactivate stock
    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(
            @PathVariable
            @Positive(message = "Stock ID must be positive")
            Long stockId) {

        medicineStockService.deleteStock(stockId);

        return ResponseEntity.noContent().build();
    }


}
