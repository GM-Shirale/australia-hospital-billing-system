package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.BillingSummaryResponse;

public interface BillingSummaryService {

    BillingSummaryResponse getBillingSummary(Long patientId);

    BillingSummaryResponse getBillingSummary();

}