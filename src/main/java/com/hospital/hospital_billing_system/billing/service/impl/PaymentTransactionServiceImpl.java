package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.PaymentTransactionRequest;
import com.hospital.hospital_billing_system.billing.dto.PaymentTransactionResponse;
import com.hospital.hospital_billing_system.billing.entity.Payment;
import com.hospital.hospital_billing_system.billing.entity.PaymentStatus;
import com.hospital.hospital_billing_system.billing.entity.PaymentTransaction;
import com.hospital.hospital_billing_system.billing.entity.PaymentTransactionStatus;
import com.hospital.hospital_billing_system.billing.repository.PaymentRepository;
import com.hospital.hospital_billing_system.billing.repository.PaymentTransactionRepository;
import com.hospital.hospital_billing_system.billing.service.PaymentTransactionService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentTransactionResponse createTransaction(PaymentTransactionRequest request) {

        log.info("Creating payment transaction for payment id: {}", request.getPaymentId());

        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + request.getPaymentId()));

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Transaction amount must be greater than zero");
        }

        if (request.getPaymentMethod() == null) {
            throw new IllegalStateException("Payment method is required");
        }

        BigDecimal remainingAmount = payment.getRemainingAmount();

        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Payment is already fully paid");
        }

        if (request.getAmount().compareTo(remainingAmount) > 0) {
            throw new IllegalStateException(
                    "Transaction amount exceeds remaining payment amount");
        }

        BigDecimal newPaidAmount = payment.getPaidAmount()
                .add(request.getAmount());

        BigDecimal newRemainingAmount = payment.getTotalAmount()
                .subtract(newPaidAmount);

        payment.setPaidAmount(newPaidAmount);
        payment.setRemainingAmount(newRemainingAmount);

        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            payment.setPaymentStatus(PaymentStatus.PAID);
        } else {
            payment.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        }

        paymentRepository.save(payment);

        long number = paymentTransactionRepository.getNextTransactionNumber();
        String transactionNumber = String.format("TXN-%05d", number);

        PaymentTransaction transaction = PaymentTransaction.builder()
                .transactionNumber(transactionNumber)
                .payment(payment)
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .transactionStatus(PaymentTransactionStatus.SUCCESS)
                .gatewayName(request.getGatewayName())
                .gatewayTransactionId(request.getGatewayTransactionId())
                .transactionDate(LocalDateTime.now())
                .build();

        PaymentTransaction savedTransaction =
                paymentTransactionRepository.save(transaction);

        log.info("Payment transaction created successfully with id: {}",
                savedTransaction.getTransactionId());

        return mapToResponse(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentTransactionResponse getTransactionById(Long transactionId) {

        log.info("Fetching payment transaction with id: {}", transactionId);

        PaymentTransaction transaction =
                paymentTransactionRepository.findById(transactionId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Payment transaction not found with id: " + transactionId));

        return mapToResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentTransactionResponse> getTransactionsByPaymentId(Long paymentId) {

        log.info("Fetching transactions for payment id: {}", paymentId);

        if (!paymentRepository.existsById(paymentId)) {
            throw new ResourceNotFoundException(
                    "Payment not found with id: " + paymentId);
        }

        return paymentTransactionRepository
                .findByPaymentPaymentId(paymentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PaymentTransactionResponse mapToResponse(
            PaymentTransaction transaction) {

        return PaymentTransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .transactionNumber(transaction.getTransactionNumber())
                .paymentId(transaction.getPayment().getPaymentId())
                .paymentMethod(transaction.getPaymentMethod())
                .amount(transaction.getAmount())
                .transactionStatus(transaction.getTransactionStatus())
                .gatewayName(transaction.getGatewayName())
                .gatewayTransactionId(transaction.getGatewayTransactionId())
                .transactionDate(transaction.getTransactionDate())
                .failureReason(transaction.getFailureReason())
                .build();
    }
}