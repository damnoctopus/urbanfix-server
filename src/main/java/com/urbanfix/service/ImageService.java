package com.urbanfix.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    public String storeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Path uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            log.error("Unable to create upload directory {}", uploadPath, e);
            throw new RuntimeException("Failed to prepare upload directory", e);
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(filename);
        String stored = UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);
        Path target = uploadPath.resolve(stored);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to store file {}", target, e);
            throw new RuntimeException("Failed to store file", e);
        }

        return target.toString();
    }
}
