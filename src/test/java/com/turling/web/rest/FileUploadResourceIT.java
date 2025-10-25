package com.turling.web.rest;

import com.turling.IntegrationTest;
import com.turling.config.ApplicationProperties;
import com.turling.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the FileUploadResource REST controller.
 */
@AutoConfigureWebMvc
@IntegrationTest
class FileUploadResourceIT {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationProperties applicationProperties;

    private MockMvc restFileUploadMockMvc;

    @BeforeEach
    public void setup() {
        FileUploadResource fileUploadResource = new FileUploadResource(fileUploadService);
        this.restFileUploadMockMvc = MockMvcBuilders.standaloneSetup(fileUploadResource).build();
    }

    @Test
    void testUploadFile() throws Exception {
        // Create a mock file
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Hello World!".getBytes()
        );

        // Test file upload
        restFileUploadMockMvc
            .perform(
                multipart("/api/files/upload")
                    .file(file)
                    .param("folder", "test-folder")
                    .param("filename", "my-test-file")
                    .param("bizType", "document")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.originalFilename").value("test.txt"))
            .andExpect(jsonPath("$.folder").value("test-folder"))
            .andExpect(jsonPath("$.bizType").value("document"))
            .andExpect(jsonPath("$.fileSize").value(12L))
            .andExpect(jsonPath("$.contentType").value("text/plain"));
    }

    @Test
    void testUploadEmptyFile() throws Exception {
        // Create an empty mock file
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "empty.txt",
            "text/plain",
            new byte[0]
        );

        // Test empty file upload should fail
        restFileUploadMockMvc
            .perform(
                multipart("/api/files/upload")
                    .file(file)
                    .param("folder", "test-folder")
                    .param("filename", "empty-file")
                    .param("bizType", "document")
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void testGetDownloadUrl() throws Exception {
        String testFilePath = "test-folder/document/test-file_12345678.txt";
        
        restFileUploadMockMvc
            .perform(get("/api/files/download-url")
                .param("filePath", testFilePath))
            .andExpect(status().isOk())
            .andExpect(content().string(applicationProperties.getFileStorage().getBaseUrl() + "/" + testFilePath));
    }
}
