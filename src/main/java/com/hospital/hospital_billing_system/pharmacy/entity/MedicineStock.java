package com.hospital.hospital_billing_system.pharmacy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "medicine_stock",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stock_medicine_batch",
                        columnNames = {"medicine_id", "batch_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_stock_medicine_id",
                        columnList = "medicine_id"
                ),
                @Index(
                        name = "idx_stock_batch_id",
                        columnList = "batch_id"
                )
        })

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="stock_id")
    private Long stockId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "medicine_id",nullable = false,foreignKey = @ForeignKey(name = "fk_stock_medicine"))
    private Medicine medicine;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "batch_id",nullable = false,foreignKey = @ForeignKey(name = "fk_stock_batch"))
    private MedicineBatch batch;


    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "quantity_dispensed", nullable = false)
    private Integer quantityDispensed;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (quantityReceived == null) {
            quantityReceived = 0;
        }

        if (quantityAvailable == null) {
            quantityAvailable = quantityReceived;
        }

        if (quantityDispensed == null) {
            quantityDispensed = 0;
        }

        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
