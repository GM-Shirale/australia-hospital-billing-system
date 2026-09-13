package com.hospital.hospital_billing_system.insurance.entity;

import com.hospital.hospital_billing_system.insurance.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "insurance_claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID billId;

    @Column(nullable = false)
    private UUID patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private InsurancePolicy policy;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal claimedAmount; // Bill amount in AUD

    @Column(precision = 12, scale = 2)
    private BigDecimal approvedAmount; // Payer approved amount in AUD

    @Column(precision = 12, scale = 2)
    private BigDecimal patientCoPayment; // Patient out-of-pocket share in AUD

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;

    private String rejectionReason;

    @Column(nullable = false)
    private LocalDateTime claimDate;

    private LocalDateTime settlementDate;
}