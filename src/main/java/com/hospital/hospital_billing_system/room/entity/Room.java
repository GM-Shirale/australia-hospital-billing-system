package com.hospital.hospital_billing_system.room.entity;

import com.hospital.hospital_billing_system.department.entity.Department;
import com.hospital.hospital_billing_system.room.entity.enums.RoomStatus;
import com.hospital.hospital_billing_system.room.entity.enums.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents hospital physical facilities (rooms and beds).
 * Tracks occupancy status and base tariff rates used by the central billing engine.
 */
@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    // Primary unique identifier for the room facility
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id", updatable = false, nullable = false)
    private UUID roomId;

    // Tenant identifier ensuring room records belong strictly to one hospital
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    // Physical room number designation (e.g., "R-101", "ICU-04")
    @Column(name = "room_no", nullable = false, length = 30)
    private String roomNo;

    // Specific bed identifier within the room (e.g., "Bed-A", "Bed-01")
    @Column(name = "bed_no", nullable = false, length = 30)
    private String bedNo;

    // Category of room accommodation
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false, length = 30)
    private RoomType roomType;

    /**
     * Daily accommodation tariff in AUD.
     * Uses BigDecimal to prevent floating-point precision errors during invoice generation.
     */
    @Column(name = "daily_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;

    // Real-time occupancy state
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RoomStatus status;

    /**
     * Physical department containment.
     * Links each room/bed to its governing clinical department.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}