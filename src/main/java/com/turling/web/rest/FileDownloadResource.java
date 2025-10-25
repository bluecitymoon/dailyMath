package com.turling.web.rest;

import com.turling.config.ApplicationProperties;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for file download operations.
 */
@RestController
@RequestMapping("/api/files")
public class FileDownloadResource {

    private static final Logger LOG = LoggerFactory.getLogger(FileDownloadResource.class);

    private final ApplicationProperties applicationProperties;

    public FileDownloadResource(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code GET  /files/download/**} : Download a file by path.
     *
     * @param filePath the file path (everything after /api/files/download/)
     * @return the {@link ResponseEntity} with the file content
     */
    @GetMapping("/download/**")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
        LOG.debug("REST request to download file: {}", filePath);

        try {
            // Construct the full file path
            String fullPath = Paths.get(applicationProperties.getFileStorage().getUploadPath(), filePath).toString();
            File file = new File(fullPath);

            if (!file.exists() || !file.isFile()) {
                LOG.warn("File not found: {}", fullPath);
                return ResponseEntity.notFound().build();
            }

            // Check if file is within the upload directory for security
            Path uploadPath = Paths.get(applicationProperties.getFileStorage().getUploadPath()).toAbsolutePath();
            Path requestedPath = Paths.get(fullPath).toAbsolutePath();

            if (!requestedPath.startsWith(uploadPath)) {
                LOG.warn("Access denied to file outside upload directory: {}", fullPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new FileSystemResource(file);

            // Determine content type
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
        } catch (IOException e) {
            LOG.error("Error downloading file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * {@code GET  /files/view/**} : View a file by path (inline display).
     *
     * @param filePath the file path (everything after /api/files/view/)
     * @return the {@link ResponseEntity} with the file content for inline viewing
     */
    @GetMapping("/view/**")
    public ResponseEntity<Resource> viewFile(@RequestParam String filePath) {
        LOG.debug("REST request to view file: {}", filePath);

        try {
            // Construct the full file path
            String fullPath = Paths.get(applicationProperties.getFileStorage().getUploadPath(), filePath).toString();
            File file = new File(fullPath);

            if (!file.exists() || !file.isFile()) {
                LOG.warn("File not found: {}", fullPath);
                return ResponseEntity.notFound().build();
            }

            // Check if file is within the upload directory for security
            Path uploadPath = Paths.get(applicationProperties.getFileStorage().getUploadPath()).toAbsolutePath();
            Path requestedPath = Paths.get(fullPath).toAbsolutePath();

            if (!requestedPath.startsWith(uploadPath)) {
                LOG.warn("Access denied to file outside upload directory: {}", fullPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new FileSystemResource(file);

            // Determine content type
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .body(resource);
        } catch (IOException e) {
            LOG.error("Error viewing file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
