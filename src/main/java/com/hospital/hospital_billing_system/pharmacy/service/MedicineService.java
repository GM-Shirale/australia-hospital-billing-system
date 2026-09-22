package com.hospital.hospital_billing_system.pharmacy.service;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineResponseDto;

import java.util.List;

public interface MedicineService {
    MedicineResponseDto createMedicine(MedicineRequestDto request);


    MedicineResponseDto getMedicineById(Long medicineId);

    List<MedicineResponseDto> getAllMedicines();

    List<MedicineResponseDto> searchMedicines(String search);

    MedicineResponseDto updateMedicine(Long medicineId,MedicineRequestDto request);

    MedicineResponseDto updateMedicineStatus(Long medicineId,Boolean active);



}
