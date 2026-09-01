package com.hospital.hospital_billing_system.laboratory.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTestRequestDTO {

    @NotNull(message = "Test code is required")
    @Size(max = 50,message = "Not more than 50 characters")
    private String testCode;

     @NotBlank(message = "Test name is required")
     @Size(max = 150,message = "Test name should not be greater than 150")
    private String testName;

     @NotBlank(message = "Sample test is required")
     @Size(max=100,message = "Category must not exceed 100 characters")
     private String category;

     @NotBlank(message = "sample type is required")
     @Size(max = 100,message = "Sample type must not exceed 500 characters")
     private String sampleType;

     @Size(max = 500,message = "Description must not exceed 500 characters")
     private String description;

     @NotNull(message = "price is required")
     @DecimalMin(value = "0.00",inclusive = true,message = "price cannot be negative")
     @Digits(integer = 8,fraction = 2,message = "Price attlist 8 digit and 2 decimal places")
     private BigDecimal price;


     @NotNull(message = "Turnaround time is required")
     @DecimalMin(value = "0",message = "turnaround time cannot be negative")
     private Integer turnaroundTime;

     private Boolean active;


}
