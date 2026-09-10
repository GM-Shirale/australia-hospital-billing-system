package com.hospital.hospital_billing_system.pharmacy.entity;

import com.hospital.hospital_billing_system.common.enums.MedicineBatchStatus;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Data
@Table(name = "medicine_batch",uniqueConstraints = {@UniqueConstraint(name = "uk_medicine_batch",columnNames = {"medicine_id","batch_number"})},indexes = {@Index(name = "idx_medicine_batch_expiry_date", columnList = "expiry_date"),
        @Index(name = "idx_medicine_batch_status", columnList = "status")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
private Long batchId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id",nullable = false,foreignKey = @ForeignKey(name = "fk_medicine_batch_medicine"))
    private Medicine medicine;

    @Column(name = "batch_number",nullable = false,length = 100)
    private String batchNumber;

    @Column(name = "manufacturing_date",nullable = false)
    private LocalDate manufacturingDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "received_quantity",nullable = false)
    private Integer receivedQuantity;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost",nullable = false,precision = 10,scale = 2)
    private BigDecimal unitCost;

    @Column(name = "supplier_name",length = 200)
    private String supplierName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false,length = 30)
    private MedicineBatchStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = MedicineBatchStatus.AVAILABLE;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}

