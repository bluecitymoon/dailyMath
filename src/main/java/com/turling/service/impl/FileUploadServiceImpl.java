package com.turling.service.impl;

import com.turling.config.ApplicationProperties;
import com.turling.service.FileUploadService;
import com.turling.service.dto.FileUploadRequestDTO;
import com.turling.service.dto.FileUploadResponseDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for file upload operations.
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger LOG = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    public FileUploadServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public FileUploadResponseDTO uploadFile(MultipartFile file, FileUploadRequestDTO requestDTO) {
        LOG.debug("Request to upload file: {}", file.getOriginalFilename());

        // Validate file
        validateFile(file);

        // Create directory structure
        String folderPath = createFolderStructure(requestDTO.getFolder(), requestDTO.getBizType());

        // Generate unique filename
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String storedFilename = generateUniqueFilename(requestDTO.getFilename(), fileExtension);

        // Create full file path
        Path targetPath = Paths.get(folderPath, storedFilename);

        try {
            // Ensure directory exists
            Files.createDirectories(targetPath.getParent());

            // Save file
            Files.copy(file.getInputStream(), targetPath);

            // Create response
            FileUploadResponseDTO response = new FileUploadResponseDTO();
            response.setOriginalFilename(file.getOriginalFilename());
            response.setStoredFilename(storedFilename);
            response.setFolder(requestDTO.getFolder());
            response.setBizType(requestDTO.getBizType());
            response.setFilePath(targetPath.toString());
            response.setFileSize(file.getSize());
            response.setContentType(file.getContentType());
            response.setUploadTime(Instant.now());
            response.setDownloadUrl(generatePreviewUrl(targetPath.toString()));

            LOG.debug("File uploaded successfully: {}", targetPath);
            return response;
        } catch (IOException e) {
            LOG.error("Failed to upload file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public String getFileDownloadUrl(String filePath) {
        return generateDownloadUrl(filePath);
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(path);
            LOG.debug("File deletion result for {}: {}", filePath, deleted);
            return deleted;
        } catch (IOException e) {
            LOG.error("Failed to delete file: {}", e.getMessage(), e);
            return false;
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > applicationProperties.getFileStorage().getMaxFileSize()) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String[] allowedExtensions = applicationProperties.getFileStorage().getAllowedExtensions();

        if (!Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("File type not allowed. Allowed types: " + Arrays.toString(allowedExtensions));
        }
    }

    private String createFolderStructure(String folder, String bizType) {
        String basePath = applicationProperties.getFileStorage().getUploadPath();
        return Paths.get(basePath, folder, bizType).toString();
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private String generateUniqueFilename(String originalFilename, String extension) {
        String nameWithoutExtension = originalFilename;
        if (originalFilename.contains(".")) {
            nameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return String.format("%s_%s.%s", nameWithoutExtension, uniqueId, extension);
    }

    private String generateDownloadUrl(String filePath) {
        String baseUrl = applicationProperties.getFileStorage().getBaseUrl();
        String relativePath = filePath.replace(applicationProperties.getFileStorage().getUploadPath(), "");
        return baseUrl + relativePath.replace("\\", "/");
    }

    private String generatePreviewUrl(String filePath) {
        String baseUrl = applicationProperties.getFileStorage().getBaseUrl();
        String relativePath = filePath.replace(applicationProperties.getFileStorage().getUploadPath(), "");
        return baseUrl + "/view?filePath=" + relativePath.replace("\\", "/");
    }
}
