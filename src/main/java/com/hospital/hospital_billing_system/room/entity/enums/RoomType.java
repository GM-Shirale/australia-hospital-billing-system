package com.hospital.hospital_billing_system.room.entity.enums;

/**
 * Categorizes clinical hospital accommodation types.
 * Impacts daily charge calculation during billing generation.
 */
public enum RoomType {
    GENERAL_WARD,
    SEMI_PRIVATE,
    PRIVATE,
    ICU,
    ISOLATION
}