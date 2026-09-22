package com.hospital.hospital_billing_system.pharmacy.service.Impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Medicine;
import com.hospital.hospital_billing_system.pharmacy.entity.MedicineBatch;
import com.hospital.hospital_billing_system.pharmacy.mapper.MedicineBatchMapper;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineBatchRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineRepository;
import com.hospital.hospital_billing_system.pharmacy.service.MedicineBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicineBatchServiceImpl implements MedicineBatchService {

    private final MedicineBatchRepository medicineBatchRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineBatchMapper medicineBatchMapper;

    @Override
    public MedicineBatchResponseDto createMedicineBatch(
            MedicineBatchRequestDto request) {

        log.info(
                "Creating medicine batch. Medicine ID: {}, Batch Number: {}",
                request.getMedicineId(),
                request.getBatchNumber()
        );

        validateBatchDates(request);
        validateQuantity(request);

        Medicine medicine = medicineRepository
                .findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with ID: " + request.getMedicineId()
                ));

        if (medicineBatchRepository
                .existsByMedicineMedicineIdAndBatchNumber(
                        request.getMedicineId(),
                        request.getBatchNumber())) {

            throw new DuplicateResourceException(
                    "Batch number already exists for this medicine: "
                            + request.getBatchNumber()
            );
        }

        MedicineBatch medicineBatch =
                medicineBatchMapper.toEntity(request, medicine);

        MedicineBatch savedBatch =
                medicineBatchRepository.save(medicineBatch);

        log.info(
                "Medicine batch created successfully. Batch ID: {}",
                savedBatch.getBatchId()
        );

        return medicineBatchMapper.toResponseDto(savedBatch);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineBatchResponseDto getMedicineBatchById(Long batchId) {

        log.debug("Fetching medicine batch with ID: {}", batchId);

        MedicineBatch medicineBatch = medicineBatchRepository
                .findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine batch not found with ID: " + batchId
                ));

        return medicineBatchMapper.toResponseDto(medicineBatch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponseDto> getAllMedicineBatches() {

        log.debug("Fetching all medicine batches");

        return medicineBatchRepository.findAll()
                .stream()
                .map(medicineBatchMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponseDto> getBatchesByMedicineId(
            Long medicineId) {

        log.debug(
                "Fetching medicine batches for medicine ID: {}",
                medicineId
        );

        if (!medicineRepository.existsById(medicineId)) {
            throw new ResourceNotFoundException(
                    "Medicine not found with ID: " + medicineId
            );
        }

        return medicineBatchRepository
                .findByMedicineMedicineId(medicineId)
                .stream()
                .map(medicineBatchMapper::toResponseDto)
                .toList();
    }

    @Override
    public MedicineBatchResponseDto updateMedicineBatch(
            Long batchId,
            MedicineBatchRequestDto request) {

        log.info("Updating medicine batch with ID: {}", batchId);

        validateBatchDates(request);
        validateQuantity(request);

        MedicineBatch existingBatch = medicineBatchRepository
                .findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine batch not found with ID: " + batchId
                ));

        Medicine medicine = medicineRepository
                .findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with ID: " + request.getMedicineId()
                ));

        if (medicineBatchRepository
                .existsByMedicineMedicineIdAndBatchNumberAndBatchIdNot(
                        request.getMedicineId(),
                        request.getBatchNumber(),
                        batchId)) {

            throw new DuplicateResourceException(
                    "Batch number already exists for this medicine: "
                            + request.getBatchNumber()
            );
        }

        existingBatch.setMedicine(medicine);
        existingBatch.setBatchNumber(request.getBatchNumber());
        existingBatch.setManufacturingDate(
                request.getManufacturingDate());
        existingBatch.setExpiryDate(request.getExpiryDate());
        existingBatch.setReceivedQuantity(
                request.getReceivedQuantity());
        existingBatch.setQuantity(request.getQuantity());
        existingBatch.setUnitCost(request.getUnitCost());
        existingBatch.setSupplierName(request.getSupplierName());

        MedicineBatch updatedBatch =
                medicineBatchRepository.save(existingBatch);

        log.info(
                "Medicine batch updated successfully. Batch ID: {}",
                batchId
        );

        return medicineBatchMapper.toResponseDto(updatedBatch);
    }

    private void validateBatchDates(
            MedicineBatchRequestDto request) {

        if (request.getExpiryDate()
                .isBefore(request.getManufacturingDate())
                ||
                request.getExpiryDate()
                        .isEqual(request.getManufacturingDate())) {

            throw new IllegalArgumentException(
                    "Expiry date must be after manufacturing date"
            );
        }
    }

    private void validateQuantity(
            MedicineBatchRequestDto request) {

        if (request.getQuantity()
                > request.getReceivedQuantity()) {

            throw new IllegalArgumentException(
                    "Quantity cannot be greater than received quantity"
            );
        }
    }
}