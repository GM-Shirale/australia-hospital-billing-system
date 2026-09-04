package com.hospital.hospital_billing_system.patient.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveFile(Long patientId, MultipartFile fIle);

    void deleteFile(String filePAth);

}
