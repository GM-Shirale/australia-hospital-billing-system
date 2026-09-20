package com.hospital.hospital_billing_system.ai.controller;

import com.hospital.hospital_billing_system.ai.dto.MbsSuggestionRequest;
import com.hospital.hospital_billing_system.ai.service.BillingAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai/billing")
@RequiredArgsConstructor
public class BillingAiController {

    private final BillingAiService billingAiService;

    @PostMapping("/suggest-mbs")
    public ResponseEntity<Map<String, String>> suggestMbsCodes(@Valid @RequestBody MbsSuggestionRequest request) {
        String suggestion = billingAiService.suggestMbsCodes(request);
        return ResponseEntity.ok(Map.of("suggestion", suggestion));
    }
}