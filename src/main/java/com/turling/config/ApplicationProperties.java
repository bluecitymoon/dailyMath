package com.turling.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Daily Math.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();

    private final FileStorage fileStorage = new FileStorage();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class FileStorage {

        private String uploadPath = "uploads";
        private Long maxFileSize = 10485760L; // 10MB
        private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "txt", "xls", "xlsx"};
        private String baseUrl = "http://localhost:8080/api/files";

        public String getUploadPath() {
            return uploadPath;
        }

        public void setUploadPath(String uploadPath) {
            this.uploadPath = uploadPath;
        }

        public Long getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(Long maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public String[] getAllowedExtensions() {
            return allowedExtensions;
        }

        public void setAllowedExtensions(String[] allowedExtensions) {
            this.allowedExtensions = allowedExtensions;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
    // jhipster-needle-application-properties-property-class
}
