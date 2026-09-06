package com.hospital.hospital_billing_system.laboratory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabOrderItemRequestDTO {

    @NotNull(message = "Lab order ID is required")
    private Long labOrderId;

    @NotNull(message = "Lab test ID is required")
    private Long labTestId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

   /* @NotNull(message = "Price is required")
    private BigDecimal price;
*/
    @Size(max = 500, message = "Clinical notes must not exceed 500 characters")
    private String clinicalNotes;
}