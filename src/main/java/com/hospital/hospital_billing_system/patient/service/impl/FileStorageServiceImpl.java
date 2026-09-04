package com.hospital.hospital_billing_system.patient.service.impl;


import com.hospital.hospital_billing_system.patient.service.FileStorageService;
import com.hospital.hospital_billing_system.patient.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    private static final Logger log =
            LoggerFactory.getLogger(FileStorageServiceImpl.class);

    // folder where patient documents will be stored
    private static final String UPLOAD_DIR = "uploads/patients";

    @Override
    public String saveFile(Long patientId, MultipartFile file) {

        // check file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        // get original file name
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        // get file extension
        String extension = getFileExtension(originalFileName);

        // allow only PDF, JPG, JPEG and PNG
        if (!extension.equalsIgnoreCase("pdf")
                && !extension.equalsIgnoreCase("jpg")
                && !extension.equalsIgnoreCase("jpeg")
                && !extension.equalsIgnoreCase("png")) {

            throw new IllegalArgumentException(
                    "Only PDF, JPG, JPEG and PNG files are allowed"
            );
        }

        try {

            // create patient specific folder
            Path patientDirectory = Paths.get(
                    UPLOAD_DIR,
                    String.valueOf(patientId)
            );

            Files.createDirectories(patientDirectory);

            // create unique file name
            String fileName = UUID.randomUUID()
                    + "." + extension;

            Path filePath = patientDirectory.resolve(fileName);

            // save file
            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            log.info(
                    "File saved successfully for patient id: {}",
                    patientId
            );

            return filePath.toString();

        } catch (IOException exception) {

            log.error(
                    "Failed to save file for patient id: {}",
                    patientId,
                    exception
            );

            throw new RuntimeException(
                    "Failed to save patient document"
            );
        }
    }

    @Override
    public void deleteFile(String filePath) {

        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {

            Path path = Paths.get(filePath);

            // delete file if it exists
            Files.deleteIfExists(path);

            log.info("File deleted successfully: {}", filePath);

        } catch (IOException exception) {

            log.error(
                    "Failed to delete file: {}",
                    filePath,
                    exception
            );
        }
    }

    // get extension from file name
    private String getFileExtension(String fileName) {

        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1);
    }
}
