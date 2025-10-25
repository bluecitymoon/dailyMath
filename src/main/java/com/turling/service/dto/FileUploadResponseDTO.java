package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for file upload response.
 */
public class FileUploadResponseDTO implements Serializable {

    private String originalFilename;
    private String storedFilename;
    private String folder;
    private String bizType;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private Instant uploadTime;
    private String downloadUrl;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileUploadResponseDTO)) {
            return false;
        }

        FileUploadResponseDTO that = (FileUploadResponseDTO) o;
        return Objects.equals(originalFilename, that.originalFilename) &&
               Objects.equals(storedFilename, that.storedFilename) &&
               Objects.equals(folder, that.folder) &&
               Objects.equals(bizType, that.bizType) &&
               Objects.equals(filePath, that.filePath) &&
               Objects.equals(fileSize, that.fileSize) &&
               Objects.equals(contentType, that.contentType) &&
               Objects.equals(uploadTime, that.uploadTime) &&
               Objects.equals(downloadUrl, that.downloadUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalFilename, storedFilename, folder, bizType, filePath, fileSize, contentType, uploadTime, downloadUrl);
    }

    @Override
    public String toString() {
        return "FileUploadResponseDTO{" +
               "originalFilename='" + originalFilename + '\'' +
               ", storedFilename='" + storedFilename + '\'' +
               ", folder='" + folder + '\'' +
               ", bizType='" + bizType + '\'' +
               ", filePath='" + filePath + '\'' +
               ", fileSize=" + fileSize +
               ", contentType='" + contentType + '\'' +
               ", uploadTime=" + uploadTime +
               ", downloadUrl='" + downloadUrl + '\'' +
               '}';
    }
}
