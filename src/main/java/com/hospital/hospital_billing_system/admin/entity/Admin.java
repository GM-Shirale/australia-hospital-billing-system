package com.hospital.hospital_billing_system.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    // username used for admin login
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    // encrypted admin password
    @Column(name = "password", nullable = false)
    private String password;

    // admin email
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // whether admin account is active
    @Column(name = "active", nullable = false)
    private Boolean active;
}