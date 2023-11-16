package com.app.gamereview.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${image.base-directory}")
    private Path baseLocation;

    public String storeFile(MultipartFile file, String folder) throws IOException {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store an empty file.");
        }

        Path storageLocation = Path.of(baseLocation + "/" + folder);
        // Ensure the target directory exists
        Files.createDirectories(storageLocation);

        // Generate a random and unique filename
        String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("default");
        String fileExtension = "";

        // Check if the originalFilename contains a dot
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < originalFilename.length() - 1) {
            fileExtension = originalFilename.substring(lastDotIndex);
        }

        String newFilename = UUID.randomUUID() + fileExtension;

        // Resolve the target file path
        Path targetFilePath = storageLocation.resolve(newFilename);

        // Check if the filename is unique, regenerate if necessary
        while (Files.exists(targetFilePath)) {
            newFilename = UUID.randomUUID() + fileExtension;
            targetFilePath = storageLocation.resolve(newFilename);
        }

        // Copy the file to the target path, overwriting if it already exists
        Files.copy(file.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilename;
    }
}
