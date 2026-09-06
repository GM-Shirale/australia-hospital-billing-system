package com.hospital.hospital_billing_system.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Inbound payload for registering or updating a medical doctor/provider.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequest {

    @NotNull(message = "Tenant ID is mandatory")
    private UUID tenantId;

    @NotNull(message = "Department ID is mandatory")
    private UUID departmentId;

    @NotBlank(message = "First name is mandatory")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Specialization is mandatory")
    @Size(max = 100, message = "Specialization cannot exceed 100 characters")
    private String specialization;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    /**
     * Australian Medicare Provider Number format validation.
     * Must be 6 to 8 alphanumeric characters (standard Australian Medicare pattern).
     */
    @NotBlank(message = "Medicare Provider Number is mandatory for billing eligibility")
    @Pattern(regexp = "^[0-9]{6,7}[A-Z0-9]$", message = "Invalid Australian Medicare Provider Number format")
    private String providerNo;
}