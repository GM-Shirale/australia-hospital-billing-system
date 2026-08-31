package com.hospital.hospital_billing_system.laboratary.entity;



import com.hospital.hospital_billing_system.laboratary.entity.enums.LabOrderPriority;
import com.hospital.hospital_billing_system.laboratary.entity.enums.LabOrderStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private LabOrderStatus status = LabOrderStatus.ORDERED;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 30)
    @Builder.Default
    private LabOrderPriority priority = LabOrderPriority.ROUTINE;

    @Column(name = "clinical_notes", length = 500)
    private String clinicalNotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}
