package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.dto.BillRequest;
import com.hospital.hospital_billing_system.billing.dto.BillResponse;
import com.hospital.hospital_billing_system.billing.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    // constructor injection
    public BillController(BillService billService) {
        this.billService = billService;
    }

    // create bill for patient
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<BillResponse> createBill(
            @PathVariable Long patientId,
            @RequestBody BillRequest request) {

        BillResponse response =
                billService.createBill(patientId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // get bill by id
    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBillById(
            @PathVariable Long billId) {

        return ResponseEntity.ok(
                billService.getBillById(billId)
        );
    }

    // get all bills
    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills() {

        return ResponseEntity.ok(
                billService.getAllBills()
        );
    }

    // get all bills of a patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillResponse>> getBillsByPatientId(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                billService.getBillsByPatientId(patientId)
        );
    }

    // update bill
    @PutMapping("/{billId}")
    public ResponseEntity<BillResponse> updateBill(
            @PathVariable Long billId,
            @RequestBody BillRequest request) {

        return ResponseEntity.ok(
                billService.updateBill(
                        billId,
                        request
                )
        );
    }

    // delete bill
    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> deleteBill(
            @PathVariable Long billId) {

        billService.deleteBill(billId);

        return ResponseEntity.noContent().build();
    }
}