package com.hospital.hospital_billing_system.room.controller;

import com.hospital.hospital_billing_system.room.dto.RoomRequest;
import com.hospital.hospital_billing_system.room.dto.RoomResponse;
import com.hospital.hospital_billing_system.room.entity.enums.RoomStatus;
import com.hospital.hospital_billing_system.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing endpoints for hospital room and bed infrastructure.
 */
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // Create a new room/bed facility under a hospital tenant
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody RoomRequest request) {
        RoomResponse response = roomService.createRoom(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Retrieve a room by ID ensuring tenant boundaries
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        RoomResponse response = roomService.getRoomById(roomId, tenantId);
        return ResponseEntity.ok(response);
    }

    // Retrieve all rooms belonging to the authenticated hospital tenant
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRoomsByTenant(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        List<RoomResponse> response = roomService.getRoomsByTenant(tenantId);
        return ResponseEntity.ok(response);
    }

    // Filter only AVAILABLE rooms (Used by Admissions team for allocating beds)
    @GetMapping("/available")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        List<RoomResponse> response = roomService.getAvailableRooms(tenantId);
        return ResponseEntity.ok(response);
    }

    // Filter rooms by department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByDepartment(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID departmentId) {
        List<RoomResponse> response = roomService.getRoomsByDepartment(tenantId, departmentId);
        return ResponseEntity.ok(response);
    }

    // Update complete room details
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @Valid @RequestBody RoomRequest request) {
        RoomResponse response = roomService.updateRoom(roomId, tenantId, request);
        return ResponseEntity.ok(response);
    }

    // Update room occupancy status (AVAILABLE / OCCUPIED / UNDER_MAINTENANCE)
    @PatchMapping("/{roomId}/status")
    public ResponseEntity<RoomResponse> updateRoomStatus(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam("status") RoomStatus status) {
        RoomResponse response = roomService.updateRoomStatus(roomId, tenantId, status);
        return ResponseEntity.ok(response);
    }

    // Delete a room record
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable UUID roomId,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        roomService.deleteRoom(roomId, tenantId);
        return ResponseEntity.noContent().build();
    }
}