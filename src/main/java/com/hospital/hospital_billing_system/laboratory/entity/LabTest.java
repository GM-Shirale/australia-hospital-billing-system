package com.hospital.hospital_billing_system.laboratory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "lab_tests") 
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class LabTest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "test_code", nullable = false, unique = true, length = 50) 
	private String testCode;

	@Column(name = "test_name", nullable = false, length = 150) 
	private String testName;

	@Column(name = "category", nullable = false, length = 100) 
	private String category;

	@Column(name = "sample_type", nullable = false, length = 100) 
	private String sampleType;

	@Column(name = "description", length = 500) 
	private String description;

	@Column(name = "price", nullable = false, precision = 10, scale = 2) 
	private BigDecimal price;

	@Column(name = "turnaround_time")
	private Integer turnaroundTime;

	@Column(name = "active", nullable = false) 
	private Boolean active = true;

	@Column(name = "created_at", nullable = false) 
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist 
	protected void onCreate() { 
		createdAt = LocalDateTime.now(); }

	@PreUpdate 
	protected void onUpdate() {
		updatedAt = LocalDateTime.now(); } }