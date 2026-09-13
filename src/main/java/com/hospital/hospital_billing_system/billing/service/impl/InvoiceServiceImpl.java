package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.InvoiceRequest;
import com.hospital.hospital_billing_system.billing.dto.InvoiceResponse;
import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.entity.Invoice;
import com.hospital.hospital_billing_system.billing.entity.InvoiceStatus;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.repository.InvoiceRepository;
import com.hospital.hospital_billing_system.billing.service.InvoiceService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final BillRepository billRepository;

    @Override
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {

        log.info("Creating invoice for bill id: {}", request.getBillId());

        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with id: " + request.getBillId()));

        if (invoiceRepository.findByBillBillId(request.getBillId()).isPresent()) {
            throw new IllegalStateException(
                    "Invoice already exists for bill id: " + request.getBillId());
        }

        long number = invoiceRepository.getNextInvoiceNumber();
        String invoiceNumber = String.format("INV-%05d", number);

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .bill(bill)
                .invoiceAmount(bill.getTotalAmount())
                .invoiceDate(LocalDateTime.now())
                .invoiceStatus(InvoiceStatus.GENERATED)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        log.info("Invoice created successfully with id: {}",
                savedInvoice.getInvoiceId());

        return mapToResponse(savedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long invoiceId) {

        log.info("Fetching invoice with id: {}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found with id: " + invoiceId));

        return mapToResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceByBillId(Long billId) {

        log.info("Fetching invoice for bill id: {}", billId);

        Invoice invoice = invoiceRepository.findByBillBillId(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found for bill id: " + billId));

        return mapToResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {

        log.info("Fetching all invoices");

        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {

        return InvoiceResponse.builder()
                .invoiceId(invoice.getInvoiceId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .billId(invoice.getBill().getBillId())
                .invoiceAmount(invoice.getInvoiceAmount())
                .invoiceDate(invoice.getInvoiceDate())
                .invoiceStatus(invoice.getInvoiceStatus())
                .build();
    }
}