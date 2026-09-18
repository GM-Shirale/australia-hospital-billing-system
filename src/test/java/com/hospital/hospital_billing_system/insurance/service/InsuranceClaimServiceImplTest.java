package com.hospital.hospital_billing_system.insurance.service;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.insurance.dto.ClaimResponse;
import com.hospital.hospital_billing_system.insurance.dto.ClaimSubmissionRequest;
import com.hospital.hospital_billing_system.insurance.entity.InsuranceClaim;
import com.hospital.hospital_billing_system.insurance.entity.InsurancePolicy;
import com.hospital.hospital_billing_system.insurance.enums.ClaimStatus;
import com.hospital.hospital_billing_system.insurance.enums.InsuranceType;
import com.hospital.hospital_billing_system.insurance.repository.InsuranceClaimRepository;
import com.hospital.hospital_billing_system.insurance.repository.InsurancePolicyRepository;
import com.hospital.hospital_billing_system.insurance.service.impl.InsuranceClaimServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests verifying Australian Insurance Claim adjudication rules,
 * coverage limits, patient co-payments, and tenant isolation boundaries.
 */
@ExtendWith(MockitoExtension.class)
class InsuranceClaimServiceImplTest {

    @Mock
    private InsuranceClaimRepository claimRepository;

    @Mock
    private InsurancePolicyRepository policyRepository;

    @InjectMocks
    private InsuranceClaimServiceImpl claimService;

    private UUID tenantId;
    private UUID patientId;
    private UUID policyId;
    private UUID billId;
    private InsurancePolicy mockPolicy;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        patientId = UUID.randomUUID();
        policyId = UUID.randomUUID();
        billId = UUID.randomUUID();

        mockPolicy = InsurancePolicy.builder()
                .id(policyId)
                .tenantId(tenantId)
                .patientId(patientId)
                .policyNumber("MED-AU-987654")
                .providerName("Medicare Australia")
                .insuranceType(InsuranceType.PUBLIC_MEDICARE)
                .coverageLimit(new BigDecimal("5000.00"))
                .remainingLimit(new BigDecimal("1500.00"))
                .validFrom(LocalDate.of(2026, 1, 1))
                .validTo(LocalDate.of(2026, 12, 31))
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should approve claim 100% when claimed amount is within remaining balance")
    void shouldApproveClaimFullyWhenWithinLimit() {
        ClaimSubmissionRequest request = ClaimSubmissionRequest.builder()
                .billId(billId)
                .patientId(patientId)
                .policyId(policyId)
                .claimedAmount(new BigDecimal("500.00"))
                .serviceDate(LocalDate.of(2026, 6, 15))
                .build();

        when(claimRepository.findByBillIdAndTenantId(billId, tenantId)).thenReturn(Optional.empty());
        when(policyRepository.findByIdAndTenantId(policyId, tenantId)).thenReturn(Optional.of(mockPolicy));
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(invocation -> {
            InsuranceClaim claim = invocation.getArgument(0);
            claim.setId(UUID.randomUUID());
            return claim;
        });

        ClaimResponse response = claimService.submitAndAdjudicateClaim(request, tenantId);

        assertNotNull(response);
        assertEquals(ClaimStatus.APPROVED, response.getStatus());
        assertEquals(new BigDecimal("500.00"), response.getApprovedAmount());
        assertEquals(BigDecimal.ZERO, response.getPatientCoPayment());
        assertEquals(new BigDecimal("1000.00"), mockPolicy.getRemainingLimit());
        verify(policyRepository, times(1)).save(mockPolicy);
        verify(claimRepository, times(1)).save(any(InsuranceClaim.class));
    }

