package com.hospital.hospital_billing_system.room.service;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.department.entity.Department;
import com.hospital.hospital_billing_system.department.repository.DepartmentRepository;
import com.hospital.hospital_billing_system.room.dto.RoomRequest;
import com.hospital.hospital_billing_system.room.dto.RoomResponse;
import com.hospital.hospital_billing_system.room.entity.Room;
import com.hospital.hospital_billing_system.room.repository.RoomRepository;
import com.hospital.hospital_billing_system.room.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private UUID tenantId;
    private UUID departmentId;
    private RoomRequest request;
    private Room room;
    private Department department;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        departmentId = UUID.randomUUID();

        department = Department.builder()
                .departmentId(departmentId)
                .tenantId(tenantId)
                .departmentName("Cardiology")
                .location("Building A")
                .build();

        request = RoomRequest.builder()
                .tenantId(tenantId)
                .departmentId(departmentId)
                .roomNo("101")
                .bedNo("BED-A")
                .dailyRate(new BigDecimal("250.00"))
                .build();

        room = Room.builder()
                .roomId(UUID.randomUUID())
                .tenantId(tenantId)
                .department(department)
                .roomNo("101")
                .bedNo("BED-A")
                .dailyRate(new BigDecimal("250.00"))
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new room and bed")
    void shouldCreateRoomSuccessfully() {
        when(roomRepository.existsByTenantIdAndRoomNoAndBedNo(tenantId, "101", "BED-A"))
                .thenReturn(false);
        when(departmentRepository.findByDepartmentIdAndTenantId(departmentId, tenantId))
                .thenReturn(Optional.of(department));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomResponse response = roomService.createRoom(request);

        assertNotNull(response);
        assertEquals("101", response.getRoomNo());
        assertEquals("BED-A", response.getBedNo());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when bed already exists in room")
    void shouldThrowExceptionWhenBedAlreadyExists() {
        when(roomRepository.existsByTenantIdAndRoomNoAndBedNo(tenantId, "101", "BED-A"))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> roomService.createRoom(request));
        verify(roomRepository, never()).save(any(Room.class));
    }
}