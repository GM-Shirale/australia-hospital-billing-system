package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.PaymentTransactionRequest;
import com.hospital.hospital_billing_system.billing.dto.PaymentTransactionResponse;

import java.util.List;

public interface PaymentTransactionService {

    PaymentTransactionResponse createTransaction(PaymentTransactionRequest request);

    PaymentTransactionResponse getTransactionById(Long transactionId);

    List<PaymentTransactionResponse> getTransactionsByPaymentId(Long paymentId);


}