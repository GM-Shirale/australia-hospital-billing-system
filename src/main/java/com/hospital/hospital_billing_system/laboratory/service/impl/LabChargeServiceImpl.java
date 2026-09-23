package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabChargeRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabChargeResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabCharge;
import com.hospital.hospital_billing_system.laboratory.entity.LabOrderItem;
import com.hospital.hospital_billing_system.laboratory.repo.LabChargeRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderItemRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabChargeServiceImpl implements LabChargeService {

    private final LabChargeRepository labChargeRepository;
    private final LabOrderItemRepository labOrderItemRepository;

    @Override
    public LabChargeResponseDTO createCharge(
            LabChargeRequestDTO request) {

        log.info(
                "Creating lab charge for order item ID: {}",
                request.getLabOrderItemId()
        );

        LabOrderItem labOrderItem =
                labOrderItemRepository.findById(
                        request.getLabOrderItemId()
                ).orElseThrow(() -> {

                    log.warn(
                            "Lab order item not found with ID: {}",
                            request.getLabOrderItemId()
                    );

                    return new ResourceNotFoundException(
                            "Lab order item not found with ID: "
                                    + request.getLabOrderItemId()
                    );
                });

        validateAmounts(request);

        LabCharge labCharge = LabCharge.builder()
                .chargeNumber(generateChargeNumber())
                .labOrderItem(labOrderItem)
                .providerCharge(request.getProviderCharge())
                .mbsItemNumber(request.getMbsItemNumber())
                .medicareBenefit(request.getMedicareBenefit())
                .patientAmount(request.getPatientAmount())
                .billingType(request.getBillingType())
                .status(request.getStatus())
                .build();

        LabCharge savedCharge =
                labChargeRepository.save(labCharge);

        log.info(
                "Lab charge created successfully. Charge ID: {}, Charge Number: {}, Order Item ID: {}",
                savedCharge.getId(),
                savedCharge.getChargeNumber(),
                request.getLabOrderItemId()
        );

        return mapToResponseDTO(savedCharge);
    }

    @Override
    @Transactional(readOnly = true)
    public LabChargeResponseDTO getChargeById(Long id) {

        log.debug(
                "Fetching lab charge by ID: {}",
                id
        );

        LabCharge labCharge =
                labChargeRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Lab charge not found with ID: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Lab charge not found with ID: "
                                            + id
                            );
                        });

        return mapToResponseDTO(labCharge);
    }

    @Override
    @Transactional(readOnly = true)
    public LabChargeResponseDTO getChargeByChargeNumber(
            String chargeNumber) {

        log.debug(
                "Fetching lab charge by charge number: {}",
                chargeNumber
        );

        LabCharge labCharge =
                labChargeRepository
                        .findByChargeNumber(chargeNumber)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Lab charge not found with charge number: {}",
                                    chargeNumber
                            );

                            return new ResourceNotFoundException(
                                    "Lab charge not found with charge number: "
                                            + chargeNumber
                            );
                        });

        return mapToResponseDTO(labCharge);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabChargeResponseDTO> getChargesByLabOrderItemId(
            Long labOrderItemId) {

        log.debug(
                "Fetching lab charges for order item ID: {}",
                labOrderItemId
        );

        return labChargeRepository
                .findByLabOrderItemId(labOrderItemId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabChargeResponseDTO> getChargesByStatus(
            LabChargeStatus status) {

        log.debug(
                "Fetching lab charges by status: {}",
                status
        );

        return labChargeRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabChargeResponseDTO> getChargesByBillingType(
            BillingType billingType) {

        log.debug(
                "Fetching lab charges by billing type: {}",
                billingType
        );

        return labChargeRepository
                .findByBillingType(billingType)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public LabChargeResponseDTO updateCharge(
            Long id,
            LabChargeRequestDTO request) {

        log.info(
                "Updating lab charge ID: {}",
                id
        );

        LabCharge labCharge =
                labChargeRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Lab charge not found with ID: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Lab charge not found with ID: "
                                            + id
                            );
                        });

        LabOrderItem labOrderItem =
                labOrderItemRepository.findById(
                        request.getLabOrderItemId()
                ).orElseThrow(() -> {

                    log.warn(
                            "Lab order item not found with ID: {}",
                            request.getLabOrderItemId()
                    );

                    return new ResourceNotFoundException(
                            "Lab order item not found with ID: "
                                    + request.getLabOrderItemId()
                    );
                });

        validateAmounts(request);

        labCharge.setLabOrderItem(labOrderItem);
        labCharge.setProviderCharge(
                request.getProviderCharge()
        );
        labCharge.setMbsItemNumber(
                request.getMbsItemNumber()
        );
        labCharge.setMedicareBenefit(
                request.getMedicareBenefit()
        );
        labCharge.setPatientAmount(
                request.getPatientAmount()
        );
        labCharge.setBillingType(
                request.getBillingType()
        );
        labCharge.setStatus(
                request.getStatus()
        );

        LabCharge updatedCharge =
                labChargeRepository.save(labCharge);

        log.info(
                "Lab charge updated successfully. Charge ID: {}, Charge Number: {}",
                updatedCharge.getId(),
                updatedCharge.getChargeNumber()
        );

        return mapToResponseDTO(updatedCharge);
    }

    @Override
    public void deleteCharge(Long id) {

        log.info(
                "Deleting lab charge ID: {}",
                id
        );

        LabCharge labCharge =
                labChargeRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Lab charge not found with ID: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Lab charge not found with ID: "
                                            + id
                            );
                        });

        labChargeRepository.delete(labCharge);

        log.info(
                "Lab charge deleted successfully. Charge ID: {}, Charge Number: {}",
                id,
                labCharge.getChargeNumber()
        );
    }

    private void validateAmounts(
            LabChargeRequestDTO request) {

        BigDecimal providerCharge =
                request.getProviderCharge();

        BigDecimal medicareBenefit =
                request.getMedicareBenefit();

        BigDecimal patientAmount =
                request.getPatientAmount();

        if (medicareBenefit != null
                && medicareBenefit.compareTo(providerCharge) > 0) {

            log.warn(
                    "Invalid lab charge amounts: Medicare benefit is greater than provider charge"
            );

            throw new IllegalArgumentException(
                    "Medicare benefit cannot be greater than provider charge"
            );
        }

        if (medicareBenefit != null) {

            BigDecimal expectedPatientAmount =
                    providerCharge.subtract(medicareBenefit);

            if (patientAmount.compareTo(expectedPatientAmount) != 0) {

                log.warn(
                        "Invalid lab charge amounts: patient amount does not match provider charge minus Medicare benefit"
                );

                throw new IllegalArgumentException(
                        "Patient amount must equal provider charge "
                                + "minus Medicare benefit"
                );
            }
        }
    }

    private String generateChargeNumber() {

        long nextNumber =
                labChargeRepository.count() + 1;

        String chargeNumber =
                String.format(
                        "CHG-%06d",
                        nextNumber
                );

        while (labChargeRepository.existsByChargeNumber(
                chargeNumber)) {

            nextNumber++;

            chargeNumber =
                    String.format(
                            "CHG-%06d",
                            nextNumber
                    );
        }

        log.debug(
                "Generated lab charge number: {}",
                chargeNumber
        );

        return chargeNumber;
    }

    private LabChargeResponseDTO mapToResponseDTO(
            LabCharge labCharge) {

        LabOrderItem orderItem =
                labCharge.getLabOrderItem();

        return LabChargeResponseDTO.builder()
                .id(labCharge.getId())
                .chargeNumber(labCharge.getChargeNumber())
                .labOrderItemId(orderItem.getId())
                .labOrderId(
                        orderItem.getLabOrder().getId()
                )
                .orderNumber(
                        orderItem.getLabOrder().getOrderNumber()
                )
                .labTestId(
                        orderItem.getLabTest().getId()
                )
                .testCode(
                        orderItem.getLabTest().getTestCode()
                )
                .testName(
                        orderItem.getLabTest().getTestName()
                )
                .providerCharge(
                        labCharge.getProviderCharge()
                )
                .mbsItemNumber(
                        labCharge.getMbsItemNumber()
                )
                .medicareBenefit(
                        labCharge.getMedicareBenefit()
                )
                .patientAmount(
                        labCharge.getPatientAmount()
                )
                .billingType(
                        labCharge.getBillingType()
                )
                .status(
                        labCharge.getStatus()
                )
                .chargedAt(
                        labCharge.getChargedAt()
                )
                .createdAt(
                        labCharge.getCreatedAt()
                )
                .updatedAt(
                        labCharge.getUpdatedAt()
                )
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalProviderChargeByLabOrderId(
            Long labOrderId) {

        log.debug(
                "Calculating total provider charge for lab order ID: {}",
                labOrderId
        );

        return labChargeRepository
                .getTotalProviderChargeByLabOrderId(labOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalMedicareBenefitByLabOrderId(
            Long labOrderId) {

        log.debug(
                "Calculating total Medicare benefit for lab order ID: {}",
                labOrderId
        );

        return labChargeRepository
                .getTotalMedicareBenefitByLabOrderId(labOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPatientAmountByLabOrderId(
            Long labOrderId) {

        log.debug(
                "Calculating total patient amount for lab order ID: {}",
                labOrderId
        );

        return labChargeRepository
                .getTotalPatientAmountByLabOrderId(labOrderId);
    }
}