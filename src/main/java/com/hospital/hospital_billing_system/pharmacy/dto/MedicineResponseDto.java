package com.hospital.hospital_billing_system.pharmacy.dto;

import com.hospital.hospital_billing_system.common.enums.DosageForm;
import com.hospital.hospital_billing_system.common.enums.MedicineRoute;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponseDto {

    private Long medicineId;

    private String medicineCode;

    private String genericName;

    private String brandName;

    private String strength;

    private DosageForm dosageForm;

    private MedicineRoute route;

    private String manufacturer;

    private String pbsItemCode;

    private Boolean prescriptionRequired;

    private BigDecimal unitPrice;

    private Integer reorderLevel;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}