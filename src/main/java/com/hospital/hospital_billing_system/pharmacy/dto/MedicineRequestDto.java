package com.hospital.hospital_billing_system.pharmacy.dto;


import com.hospital.hospital_billing_system.common.enums.DosageForm;
import com.hospital.hospital_billing_system.common.enums.MedicineRoute;
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
public class MedicineRequestDto {

    @NotBlank(message = "Medicine code is required")
    @Size(max = 50,message = "Medicine code must not exceed 150 characters")
    private String medicineCode;

    @NotBlank(message = "Generic name is required")
    @Size(max = 150,message = "Generic name must not exceed 150 characters")
    private String genericName;


    @Size(max = 150, message = "Brand name must not exceed 150 characters")
    private String brandName;


    @NotBlank(message = "Strength is required")
    @Size(max = 100,message = "Strength must not exceed 100 characters")
    private String strength;

    @NotNull(message = "Dosage form is required")
    private DosageForm dosageForm;

    @NotNull(message = "Medicine route is required")
    private MedicineRoute medicineRoute;

    @Size(max = 50,message = "Manufacturer must not exceed 200 characters")
    private String manufacturer;

    @Size(max = 50,message = "PBS item code must not exceed 50 characters")
    private String pbsItemCode;

    @NotNull(message = "Prescription required field is require" )
    private Boolean prescriptionRequired;

    @NotNull(message = "Unit price is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Unit price must not be negative"
    )
    private BigDecimal unitPrice;

    @NotNull(message = "Reorder level is required")
    @Min(value = 0, message = "Reorder level must not be negative")
    private Integer reorderLevel;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
