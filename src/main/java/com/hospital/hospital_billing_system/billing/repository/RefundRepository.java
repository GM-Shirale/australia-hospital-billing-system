package com.hospital.hospital_billing_system.billing.repository;

import com.hospital.hospital_billing_system.billing.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    List<Refund> findByPaymentTransactionTransactionId(Long transactionId);

    @Query(value = "SELECT nextval('refund_number_seq')", nativeQuery = true)
    long getNextRefundNumber();
}