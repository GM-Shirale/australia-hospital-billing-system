package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.dto.BillItemRequest;
import com.hospital.hospital_billing_system.billing.dto.BillItemResponse;
import com.hospital.hospital_billing_system.billing.service.BillItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-items")
@RequiredArgsConstructor
public class BillItemController {

    private final BillItemService billItemService;

    @PostMapping("/bill/{billId}")
    public ResponseEntity<BillItemResponse> createBillItem(
            @PathVariable Long billId,
            @RequestBody BillItemRequest request) {

        BillItemResponse response =
                billItemService.createBillItem(billId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{billItemId}")
    public ResponseEntity<BillItemResponse> getBillItemById(
            @PathVariable Long billItemId) {

        return ResponseEntity.ok(
                billItemService.getBillItemById(billItemId)
        );
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<BillItemResponse>> getBillItemsByBillId(
            @PathVariable Long billId) {

        return ResponseEntity.ok(
                billItemService.getBillItemsByBillId(billId)
        );
    }

    @PutMapping("/{billItemId}")
    public ResponseEntity<BillItemResponse> updateBillItem(
            @PathVariable Long billItemId,
            @RequestBody BillItemRequest request) {

        return ResponseEntity.ok(
                billItemService.updateBillItem(billItemId, request)
        );
    }

    @DeleteMapping("/{billItemId}")
    public ResponseEntity<Void> deleteBillItem(
            @PathVariable Long billItemId) {

        billItemService.deleteBillItem(billItemId);

        return ResponseEntity.noContent().build();
    }
}