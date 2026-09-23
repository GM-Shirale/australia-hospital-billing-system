package com.hospital.hospital_billing_system.room.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Inbound payload for calculating room stay accommodation charges.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomStayChargeRequest {

    @NotNull(message = "Tenant ID is mandatory")
    private UUID tenantId;

    @NotNull(message = "Patient ID is mandatory")
    private UUID patientId;

    @NotNull(message = "Room ID is mandatory")
    private UUID roomId;

    @NotNull(message = "Admission date is mandatory")
    private LocalDate admissionDate;

    @NotNull(message = "Discharge date is mandatory")
    private LocalDate dischargeDate;

    @Min(value = 1, message = "Total days stayed must be at least 1")
    private long totalDays;
}