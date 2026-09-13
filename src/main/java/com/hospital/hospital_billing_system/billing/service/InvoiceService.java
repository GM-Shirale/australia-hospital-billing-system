package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.InvoiceRequest;
import com.hospital.hospital_billing_system.billing.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse createInvoice(InvoiceRequest request);

    InvoiceResponse getInvoiceById(Long invoiceId);

    InvoiceResponse getInvoiceByBillId(Long billId);

    List<InvoiceResponse> getAllInvoices();
}