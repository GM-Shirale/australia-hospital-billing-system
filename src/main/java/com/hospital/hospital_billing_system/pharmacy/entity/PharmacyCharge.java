package com.hospital.hospital_billing_system.pharmacy.entity;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "pharmacy_charge",
        indexes = {
                @Index(
                        name = "idx_pharmacy_charge_dispensing_id",
                        columnList = "dispensing_id"
                ),
                @Index(
                        name = "idx_pharmacy_charge_patient_id",
                        columnList = "patient_id"
                ),
                @Index(
                        name = "idx_pharmacy_charge_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_pharmacy_charge_billing_type",
                        columnList = "billing_type"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_pharmacy_charge_number",
                        columnNames = "charge_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacy_charge_id")
    private Long pharmacyChargeId;

    @Column(
            name = "charge_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String chargeNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "dispensing_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_pharmacy_charge_dispensing"
            )
    )
    private Dispensing dispensing;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(
            name = "unit_price",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal unitPrice;

    @Column(
            name = "total_amount",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "billing_type",
            nullable = false,
            length = 30
    )
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private LabChargeStatus status;

    @Column(name = "charged_at", nullable = false)
    private LocalDateTime chargedAt;

    @Column(name = "notes", length = 500)
    private String notes;

    @PrePersist
    protected void onCreate() {

        if (chargedAt == null) {
            chargedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = LabChargeStatus.PENDING;
        }
    }
}