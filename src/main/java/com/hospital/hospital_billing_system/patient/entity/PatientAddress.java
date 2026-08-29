package com.hospital.hospital_billing_system.patient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAddress {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    // address type like HOME, WORK
    @Column(name = "address_type", nullable = false, length = 30)
    private String addressType;

    // street address
    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;

    // additional address details
    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    // suburb name
    @Column(name = "suburb", length = 100)
    private String suburb;

    // state like NSW, VIC, QLD
    @Column(name = "state", length = 50)
    private String state;

    // Australian postcode
    @Column(name = "postcode", length = 10)
    private String postcode;

    // country name
    @Column(name = "country", length = 100)
    private String country;

    // patient who owns this address
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}