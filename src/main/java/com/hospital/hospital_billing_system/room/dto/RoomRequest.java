package com.hospital.hospital_billing_system.room.dto;

import com.hospital.hospital_billing_system.room.entity.enums.RoomStatus;
import com.hospital.hospital_billing_system.room.entity.enums.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Inbound payload for configuring hospital rooms and bed accommodations.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {

    @NotNull(message = "Tenant ID is mandatory")
    private UUID tenantId;

    @NotNull(message = "Department ID is mandatory")
    private UUID departmentId;

    @NotBlank(message = "Room number is mandatory")
    @Size(max = 30, message = "Room number cannot exceed 30 characters")
    private String roomNo;

    @NotBlank(message = "Bed number is mandatory")
    @Size(max = 30, message = "Bed number cannot exceed 30 characters")
    private String bedNo;

    @NotNull(message = "Room type is mandatory")
    private RoomType roomType;

    /**
     * Daily accommodation tariff in AUD.
     * Must be greater than or equal to 0.00.
     */
    @NotNull(message = "Daily rate is mandatory")
    @DecimalMin(value = "0.00", message = "Daily rate cannot be negative")
    private BigDecimal dailyRate;

    @NotNull(message = "Operational status is mandatory")
    private RoomStatus status;
}