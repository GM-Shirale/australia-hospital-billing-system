package com.hospital.hospital_billing_system.room.service.impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.department.entity.Department;
import com.hospital.hospital_billing_system.department.repository.DepartmentRepository;
import com.hospital.hospital_billing_system.room.dto.RoomRequest;
import com.hospital.hospital_billing_system.room.dto.RoomResponse;
import com.hospital.hospital_billing_system.room.entity.Room;
import com.hospital.hospital_billing_system.room.entity.enums.RoomStatus;
import com.hospital.hospital_billing_system.room.repository.RoomRepository;
import com.hospital.hospital_billing_system.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of RoomService managing room tariffs, tenant boundaries, and bed occupancy states.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public RoomResponse createRoom(RoomRequest request) {
        // Business Rule: A hospital cannot have the same bed number inside the same room twice
        if (roomRepository.existsByTenantIdAndRoomNoAndBedNo(request.getTenantId(), request.getRoomNo(), request.getBedNo())) {
            throw new DuplicateResourceException(
                    "Bed '" + request.getBedNo() + "' already exists in Room '" + request.getRoomNo() + "' for this hospital.");
        }

        // Business Rule: Ensure Department belongs to the same hospital tenant
        Department department = departmentRepository.findByDepartmentIdAndTenantId(request.getDepartmentId(), request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + request.getDepartmentId() + " for this hospital tenant."));

        Room room = Room.builder()
                .tenantId(request.getTenantId())
                .department(department)
                .roomNo(request.getRoomNo())
                .bedNo(request.getBedNo())
                .roomType(request.getRoomType())
                .dailyRate(request.getDailyRate())
                .status(request.getStatus())
                .build();

        Room saved = roomRepository.save(room);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(UUID roomId, UUID tenantId) {
        Room room = roomRepository.findByRoomIdAndTenantId(roomId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        return mapToResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByTenant(UUID tenantId) {
        return roomRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRooms(UUID tenantId) {
        return roomRepository.findByTenantIdAndStatus(tenantId, RoomStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByDepartment(UUID tenantId, UUID departmentId) {
        return roomRepository.findByTenantIdAndDepartment_DepartmentId(tenantId, departmentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse updateRoom(UUID roomId, UUID tenantId, RoomRequest request) {
        Room room = roomRepository.findByRoomIdAndTenantId(roomId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        Department department = departmentRepository.findByDepartmentIdAndTenantId(request.getDepartmentId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + request.getDepartmentId()));

        room.setDepartment(department);
        room.setRoomNo(request.getRoomNo());
        room.setBedNo(request.getBedNo());
        room.setRoomType(request.getRoomType());
        room.setDailyRate(request.getDailyRate());
        room.setStatus(request.getStatus());

        Room updated = roomRepository.save(room);
        return mapToResponse(updated);
    }

    @Override
    public RoomResponse updateRoomStatus(UUID roomId, UUID tenantId, RoomStatus status) {
        Room room = roomRepository.findByRoomIdAndTenantId(roomId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        room.setStatus(status);
        Room updated = roomRepository.save(room);
        return mapToResponse(updated);
    }

    @Override
    public void deleteRoom(UUID roomId, UUID tenantId) {
        Room room = roomRepository.findByRoomIdAndTenantId(roomId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        roomRepository.delete(room);
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .tenantId(room.getTenantId())
                .departmentId(room.getDepartment().getDepartmentId())
                .departmentName(room.getDepartment().getDepartmentName())
                .roomNo(room.getRoomNo())
                .bedNo(room.getBedNo())
                .roomType(room.getRoomType())
                .dailyRate(room.getDailyRate())
                .status(room.getStatus())
                .build();
    }
}