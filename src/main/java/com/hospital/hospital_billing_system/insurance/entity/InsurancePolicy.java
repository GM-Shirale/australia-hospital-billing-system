package com.hospital.hospital_billing_system.insurance.entity;

import com.hospital.hospital_billing_system.insurance.enums.InsuranceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "insurance_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false, length = 100)
    private String policyNumber;

    @Column(nullable = false, length = 100)
    private String providerName; // e.g., Medicare, Bupa, Medibank

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceType insuranceType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal coverageLimit; // Total annual limit in AUD

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal remainingLimit; // Remaining balance in AUD

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    @Column(nullable = false)
    private boolean active;
}