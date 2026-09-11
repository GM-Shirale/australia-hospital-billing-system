package com.hospital.hospital_billing_system.pharmacy.service.Impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStockRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStockResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Medicine;
import com.hospital.hospital_billing_system.pharmacy.entity.MedicineBatch;
import com.hospital.hospital_billing_system.pharmacy.entity.MedicineStock;
import com.hospital.hospital_billing_system.pharmacy.mapper.MedicineStockMapper;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineBatchRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineStockRepository;
import com.hospital.hospital_billing_system.pharmacy.service.MedicineStockService;
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
public class MedicineStockServiceImpl implements MedicineStockService {

    private final MedicineStockRepository medicineStockRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineBatchRepository medicineBatchRepository;
    private final MedicineStockMapper medicineStockMapper;

    @Override
    public MedicineStockResponseDto createStock(
            MedicineStockRequestDto requestDto) {

        log.info(
                "Creating medicine stock for medicineId={} and batchId={}",
                requestDto.getMedicineId(),
                requestDto.getBatchId()
        );

        Medicine medicine = medicineRepository
                .findById(requestDto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with ID: "
                                + requestDto.getMedicineId()
                ));

        MedicineBatch batch = medicineBatchRepository
                .findById(requestDto.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine batch not found with ID: "
                                + requestDto.getBatchId()
                ));

        validateMedicine(medicine);
        validateBatch(batch, medicine);

        if (medicineStockRepository
                .existsByMedicineMedicineIdAndBatchBatchId(
                        requestDto.getMedicineId(),
                        requestDto.getBatchId())) {

            throw new DuplicateResourceException(
                    "Stock already exists for this medicine and batch"
            );
        }

        MedicineStock stock = MedicineStock.builder()
                .medicine(medicine)
                .batch(batch)
                .quantityReceived(requestDto.getQuantityReceived())
                .quantityAvailable(requestDto.getQuantityReceived())
                .quantityDispensed(0)
                .active(true)
                .build();

        MedicineStock savedStock =
                medicineStockRepository.save(stock);

        log.info(
                "Medicine stock created successfully with stockId={}",
                savedStock.getStockId()
        );

        return medicineStockMapper.toResponseDto(savedStock);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineStockResponseDto getStockById(Long stockId) {

        MedicineStock stock = medicineStockRepository
                .findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine stock not found with ID: " + stockId
                ));

        return medicineStockMapper.toResponseDto(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineStockResponseDto> getStockByMedicineId(
            Long medicineId) {

        if (!medicineRepository.existsById(medicineId)) {
            throw new ResourceNotFoundException(
                    "Medicine not found with ID: " + medicineId
            );
        }

        return medicineStockRepository
                .findByMedicineMedicineId(medicineId)
                .stream()
                .map(medicineStockMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineStockResponseDto> getStockByBatchId(
            Long batchId) {

        if (!medicineBatchRepository.existsById(batchId)) {
            throw new ResourceNotFoundException(
                    "Medicine batch not found with ID: " + batchId
            );
        }

        return medicineStockRepository
                .findByBatchBatchId(batchId)
                .stream()
                .map(medicineStockMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineStockResponseDto> getActiveStock() {

        return medicineStockRepository
                .findByActiveTrue()
                .stream()
                .map(medicineStockMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineStockResponseDto> getLowStock() {

        List<MedicineStock> stocks =
                medicineStockRepository.findByActiveTrue();

        return stocks.stream()
                .filter(stock ->
                        stock.getMedicine().getReorderLevel() != null
                                && stock.getQuantityAvailable() != null
                                && stock.getQuantityAvailable()
                                <= stock.getMedicine().getReorderLevel()
                )
                .map(medicineStockMapper::toResponseDto)
                .toList();
    }

    @Override
    public MedicineStockResponseDto updateStock(
            Long stockId,
            MedicineStockRequestDto requestDto) {

        MedicineStock existingStock =
                medicineStockRepository.findById(stockId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Medicine stock not found with ID: "
                                        + stockId
                        ));

        Medicine medicine = medicineRepository
                .findById(requestDto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with ID: "
                                + requestDto.getMedicineId()
                ));

        MedicineBatch batch = medicineBatchRepository
                .findById(requestDto.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine batch not found with ID: "
                                + requestDto.getBatchId()
                ));

        validateMedicine(medicine);
        validateBatch(batch, medicine);

        boolean medicineChanged =
                !existingStock.getMedicine()
                        .getMedicineId()
                        .equals(requestDto.getMedicineId());

        boolean batchChanged =
                !existingStock.getBatch()
                        .getBatchId()
                        .equals(requestDto.getBatchId());

        if ((medicineChanged || batchChanged)
                && medicineStockRepository
                .existsByMedicineMedicineIdAndBatchBatchId(
                        requestDto.getMedicineId(),
                        requestDto.getBatchId())) {

            throw new DuplicateResourceException(
                    "Stock already exists for this medicine and batch"
            );
        }

        Integer quantityDispensed =
                existingStock.getQuantityDispensed() == null
                        ? 0
                        : existingStock.getQuantityDispensed();

        if (requestDto.getQuantityReceived() < quantityDispensed) {
            throw new IllegalArgumentException(
                    "Quantity received cannot be less than "
                            + "quantity already dispensed: "
                            + quantityDispensed
            );
        }

        existingStock.setMedicine(medicine);
        existingStock.setBatch(batch);

        existingStock.setQuantityReceived(
                requestDto.getQuantityReceived()
        );

        existingStock.setQuantityDispensed(
                quantityDispensed
        );

        existingStock.setQuantityAvailable(
                requestDto.getQuantityReceived()
                        - quantityDispensed
        );

        MedicineStock updatedStock =
                medicineStockRepository.save(existingStock);

        log.info(
                "Medicine stock updated successfully with stockId={}",
                stockId
        );

        return medicineStockMapper.toResponseDto(updatedStock);
    }

    @Override
    public void deleteStock(Long stockId) {

        MedicineStock stock =
                medicineStockRepository.findById(stockId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Medicine stock not found with ID: "
                                        + stockId
                        ));

        if (stock.getQuantityDispensed() != null
                && stock.getQuantityDispensed() > 0) {

            throw new IllegalStateException(
                    "Stock cannot be deleted because medicine "
                            + "has already been dispensed"
            );
        }

        stock.setActive(false);

        medicineStockRepository.save(stock);

        log.info(
                "Medicine stock deactivated successfully with stockId={}",
                stockId
        );
    }

    private void validateMedicine(Medicine medicine) {

        if (!Boolean.TRUE.equals(medicine.getActive())) {
            throw new IllegalStateException(
                    "Medicine is inactive and cannot be stocked"
            );
        }
    }

    private void validateBatch(
            MedicineBatch batch,
            Medicine medicine) {

        if (!batch.getMedicine()
                .getMedicineId()
                .equals(medicine.getMedicineId())) {

            throw new IllegalArgumentException(
                    "Medicine batch does not belong to the selected medicine"
            );
        }

        if (batch.getExpiryDate() == null) {
            throw new IllegalStateException(
                    "Medicine batch expiry date is required"
            );
        }

        if (batch.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException(
                    "Cannot create stock for an expired medicine batch"
            );
        }
    }
}