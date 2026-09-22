package com.hospital.hospital_billing_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Disable CSRF for REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // Disable default login and basic authentication
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // Login APIs are public
                        .requestMatchers(
                                "/api/auth/admin/login",
                                "/api/auth/user/login"
                        ).permitAll()

                        // Admin APIs
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Patient APIs
                        .requestMatchers(
                                "/api/patients/**",
                                "/api/patient-addresses/**",
                                "/api/patient-documents/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECEPTIONIST",
                                "DOCTOR",
                                "BILLING_STAFF",
                                "LAB_STAFF",
                                "PHARMACY_STAFF"
                        )

                        // Admission APIs
                        .requestMatchers("/api/admissions/**")
                        .hasAnyRole(
                                "ADMIN",
                                "RECEPTIONIST",
                                "DOCTOR",
                                "BILLING_STAFF"
                        )

                        // Bill APIs
                        .requestMatchers("/api/bills/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BILLING_STAFF"
                        )

                        // Invoice APIs
                        .requestMatchers("/api/invoices/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BILLING_STAFF"
                        )

                        // Payment APIs
                        .requestMatchers("/api/payments/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BILLING_STAFF"
                        )

                        // Payment transaction APIs
                        .requestMatchers("/api/payment-transactions/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BILLING_STAFF"
                        )

                        // Refund APIs
                        .requestMatchers("/api/refunds/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BILLING_STAFF"
                        )

                        // Billing summary APIs
                        .requestMatchers("/api/billing-summary/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BILLING_STAFF"
                        )

                        // Bill items need additional business-level
                        // authorization based on item type
                        .requestMatchers("/api/bill-items/**")
                        .hasAnyRole(
                                "ADMIN",
                                "BILLING_STAFF",
                                "DOCTOR",
                                "LAB_STAFF",
                                "PHARMACY_STAFF"
                        )

                        // Any other authenticated API
                        .anyRequest().authenticated()
                )

                // Check JWT before Spring Security authentication
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}