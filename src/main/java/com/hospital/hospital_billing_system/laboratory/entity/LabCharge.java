package com.hospital.hospital_billing_system.laboratory.entity;


import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "lab_charges",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lab_charge_number",
                        columnNames = "charge_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "charge_number", nullable = false, unique = true, length = 50)
    private String chargeNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lab_order_item_id", nullable = false)
    private LabOrderItem labOrderItem;

    /**
     * Provider's actual charge for the laboratory service.
     */
    @Column(name = "provider_charge", nullable = false, precision = 12, scale = 2)
    private BigDecimal providerCharge;

    /**
     * Applicable MBS item number, if the laboratory service
     * has an applicable MBS item.
     */
    @Column(name = "mbs_item_number", length = 20)
    private String mbsItemNumber;

    /**
     * Medicare benefit applicable to this service,
     * when eligible.
     */
    @Column(name = "medicare_benefit", precision = 12, scale = 2)
    private BigDecimal medicareBenefit;

    /**
     * Amount payable by the patient after applicable
     * Medicare/private billing arrangements.
     */
    @Column(name = "patient_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal patientAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_type", nullable = false, length = 30)
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private LabChargeStatus status = LabChargeStatus.PENDING;

    @Column(name = "charged_at", nullable = false)
    private LocalDateTime chargedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (chargedAt == null) {
            chargedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}