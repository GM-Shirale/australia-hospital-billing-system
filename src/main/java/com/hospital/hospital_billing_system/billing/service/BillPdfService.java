package com.hospital.hospital_billing_system.billing.service;

public interface BillPdfService {

    // Generate final bill PDF for the given bill
    byte[] generateBillPdf(Long billId);

}