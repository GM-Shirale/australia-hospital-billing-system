package com.hospital.hospital_billing_system.admission.entity;


import com.hospital.hospital_billing_system.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admission {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admission_id")
    private Long admissionId;

    // unique admission number
    @Column(name = "admission_number", nullable = false, unique = true, length = 50)
    private String admissionNumber;

    // patient who is admitted
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // date and time when patient is admitted
    @Column(name = "admission_date", nullable = false)
    private LocalDateTime admissionDate;

    // date and time when patient is discharged
    @Column(name = "discharge_date")
    private LocalDateTime dischargeDate;

    // reason for admission
    @Column(name = "admission_reason", length = 255)
    private String admissionReason;

    // current status of admission
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AdmissionStatus status;
}