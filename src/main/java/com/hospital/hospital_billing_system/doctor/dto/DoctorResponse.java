package com.hospital.hospital_billing_system.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Outbound response view representing registered doctor details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {

    private UUID doctorId;
    private UUID tenantId;
    private UUID departmentId;
    private String departmentName;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private String providerNo;
}