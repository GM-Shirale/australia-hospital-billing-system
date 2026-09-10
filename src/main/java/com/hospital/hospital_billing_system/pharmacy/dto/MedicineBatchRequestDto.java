package com.hospital.hospital_billing_system.pharmacy.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineBatchRequestDto {

    @NotNull(message = "Medicine ID is required")
    @Positive(message = "Medicine ID must be greater than zero")
    private Long medicineId;

    @NotBlank(message = "Batch number is required")
    @Size(max = 100, message = "Batch number must not exceed 100 characters")
    private String batchNumber;

    @NotNull(message = "Manufacturing date is required")
    private LocalDate manufacturingDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Received quantity is required")
    @Positive(message = "Received quantity must be greater than zero")
    private Integer receivedQuantity;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must not be negative")
    private Integer quantity;

    @NotNull(message = "Unit cost is required")
    @DecimalMin(value = "0.00", inclusive = true,
            message = "Unit cost must not be negative")
    @Digits(integer = 8, fraction = 2,
            message = "Unit cost must have up to 8 integer digits and 2 decimal places")
    private BigDecimal unitCost;

    @Size(max = 200, message = "Supplier name must not exceed 200 characters")
    private String supplierName;
}