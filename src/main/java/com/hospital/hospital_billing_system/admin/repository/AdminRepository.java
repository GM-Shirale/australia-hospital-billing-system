package com.hospital.hospital_billing_system.admin.repository;

import com.hospital.hospital_billing_system.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // find admin by username during login
    Optional<Admin> findByUsername(String username);

    // check whether admin username already exists
    boolean existsByUsername(String username);

    // check whether admin email already exists
    boolean existsByEmail(String email);
}