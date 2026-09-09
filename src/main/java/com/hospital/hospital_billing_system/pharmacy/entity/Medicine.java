package com.hospital.hospital_billing_system.pharmacy.entity;

import com.hospital.hospital_billing_system.common.enums.DosageForm;
import com.hospital.hospital_billing_system.common.enums.MedicineRoute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {

    // primary key of the medicine table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Long medicineId;

    //Unique hospital medicine code.
    @Column(name = "medicine_code",nullable = false,unique = true,length = 50)
    private String medicineCode;

    @Column(name = "generic_name",nullable = false,length = 150)
    private String genericName;

    @Column(name = "brand_name",length = 150)
    private String brandName;

    @Column(name = "strength",nullable = false,length = 100)
    private String strength;

    @Enumerated(EnumType.STRING)
    @Column(name = "dosage_form",nullable = false,length = 50)
    private DosageForm dosageForm;

    @Enumerated(EnumType.STRING)
    @Column(name = "route",nullable = false,length = 50)
    private MedicineRoute route;


    @Column(name ="manufacturer",length = 200)
    private String manufacturer;

    @Column(name ="pbs_item_code",nullable = false )
    private String pbsItemCode;

    @Column(name = "prescription_required", nullable = false)
    private Boolean prescriptionRequired;


    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (prescriptionRequired == null) {
            prescriptionRequired = true;
        }
        if (active == null) {
            active = true;
        }
    }
    @PreUpdate
    protected void onUpdate () {

        updatedAt = LocalDateTime.now();
    }
}

