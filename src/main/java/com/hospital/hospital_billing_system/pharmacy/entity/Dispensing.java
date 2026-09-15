package com.hospital.hospital_billing_system.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "dispensing",
        indexes = {
                @Index(
                        name = "idx_dispensing_prescription_item_id",
                        columnList = "prescription_item_id"
                ),
                @Index(
                        name = "idx_dispensing_stock_id",
                        columnList = "stock_id"
                ),
                @Index(
                        name = "idx_dispensing_dispensed_at",
                        columnList = "dispensed_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dispensing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispensing_id")
    private Long dispensingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "prescription_item_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_dispensing_prescription_item"
            )
    )
    private PrescriptionItem prescriptionItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "stock_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_dispensing_stock"
            )
    )
    private MedicineStock stock;

    @Column(name = "quantity_dispensed", nullable = false)
    private Integer quantityDispensed;

    @Column(name = "dispensed_at", nullable = false)
    private LocalDateTime dispensedAt;

    @Column(name = "dispensed_by", nullable = false, length = 100)
    private String dispensedBy;

    @Column(name = "notes", length = 500)
    private String notes;

    @PrePersist
    protected void onCreate() {
        if (dispensedAt == null) {
            dispensedAt = LocalDateTime.now();
        }
    }
}