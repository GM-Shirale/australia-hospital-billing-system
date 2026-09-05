package com.hospital.hospital_billing_system.billing.entity;


import com.hospital.hospital_billing_system.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    // unique hospital bill number
    @Column(name = "bill_number", nullable = false, unique = true, length = 50)
    private String billNumber;

    // patient for whom bill is generated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // items included in this bill
    @OneToMany(
            mappedBy = "bill",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<BillItem> billItems = new ArrayList<>();

    // total amount of the bill
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    // amount to be paid by patient
    @Column(name = "patient_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal patientAmount;

    // amount expected from insurance
    @Column(name = "insurance_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal insuranceAmount;

    // when bill was generated
    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billDate;
}
