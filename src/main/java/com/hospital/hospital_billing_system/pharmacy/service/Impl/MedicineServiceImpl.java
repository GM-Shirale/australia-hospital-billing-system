package com.hospital.hospital_billing_system.pharmacy.service.Impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Medicine;
import com.hospital.hospital_billing_system.pharmacy.mapper.MedicineMapper;
import com.hospital.hospital_billing_system.pharmacy.repo.MedicineRepository;
import com.hospital.hospital_billing_system.pharmacy.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public MedicineResponseDto createMedicine(MedicineRequestDto request) {
    log.info("Creating medicine with code: {}",
            request.getMedicineCode());

    if (medicineRepository.existsByMedicineCode(
            request.getMedicineCode()
    )){
        log.warn(
                "Medicine creation failed. Duplicate medicne code: {}",
                request.getMedicineCode()
        );

        throw new DuplicateResourceException(
                "Medicine already exists with code: "
                +request.getMedicineCode()
        );
    }

        Medicine medicine=medicineMapper.toEntity(request);

    Medicine savedMedicine=medicineRepository.save(medicine);
    log.info(
            "Medicine created successfully. Medicine ID: {}, Code{}" ,
        savedMedicine.getMedicineId(),
        savedMedicine.getMedicineCode()
    );
    return medicineMapper.toResponseDto(savedMedicine);

    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponseDto getMedicineById(Long medicineId) {

        log.debug("Fetching medicine with ID: {}", medicineId);

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> {

                    log.warn(
                            "Medicine not found with ID: {}",
                            medicineId
                    );

                    return new ResourceNotFoundException(
                            "Medicine not found with ID: " + medicineId
                    );
                });

        return medicineMapper.toResponseDto(medicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponseDto> getAllMedicines() {

        log.debug("Fetching all medicines");

        List<MedicineResponseDto> medicines=medicineRepository
                .findAll()
                .stream()
                .map(medicineMapper::toResponseDto)
                .toList();

        log.debug(
                "Successfully fetched {} medicine",
                medicines.size()
        );
        return medicines;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponseDto> searchMedicines(String search) {
        if (search==null || search.trim().isEmpty()){
            log.debug(
                    "Medicine search keyword is empty.Fetching all medicines"
            );
            return getAllMedicines();
        }
        String keyword=search.trim();

        log.debug(
                "Searching medicines with keyword: {}",
                keyword
        );

        List<MedicineResponseDto> medicines=medicineRepository
                .findByMedicineCodeContainingIgnoreCaseOrGenericNameContainingIgnoreCaseOrBrandNameContainingIgnoreCase(keyword,keyword,keyword)
                .stream()
                .map(medicineMapper::toResponseDto)
                .toList();

        log.debug(
                "Medicine search commpleted keyword: {}, results: {}",
                keyword,
                medicines.size()
        );
        return medicines;
    }

    @Override
    public MedicineResponseDto updateMedicine(Long medicineId, MedicineRequestDto request) {

        log.info("Updating medicine with ID: {}",
                medicineId);

        Medicine medicine=medicineRepository.findById(medicineId)
                .orElseThrow(()->{
                    log.warn("Medicine update failed. Medicine not found with ID: {}",
                            medicineId);

                    return new ResourceNotFoundException(
                            "Medicine not found with ID: "+medicineId
                    );
                });
  if (medicineRepository.existsByMedicineCodeAndMedicineIdNot(
          request.getMedicineCode(), medicineId
  )){
      log.warn("Medicine update failed. Duplicate medicine code: {}",
              request.getMedicineCode()
      );
      throw new DuplicateResourceException(
              "Medicine already exists with code: "
              +request.getMedicineCode()
      );
  }

        medicine.setMedicineCode(request.getMedicineCode());
        medicine.setGenericName(request.getGenericName());
        medicine.setBrandName(request.getBrandName());
        medicine.setStrength(request.getStrength());
        medicine.setDosageForm(request.getDosageForm());
        medicine.setRoute(request.getMedicineRoute());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setPbsItemCode(request.getPbsItemCode());
        medicine.setPrescriptionRequired(
                request.getPrescriptionRequired()
        );
        medicine.setUnitPrice(request.getUnitPrice());
        medicine.setReorderLevel(request.getReorderLevel());
        medicine.setActive(request.getActive());

        Medicine updatedMedicine =
                medicineRepository.save(medicine);

        log.info(
                "Medicine updated successfully. Medicine ID: {}",
                updatedMedicine.getMedicineId()
        );

        return medicineMapper.toResponseDto(updatedMedicine);


    }

    @Override
    public MedicineResponseDto updateMedicineStatus(Long medicineId, Boolean active) {

        log.info("Updating medicine status",medicineId,active);

        Medicine medicine=medicineRepository.findById(medicineId)
                .orElseThrow(()->{
                    log.warn("\"Medicine status update failed. Medicine not found with ID: {}",
                                                       medicineId);

        return new ResourceNotFoundException(
                "Medicine not found with ID: "+medicineId
        );
                });
medicine.setActive(active);

Medicine updatedMedicine=
        medicineRepository.save(medicine);

log.info(
        "Medicine status updated  successfully.",
        updatedMedicine.getMedicineId(),
        updatedMedicine.getMedicineCode()
);


        return null;
    }
}
