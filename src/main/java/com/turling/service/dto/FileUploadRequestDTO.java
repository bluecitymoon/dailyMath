package com.turling.service.dto;

import java.io.Serializable;
import java.util.Objects;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * A DTO for file upload request.
 */
public class FileUploadRequestDTO implements Serializable {

    @NotBlank(message = "Folder cannot be blank")
    private String folder;

    @NotBlank(message = "Filename cannot be blank")
    private String filename;

    @NotBlank(message = "Business type cannot be blank")
    private String bizType;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileUploadRequestDTO)) {
            return false;
        }

        FileUploadRequestDTO that = (FileUploadRequestDTO) o;
        return Objects.equals(folder, that.folder) &&
               Objects.equals(filename, that.filename) &&
               Objects.equals(bizType, that.bizType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folder, filename, bizType);
    }

    @Override
    public String toString() {
        return "FileUploadRequestDTO{" +
               "folder='" + folder + '\'' +
               ", filename='" + filename + '\'' +
               ", bizType='" + bizType + '\'' +
               '}';
    }
}
