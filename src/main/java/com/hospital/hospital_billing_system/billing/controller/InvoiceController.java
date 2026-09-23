package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.dto.InvoiceRequest;
import com.hospital.hospital_billing_system.billing.dto.InvoiceResponse;
import com.hospital.hospital_billing_system.billing.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(
            @RequestBody InvoiceRequest request) {

        InvoiceResponse response = invoiceService.createInvoice(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(
            @PathVariable Long invoiceId) {

        return ResponseEntity.ok(
                invoiceService.getInvoiceById(invoiceId)
        );
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<InvoiceResponse> getInvoiceByBillId(
            @PathVariable Long billId) {

        return ResponseEntity.ok(
                invoiceService.getInvoiceByBillId(billId)
        );
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {

        return ResponseEntity.ok(
                invoiceService.getAllInvoices()
        );
    }
}