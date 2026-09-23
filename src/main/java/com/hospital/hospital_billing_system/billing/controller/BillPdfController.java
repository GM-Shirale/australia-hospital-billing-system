package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.service.BillPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillPdfController {

    private final BillPdfService billPdfService;

    @GetMapping("/{billId}/pdf")
    public ResponseEntity<byte[]> generateBillPdf(
            @PathVariable Long billId) {

        byte[] pdf = billPdfService.generateBillPdf(billId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=final-bill-" + billId + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}