package com.hospital.hospital_billing_system.pharmacy.service.Impl;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.pharmacy.dto.PharmacyChargeRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PharmacyChargeResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Dispensing;
import com.hospital.hospital_billing_system.pharmacy.entity.PharmacyCharge;
import com.hospital.hospital_billing_system.pharmacy.mapper.PharmacyChargeMapper;
import com.hospital.hospital_billing_system.pharmacy.repo.DispensingRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.PharmacyChargeRepository;
import com.hospital.hospital_billing_system.pharmacy.service.PharmacyChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PharmacyChargeServiceImpl implements PharmacyChargeService {

    private final PharmacyChargeRepository pharmacyChargeRepository;
    private final DispensingRepository dispensingRepository;
    private final PharmacyChargeMapper pharmacyChargeMapper;

    @Override
    public PharmacyChargeResponseDto createPharmacyCharge(
            PharmacyChargeRequestDto requestDto) {

        log.info(
                "Creating pharmacy charge for dispensing ID: {}",
                requestDto.getDispensingId()
        );

        Dispensing dispensing =
                dispensingRepository.findById(
                        requestDto.getDispensingId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Dispensing not found with ID: "
                                        + requestDto.getDispensingId()
                        )
                );

        if (pharmacyChargeRepository
                .findByDispensingDispensingId(
                        requestDto.getDispensingId()
                ).isPresent()) {

            throw new IllegalStateException(
                    "Pharmacy charge already exists for dispensing ID: "
                            + requestDto.getDispensingId()
            );
        }

        Long patientId =
                dispensing.getPrescriptionItem()
                        .getPrescription()
                        .getPatient()
                        .getPatientId();

        Long medicineId =
                dispensing.getPrescriptionItem()
                        .getMedicine()
                        .getMedicineId();

        Integer quantity =
                dispensing.getQuantityDispensed();

        BigDecimal unitPrice =
                dispensing.getPrescriptionItem()
                        .getMedicine()
                        .getUnitPrice();

        BigDecimal totalAmount =
                unitPrice.multiply(
                        BigDecimal.valueOf(quantity)
                );

        PharmacyCharge charge = PharmacyCharge.builder()
                .chargeNumber(generateChargeNumber())
                .dispensing(dispensing)
                .patientId(patientId)
                .medicineId(medicineId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalAmount(totalAmount)
                .billingType(requestDto.getBillingType())
                .status(LabChargeStatus.PENDING)
                .notes(requestDto.getNotes())
                .build();

        PharmacyCharge savedCharge =
                pharmacyChargeRepository.save(charge);

        log.info(
                "Pharmacy charge created successfully. Charge ID: {}, Charge Number: {}",
                savedCharge.getPharmacyChargeId(),
                savedCharge.getChargeNumber()
        );

        return pharmacyChargeMapper.toResponseDto(savedCharge);
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyChargeResponseDto getPharmacyChargeById(
            Long pharmacyChargeId) {

        PharmacyCharge charge =
                pharmacyChargeRepository.findById(
                        pharmacyChargeId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pharmacy charge not found with ID: "
                                        + pharmacyChargeId
                        )
                );

        return pharmacyChargeMapper.toResponseDto(charge);
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyChargeResponseDto getPharmacyChargeByChargeNumber(
            String chargeNumber) {

        PharmacyCharge charge =
                pharmacyChargeRepository
                        .findByChargeNumber(chargeNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pharmacy charge not found with charge number: "
                                                + chargeNumber
                                )
                        );

        return pharmacyChargeMapper.toResponseDto(charge);
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyChargeResponseDto getPharmacyChargeByDispensingId(
            Long dispensingId) {

        PharmacyCharge charge =
                pharmacyChargeRepository
                        .findByDispensingDispensingId(dispensingId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pharmacy charge not found for dispensing ID: "
                                                + dispensingId
                                )
                        );

        return pharmacyChargeMapper.toResponseDto(charge);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyChargeResponseDto> getPharmacyChargesByPatientId(
            Long patientId) {

        return pharmacyChargeRepository
                .findByPatientId(patientId)
                .stream()
                .map(pharmacyChargeMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyChargeResponseDto> getPharmacyChargesByMedicineId(
            Long medicineId) {

        return pharmacyChargeRepository
                .findByMedicineId(medicineId)
                .stream()
                .map(pharmacyChargeMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyChargeResponseDto> getPharmacyChargesByStatus(
            LabChargeStatus status) {

        return pharmacyChargeRepository
                .findByStatus(status)
                .stream()
                .map(pharmacyChargeMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyChargeResponseDto> getPharmacyChargesByBillingType(
            BillingType billingType) {

        return pharmacyChargeRepository
                .findByBillingType(billingType)
                .stream()
                .map(pharmacyChargeMapper::toResponseDto)
                .toList();
    }

    private String generateChargeNumber() {

        return "PC-" +
                System.currentTimeMillis() +
                "-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}