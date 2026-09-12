package com.hospital.hospital_billing_system.billing.repository;

import com.hospital.hospital_billing_system.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByBillBillId(Long billId);

    @Query(value = "SELECT nextval('invoice_number_seq')", nativeQuery = true)
    long getNextInvoiceNumber();
}