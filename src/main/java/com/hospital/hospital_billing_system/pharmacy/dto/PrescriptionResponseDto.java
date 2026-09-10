package com.hospital.hospital_billing_system.pharmacy.dto;

import com.hospital.hospital_billing_system.common.enums.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDto {

    private Long prescriptionId;

    private Long patientId;

    private UUID doctorId;

    private LocalDate prescriptionDate;

    private String notes;

    private PrescriptionStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
