package com.turling.web.rest;

import com.turling.service.FileUploadService;
import com.turling.service.dto.FileUploadRequestDTO;
import com.turling.service.dto.FileUploadResponseDTO;
import com.turling.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for file upload operations.
 */
@RestController
@RequestMapping("/api/files")
public class FileUploadResource {

    private static final Logger LOG = LoggerFactory.getLogger(FileUploadResource.class);

    private static final String ENTITY_NAME = "fileUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileUploadService fileUploadService;

    public FileUploadResource(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * {@code POST  /files/upload} : Upload a file with custom parameters.
     *
     * @param file the file to upload
     * @param folder the folder to store the file
     * @param filename the custom filename
     * @param bizType the business type
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the file upload response
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDTO> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("folder") String folder,
        @RequestParam("filename") String filename,
        @RequestParam("bizType") String bizType
    ) {
        LOG.debug("REST request to upload file: {} to folder: {} with filename: {} and bizType: {}", 
                 file.getOriginalFilename(), folder, filename, bizType);

        if (file.isEmpty()) {
            throw new BadRequestAlertException("File cannot be empty", ENTITY_NAME, "fileempty");
        }

        FileUploadRequestDTO requestDTO = new FileUploadRequestDTO();
        requestDTO.setFolder(folder);
        requestDTO.setFilename(filename);
        requestDTO.setBizType(bizType);

        try {
            FileUploadResponseDTO response = fileUploadService.uploadFile(file, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationerror");
        } catch (Exception e) {
            LOG.error("Error uploading file", e);
            throw new BadRequestAlertException("File upload failed", ENTITY_NAME, "uploadfailed");
        }
    }

    /**
     * {@code POST  /files/upload-with-dto} : Upload a file using DTO for parameters.
     *
     * @param file the file to upload
     * @param requestDTO the upload request containing folder, filename, and bizType
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the file upload response
     */
    @PostMapping(value = "/upload-with-dto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDTO> uploadFileWithDto(
        @RequestParam("file") MultipartFile file,
        @Valid @ModelAttribute FileUploadRequestDTO requestDTO
    ) {
        LOG.debug("REST request to upload file with DTO: {}", requestDTO);

        if (file.isEmpty()) {
            throw new BadRequestAlertException("File cannot be empty", ENTITY_NAME, "fileempty");
        }

        try {
            FileUploadResponseDTO response = fileUploadService.uploadFile(file, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationerror");
        } catch (Exception e) {
            LOG.error("Error uploading file", e);
            throw new BadRequestAlertException("File upload failed", ENTITY_NAME, "uploadfailed");
        }
    }

    /**
     * {@code GET  /files/download-url} : Get download URL for a file.
     *
     * @param filePath the file path
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the download URL
     */
    @GetMapping("/download-url")
    public ResponseEntity<String> getDownloadUrl(@RequestParam("filePath") String filePath) {
        LOG.debug("REST request to get download URL for file: {}", filePath);
        
        String downloadUrl = fileUploadService.getFileDownloadUrl(filePath);
        return ResponseEntity.ok(downloadUrl);
    }

    /**
     * {@code DELETE  /files} : Delete a file.
     *
     * @param filePath the file path
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if file was deleted successfully
     */
    @DeleteMapping("")
    public ResponseEntity<Void> deleteFile(@RequestParam("filePath") String filePath) {
        LOG.debug("REST request to delete file: {}", filePath);
        
        boolean deleted = fileUploadService.deleteFile(filePath);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
