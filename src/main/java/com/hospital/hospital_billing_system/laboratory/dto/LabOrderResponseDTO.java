package com.hospital.hospital_billing_system.laboratory.dto;


import com.hospital.hospital_billing_system.common.enums.LabOrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabOrderResponseDTO {


    private Long doctorId;

    private Long id;

    private String orderNumber;

    private Long patientId;

    private String patientNumber;

    private LocalDateTime orderedAt;

    private LabOrderStatus status;

    private String clinicalNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}