    @Test
    @DisplayName("Should partially approve claim and assign remaining balance to patient co-payment")
    void shouldPartiallyApproveClaimWhenExceedsLimit() {
        ClaimSubmissionRequest request = ClaimSubmissionRequest.builder()
                .billId(billId)
                .patientId(patientId)
                .policyId(policyId)
                .claimedAmount(new BigDecimal("2000.00")) // Remaining limit is 1500.00
                .serviceDate(LocalDate.of(2026, 6, 15))
                .build();

        when(claimRepository.findByBillIdAndTenantId(billId, tenantId)).thenReturn(Optional.empty());
        when(policyRepository.findByIdAndTenantId(policyId, tenantId)).thenReturn(Optional.of(mockPolicy));
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(invocation -> {
            InsuranceClaim claim = invocation.getArgument(0);
            claim.setId(UUID.randomUUID());
            return claim;
        });

        ClaimResponse response = claimService.submitAndAdjudicateClaim(request, tenantId);

        assertNotNull(response);
        assertEquals(ClaimStatus.PARTIALLY_APPROVED, response.getStatus());
        assertEquals(new BigDecimal("1500.00"), response.getApprovedAmount());
        assertEquals(new BigDecimal("500.00"), response.getPatientCoPayment());
        assertEquals(BigDecimal.ZERO, mockPolicy.getRemainingLimit());
        verify(policyRepository, times(1)).save(mockPolicy);
    }

    @Test
    @DisplayName("Should reject claim when treatment date is outside policy period")
    void shouldRejectClaimWhenServiceDateExpired() {
        ClaimSubmissionRequest request = ClaimSubmissionRequest.builder()
                .billId(billId)
                .patientId(patientId)
                .policyId(policyId)
                .claimedAmount(new BigDecimal("300.00"))
                .serviceDate(LocalDate.of(2027, 2, 10)) // After 2026-12-31
                .build();

        when(claimRepository.findByBillIdAndTenantId(billId, tenantId)).thenReturn(Optional.empty());
        when(policyRepository.findByIdAndTenantId(policyId, tenantId)).thenReturn(Optional.of(mockPolicy));
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClaimResponse response = claimService.submitAndAdjudicateClaim(request, tenantId);

        assertNotNull(response);
        assertEquals(ClaimStatus.REJECTED, response.getStatus());
        assertEquals(BigDecimal.ZERO, response.getApprovedAmount());
        assertEquals(new BigDecimal("300.00"), response.getPatientCoPayment());
        assertEquals("Service date is outside policy validity period", response.getRejectionReason());
        verify(policyRepository, never()).save(mockPolicy);
    }

    @Test
    @DisplayName("Should reject claim when policy remaining limit is zero")
    void shouldRejectClaimWhenLimitExhausted() {
        mockPolicy.setRemainingLimit(BigDecimal.ZERO);

        ClaimSubmissionRequest request = ClaimSubmissionRequest.builder()
                .billId(billId)
                .patientId(patientId)
                .policyId(policyId)
                .claimedAmount(new BigDecimal("400.00"))
                .serviceDate(LocalDate.of(2026, 5, 10))
                .build();

        when(claimRepository.findByBillIdAndTenantId(billId, tenantId)).thenReturn(Optional.empty());
        when(policyRepository.findByIdAndTenantId(policyId, tenantId)).thenReturn(Optional.of(mockPolicy));
        when(claimRepository.save(any(InsuranceClaim.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClaimResponse response = claimService.submitAndAdjudicateClaim(request, tenantId);

        assertNotNull(response);
        assertEquals(ClaimStatus.REJECTED, response.getStatus());
        assertEquals("Annual coverage limit exhausted", response.getRejectionReason());
        verify(policyRepository, never()).save(mockPolicy);
    }

    @Test
    @DisplayName("Should prevent duplicate claim submissions for the same bill")
    void shouldThrowExceptionWhenDuplicateClaimForBill() {
        ClaimSubmissionRequest request = ClaimSubmissionRequest.builder()
                .billId(billId)
                .patientId(patientId)
                .policyId(policyId)
                .claimedAmount(new BigDecimal("500.00"))
                .serviceDate(LocalDate.of(2026, 6, 15))
                .build();

        when(claimRepository.findByBillIdAndTenantId(billId, tenantId))
                .thenReturn(Optional.of(InsuranceClaim.builder().id(UUID.randomUUID()).build()));

        assertThrows(DuplicateResourceException.class, () ->
                claimService.submitAndAdjudicateClaim(request, tenantId)
        );

        verify(claimRepository, never()).save(any(InsuranceClaim.class));
    }
}