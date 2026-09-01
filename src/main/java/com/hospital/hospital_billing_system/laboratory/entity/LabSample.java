package com.hospital.hospital_billing_system.laboratory.entity;


import com.hospital.hospital_billing_system.common.enums.SampleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_samples")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabSample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sample_number", nullable = false, unique = true, length = 50)
    private String sampleNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @Column(name = "sample_type", nullable = false, length = 100)
    private String sampleType;

    @Column(name = "barcode", unique = true, length = 100)
    private String barcode;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private SampleStatus status = SampleStatus.COLLECTED;

    @Column(name = "collection_notes", length = 500)
    private String collectionNotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}