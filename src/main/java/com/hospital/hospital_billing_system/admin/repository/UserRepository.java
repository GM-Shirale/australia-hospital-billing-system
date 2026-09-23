package com.hospital.hospital_billing_system.admin.repository;

import com.hospital.hospital_billing_system.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // find user by username for login
    Optional<User> findByUsername(String username);

    // check whether username already exists
    boolean existsByUsername(String username);

    // check whether email already exists
    boolean existsByEmail(String email);
}