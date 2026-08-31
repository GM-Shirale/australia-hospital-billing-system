package com.hospital.hospital_billing_system.patient.entity;

import com.hospital.hospital_billing_system.common.enums.Gender;
import com.hospital.hospital_billing_system.common.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;


    @Column(
            name = "patient_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String patientNumber;


    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;


    @Column(name = "middle_name", length = 100)
    private String middleName;


    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;


    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;


    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 30)
    private Gender gender;


     // Australian Medicare number.

    @Column(name = "medicare_number", length = 20,unique = true)
    private String medicareNumber;


     // Medicare Individual Reference Number.

    @Column(name = "medicare_irn", length = 10)
    private String medicareIrn;


    @Column(name = "email", length = 255,unique = true)
    private String email;


    @Column(name = "phone", length = 10,unique = true)
    private String phone;


    @Column(name = "emergency_contact_name", length = 10)
    private String emergencyContactName;


    @Column(name = "emergency_contact_phone", length = 10)
    private String emergencyContactPhone;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PatientStatus status;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


     // Executes automatically before inserting a new patient.

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Set ACTIVE as the default patient status.
        if (status == null) {
            status = PatientStatus.ACTIVE;
        }
    }


     // Executes automatically before updating a patient.

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}