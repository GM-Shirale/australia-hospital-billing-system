package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.RefundRequest;
import com.hospital.hospital_billing_system.billing.dto.RefundResponse;

import java.util.List;

public interface RefundService {

    RefundResponse createRefund(RefundRequest request);

    RefundResponse getRefundById(Long refundId);

    List<RefundResponse> getRefundsByTransactionId(Long transactionId);

    List<RefundResponse> getAllRefunds();
}