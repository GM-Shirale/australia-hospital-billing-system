package com.hospital.hospital_billing_system.laboratory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabSampleRequestDTO {

    @NotNull(message = "Lab order ID is required")
    private Long labOrderId;

    @NotBlank(message = "Sample type is required")
    @Size(max = 100,message = "Sample type must not exced 100 characters")
    private String sampleType;

    @Size(max =100, message = "Barcode must not exceed 100 characters")
    private String barcode;

    @Size(max = 400,message = "Collection notes must not exceed 400 char")
    private String collectionNotes;

}
