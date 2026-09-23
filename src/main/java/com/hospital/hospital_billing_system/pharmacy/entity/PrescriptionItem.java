package com.hospital.hospital_billing_system.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescription_item", uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_prescription_medicine",
                        columnNames = {"prescription_id", "medicine_id"}
                )
        },
        indexes = {
                @Index(name = "idx_prescription_item_prescription_id", columnList = "prescription_id"),
                @Index(name = "idx_prescription_item_medicine_id", columnList = "medicine_id")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_item_id")
    private Long prescriptionItemId;

//Prescription to which this medicine is prescribed.
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "prescription_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prescription_item_prescription"))
    private Prescription prescription;

    //Medicine prescribed to the patient.
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "medicine_id",nullable = false,foreignKey = @ForeignKey(name  = "fk_prescription_item_medicine"))
    private Medicine medicine;

    //Example:500mg,10mg,5ml
    @Column(name = "dosage",nullable = false,length = 100)
    private String dosage;


    // Example: Once daily twice daily,Every 8 hour
    @Column(name = "frequency",nullable = false,length = 100)
    private String frequency;

    @Column(name = "duration",nullable = false)
    private Integer duration;

    //Example: DAYS, WEEKS, MONTHS
    @Column(name = "duration_unit",nullable = false,length = 20)
    private String durationUnit;


    //Total quantity prescribed.
    @Column(name = "quantity",nullable = false)
    private Integer quantity;

//Additional instructions for the patient.
    @Column(name = "instructions",length = 500)
    private String instructions;



     // Quantity already dispensed by pharmacy.

    @Column(name = "dispensed_quantity", nullable = false)
    private Integer dispensedQuantity;


    //  Quantity remaining for dispensing.
    @Column(name = "remaining_quantity", nullable = false)
    private Integer remainingQuantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (dispensedQuantity == null) {
            dispensedQuantity = 0;
        }

        if (remainingQuantity == null && quantity != null) {
            remainingQuantity = quantity;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
