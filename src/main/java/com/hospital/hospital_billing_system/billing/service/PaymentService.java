package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.PaymentRequest;
import com.hospital.hospital_billing_system.billing.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByBillId(Long billId);

    List<PaymentResponse> getAllPayments();
}