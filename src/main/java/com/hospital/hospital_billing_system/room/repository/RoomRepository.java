package com.hospital.hospital_billing_system.room.repository;

import com.hospital.hospital_billing_system.room.entity.Room;
import com.hospital.hospital_billing_system.room.entity.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for managing physical room and bed allocations.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    // Retrieve all rooms belonging to a given hospital tenant
    List<Room> findByTenantId(UUID tenantId);

    // Filter available rooms for patient admission allocation
    List<Room> findByTenantIdAndStatus(UUID tenantId, RoomStatus status);

    // Find rooms housed under a specific department
    List<Room> findByTenantIdAndDepartment_DepartmentId(UUID tenantId, UUID departmentId);

    // Prevent duplicate bed registration within the same room in a hospital
    boolean existsByTenantIdAndRoomNoAndBedNo(UUID tenantId, String roomNo, String bedNo);

    // Fetch room securely scoped to the tenant
    Optional<Room> findByRoomIdAndTenantId(UUID roomId, UUID tenantId);
}