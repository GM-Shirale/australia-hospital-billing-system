package com.hospital.hospital_billing_system.room.service;

import com.hospital.hospital_billing_system.room.dto.RoomRequest;
import com.hospital.hospital_billing_system.room.dto.RoomResponse;
import com.hospital.hospital_billing_system.room.entity.enums.RoomStatus;

import java.util.List;
import java.util.UUID;

/**
 * Business service defining operations for hospital rooms, beds, and tariff configuration.
 */
public interface RoomService {

    RoomResponse createRoom(RoomRequest request);

    RoomResponse getRoomById(UUID roomId, UUID tenantId);

    List<RoomResponse> getRoomsByTenant(UUID tenantId);

    List<RoomResponse> getAvailableRooms(UUID tenantId);

    List<RoomResponse> getRoomsByDepartment(UUID tenantId, UUID departmentId);

    RoomResponse updateRoom(UUID roomId, UUID tenantId, RoomRequest request);

    RoomResponse updateRoomStatus(UUID roomId, UUID tenantId, RoomStatus status);

    void deleteRoom(UUID roomId, UUID tenantId);
}