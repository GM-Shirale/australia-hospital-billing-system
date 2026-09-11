package com.hospital.hospital_billing_system.pharmacy.service.Impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionItemRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionItemResponseDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Medicine;
import com.hospital.hospital_billing_system.pharmacy.entity.Prescription;
import com.hospital.hospital_billing_system.pharmacy.entity.PrescriptionItem;
import com.hospital.hospital_billing_system.pharmacy.mapper.PrescriptionItemMapper;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.PrescriptionItemRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.PrescriptionRepository;
import com.hospital.hospital_billing_system.pharmacy.service.PrescriptionItemService;
import com.hospital.hospital_billing_system.pharmacy.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrescriptionItemServiceImpl implements PrescriptionItemService {

    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionItemMapper prescriptionItemMapper;


    @Override
    public PrescriptionItemResponseDto createPrescriptionItem(PrescriptionItemRequestDto requestDto) {
        log.info( "Creating prescription item for prescriptionId={} and medicineId={}",
                requestDto.getPrescriptionId(),
                requestDto.getMedicineId());

        Prescription prescription = prescriptionRepository
                .findById(requestDto.getPrescriptionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with ID: "
                                + requestDto.getPrescriptionId()
                ));

        Medicine medicine = medicineRepository
                .findById(requestDto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with ID: "
                                + requestDto.getMedicineId()
                ));

        validateMedicine(medicine);

        if (prescriptionItemRepository
                .existsByPrescriptionPrescriptionIdAndMedicineMedicineId(
                        requestDto.getPrescriptionId(),
                        requestDto.getMedicineId())) {

            throw new DuplicateResourceException(
                    "Medicine is already added to this prescription"
            );
        }

        PrescriptionItem prescriptionItem = PrescriptionItem.builder()
                .prescription(prescription)
                .medicine(medicine)
                .dosage(requestDto.getDosage())
                .frequency(requestDto.getFrequency())
                .duration(requestDto.getDuration())
                .durationUnit(requestDto.getDurationUnit())
                .quantity(requestDto.getQuantity())
                .instructions(requestDto.getInstructions())
                .build();

        PrescriptionItem savedItem =
                prescriptionItemRepository.save(prescriptionItem);


        log.info(
                "Prescription item created successfully with ID={}",
                savedItem.getPrescriptionItemId()
        );
        return prescriptionItemMapper.toResponseDto(savedItem);
    }

    private void validateMedicine(Medicine medicine) {
        if (!Boolean.TRUE.equals(medicine.getActive())) {
            throw new IllegalStateException(
                    "Medicine is inactive and cannot be prescribed"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionItemResponseDto getPrescriptionItemById(
            Long prescriptionItemId) {

        PrescriptionItem prescriptionItem =
                prescriptionItemRepository.findById(prescriptionItemId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Prescription item not found with ID: "
                                        + prescriptionItemId
                        ));

        return prescriptionItemMapper.toResponseDto(prescriptionItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionItemResponseDto> getItemsByPrescriptionId(
            Long prescriptionId) {

        if (!prescriptionRepository.existsById(prescriptionId)) {
            throw new ResourceNotFoundException(
                    "Prescription not found with ID: " + prescriptionId
            );
        }
        return prescriptionItemRepository
                .findByPrescriptionPrescriptionId(prescriptionId)
                .stream()
                .map(prescriptionItemMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PrescriptionItemResponseDto> getItemsByMedicineId(
            Long medicineId) {

        if (!medicineRepository.existsById(medicineId)) {
            throw new ResourceNotFoundException(
                    "Medicine not found with ID: " + medicineId
            );
        }

        return prescriptionItemRepository
                .findByMedicineMedicineId(medicineId)
                .stream()
                .map(prescriptionItemMapper::toResponseDto)
                .toList();
    }

    @Override
    public PrescriptionItemResponseDto updatePrescriptionItem(Long prescriptionItemId, PrescriptionItemRequestDto requestDto) {
        PrescriptionItem existingItem =
                prescriptionItemRepository.findById(prescriptionItemId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Prescription item not found with ID: "
                                        + prescriptionItemId
                        ));
        Prescription prescription = prescriptionRepository
                .findById(requestDto.getPrescriptionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with ID: "
                                + requestDto.getPrescriptionId()
                ));

        Medicine medicine = medicineRepository
                .findById(requestDto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with ID: "
                                + requestDto.getMedicineId()
                ));
        validateMedicine(medicine);

        boolean medicineChanged =
                !existingItem.getMedicine()
                        .getMedicineId()
                        .equals(requestDto.getMedicineId());

        boolean prescriptionChanged =
                !existingItem.getPrescription()
                        .getPrescriptionId()
                        .equals(requestDto.getPrescriptionId());

        if ((medicineChanged || prescriptionChanged)
                && prescriptionItemRepository
                .existsByPrescriptionPrescriptionIdAndMedicineMedicineId(
                        requestDto.getPrescriptionId(),
                        requestDto.getMedicineId())) {

            Integer dispensedQuantity =
                    existingItem.getDispensedQuantity() == null
                            ? 0
                            : existingItem.getDispensedQuantity();

            throw new DuplicateResourceException(
                    "Medicine is already added to this prescription"
            );
        }

        Integer dispensedQuantity =
                existingItem.getDispensedQuantity() == null
                        ? 0
                        : existingItem.getDispensedQuantity();

        if (requestDto.getQuantity() < dispensedQuantity) {
            throw new IllegalArgumentException(
                    "Quantity cannot be less than already dispensed quantity: "
                            + dispensedQuantity
            );
        }
        existingItem.setPrescription(prescription);
        existingItem.setMedicine(medicine);
        existingItem.setDosage(requestDto.getDosage());
        existingItem.setFrequency(requestDto.getFrequency());
        existingItem.setDuration(requestDto.getDuration());
        existingItem.setDurationUnit(requestDto.getDurationUnit());
        existingItem.setQuantity(requestDto.getQuantity());
        existingItem.setInstructions(requestDto.getInstructions());

        existingItem.setRemainingQuantity(
                requestDto.getQuantity() - dispensedQuantity
        );
        PrescriptionItem updatedItem =
                prescriptionItemRepository.save(existingItem);

        log.info(
                "Prescription item updated successfully with ID={}",
                prescriptionItemId
        );

        return prescriptionItemMapper.toResponseDto(updatedItem);
    }

    @Override
    public void deletePrescriptionItem(Long prescriptionItemId) {

        PrescriptionItem prescriptionItem =
                prescriptionItemRepository.findById(prescriptionItemId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Prescription item not found with ID: "
                                        + prescriptionItemId
                        ));
        if (prescriptionItem.getDispensedQuantity() != null
                && prescriptionItem.getDispensedQuantity() > 0) {

            throw new IllegalStateException(
                    "Prescription item cannot be deleted because medicine "
                            + "has already been dispensed"
            );
        }
        prescriptionItemRepository.delete(prescriptionItem);

        log.info(
                "Prescription item deleted successfully with ID={}",
                prescriptionItemId
        );
    }
}
