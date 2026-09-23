package com.hospital.hospital_billing_system.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // unique username used for login
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    // encrypted password
    @Column(name = "password", nullable = false)
    private String password;

    // user email
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // role assigned to the user
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Role role;

    // whether user is active
    @Column(name = "active", nullable = false)
    private Boolean active;
}