package com.hospital.hospital_billing_system.room.dto;

import com.hospital.hospital_billing_system.room.entity.enums.RoomStatus;
import com.hospital.hospital_billing_system.room.entity.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Outbound response view representing hospital room and bed details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {

    private UUID roomId;
    private UUID tenantId;
    private UUID departmentId;
    private String departmentName;
    private String roomNo;
    private String bedNo;
    private RoomType roomType;
    private BigDecimal dailyRate;
    private RoomStatus status;
}