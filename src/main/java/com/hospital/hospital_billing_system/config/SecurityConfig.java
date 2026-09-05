package com.hospital.hospital_billing_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // disable CSRF for REST APIs during development
                .csrf(AbstractHttpConfigurer::disable)

                // allow patient and billing APIs without authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/patients/**",
                                "/api/bills/**",
                                "/api/bill-items/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}