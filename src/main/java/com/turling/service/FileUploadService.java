package com.turling.service;

import com.turling.service.dto.FileUploadRequestDTO;
import com.turling.service.dto.FileUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for file upload operations.
 */
public interface FileUploadService {

    /**
     * Upload a file with custom folder, filename, and business type.
     *
     * @param file the file to upload
     * @param requestDTO the upload request containing folder, filename, and bizType
     * @return the upload response with file details
     */
    FileUploadResponseDTO uploadFile(MultipartFile file, FileUploadRequestDTO requestDTO);

    /**
     * Get file download URL by file path.
     *
     * @param filePath the file path
     * @return the download URL
     */
    String getFileDownloadUrl(String filePath);

    /**
     * Delete a file by file path.
     *
     * @param filePath the file path
     * @return true if file was deleted successfully
     */
    boolean deleteFile(String filePath);
}
