package com.hospital.hospital_billing_system.pharmacy.service.Impl;

import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.pharmacy.dto.DispensingRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.DispensingResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Dispensing;
import com.hospital.hospital_billing_system.pharmacy.entity.MedicineStock;
import com.hospital.hospital_billing_system.pharmacy.entity.PrescriptionItem;
import com.hospital.hospital_billing_system.pharmacy.mapper.DispensingMapper;
import com.hospital.hospital_billing_system.pharmacy.repo.DispensingRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineStockRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.PrescriptionItemRepository;
import com.hospital.hospital_billing_system.pharmacy.service.DispensingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DispensingServiceImpl implements DispensingService {

    private final DispensingRepository dispensingRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final DispensingMapper dispensingMapper;

    @Override
    public DispensingResponseDto createDispensing(
            DispensingRequestDto requestDto) {

        log.info(
                "Creating dispensing for prescription item ID: {}",
                requestDto.getPrescriptionItemId()
        );

        PrescriptionItem prescriptionItem =
                prescriptionItemRepository.findById(
                        requestDto.getPrescriptionItemId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prescription item not found with ID: "
                                        + requestDto.getPrescriptionItemId()
                        )
                );

        MedicineStock stock =
                medicineStockRepository.findById(
                        requestDto.getStockId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine stock not found with ID: "
                                        + requestDto.getStockId()
                        )
                );

        /*
         * Validate stock status.
         */
        if (!Boolean.TRUE.equals(stock.getActive())) {
            throw new IllegalStateException(
                    "Medicine stock is inactive"
            );
        }

        /*
         * Validate that the stock belongs to
         * the same medicine prescribed.
         */
        Long prescribedMedicineId =
                prescriptionItem.getMedicine().getMedicineId();

        Long stockMedicineId =
                stock.getMedicine().getMedicineId();

        if (!prescribedMedicineId.equals(stockMedicineId)) {
            throw new IllegalArgumentException(
                    "Selected stock does not belong to the prescribed medicine"
            );
        }

        /*
         * Validate requested quantity.
         */
        Integer quantity = requestDto.getQuantityDispensed();

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Dispensing quantity must be greater than zero"
            );
        }

        /*
         * Validate available stock.
         */
        if (stock.getQuantityAvailable() < quantity) {
            throw new IllegalStateException(
                    "Insufficient medicine stock. Available quantity: "
                            + stock.getQuantityAvailable()
            );
        }

        /*
         * Validate prescription remaining quantity.
         */
        if (prescriptionItem.getRemainingQuantity() < quantity) {
            throw new IllegalStateException(
                    "Dispensing quantity exceeds remaining prescription quantity. "
                            + "Remaining quantity: "
                            + prescriptionItem.getRemainingQuantity()
            );
        }

        /*
         * Update stock.
         */
        stock.setQuantityAvailable(
                stock.getQuantityAvailable() - quantity
        );

        stock.setQuantityDispensed(
                stock.getQuantityDispensed() + quantity
        );

        /*
         * Update prescription item.
         */
        prescriptionItem.setDispensedQuantity(
                prescriptionItem.getDispensedQuantity() + quantity
        );

        prescriptionItem.setRemainingQuantity(
                prescriptionItem.getRemainingQuantity() - quantity
        );

        medicineStockRepository.save(stock);
        prescriptionItemRepository.save(prescriptionItem);

        /*
         * Create dispensing transaction.
         */
        Dispensing dispensing = Dispensing.builder()
                .prescriptionItem(prescriptionItem)
                .stock(stock)
                .quantityDispensed(quantity)
                .dispensedBy(requestDto.getDispensedBy())
                .notes(requestDto.getNotes())
                .build();

        Dispensing savedDispensing =
                dispensingRepository.save(dispensing);

        log.info(
                "Dispensing created successfully. Dispensing ID: {}",
                savedDispensing.getDispensingId()
        );

        return dispensingMapper.toResponseDto(savedDispensing);
    }

    @Override
    @Transactional(readOnly = true)
    public DispensingResponseDto getDispensingById(
            Long dispensingId) {

        Dispensing dispensing =
                dispensingRepository.findById(dispensingId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Dispensing not found with ID: "
                                                + dispensingId
                                )
                        );

        return dispensingMapper.toResponseDto(dispensing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DispensingResponseDto> getDispensingByPrescriptionItemId(
            Long prescriptionItemId) {

        return dispensingRepository
                .findByPrescriptionItemPrescriptionItemId(
                        prescriptionItemId
                )
                .stream()
                .map(dispensingMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DispensingResponseDto> getDispensingByStockId(
            Long stockId) {

        return dispensingRepository
                .findByStockStockId(stockId)
                .stream()
                .map(dispensingMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DispensingResponseDto> getDispensingByPrescriptionId(
            Long prescriptionId) {

        return dispensingRepository
                .findByPrescriptionItemPrescriptionPrescriptionId(
                        prescriptionId
                )
                .stream()
                .map(dispensingMapper::toResponseDto)
                .toList();
    }
}