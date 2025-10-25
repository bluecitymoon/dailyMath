package com.turling.web.rest;

import com.turling.service.StudentAnswerLogService;
import com.turling.service.dto.StudentAnswerLogResponseDTO;
import com.turling.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StudentAnswerLogResource} REST controller.
 */
@SpringBootTest
@AutoConfigureWebMvc
class StudentAnswerLogResourceTest {

    @Autowired
    private StudentAnswerLogResource studentAnswerLogResource;

    @MockBean
    private StudentAnswerLogService studentAnswerLogService;

    private MockMvc restStudentAnswerLogMockMvc;

    @BeforeEach
    public void setup() {
        restStudentAnswerLogMockMvc = MockMvcBuilders.standaloneSetup(studentAnswerLogResource).build();
    }

    @Test
    @WithMockUser(username = "test", authorities = {"ROLE_USER"})
    void getStudentAnswerLogByQuestionId_WhenAnswered_ReturnsAnswerLog() throws Exception {
        // Given
        Long questionId = 1L;
        Long studentId = 1L;
        StudentAnswerLogResponseDTO responseDTO = new StudentAnswerLogResponseDTO(
            1L, studentId, questionId, "A", 1, Instant.now(), 10.0, true
        );

        when(studentAnswerLogService.findByStudentIdAndQuestionId(eq(studentId), eq(questionId)))
            .thenReturn(responseDTO);

        // When & Then
        restStudentAnswerLogMockMvc
            .perform(get("/api/student-answer-logs/by-question/{questionId}", questionId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.studentId").value(studentId))
            .andExpect(jsonPath("$.questionId").value(questionId))
            .andExpect(jsonPath("$.answer").value("A"))
            .andExpect(jsonPath("$.correct").value(1))
            .andExpect(jsonPath("$.isAnswered").value(true));
    }

    @Test
    @WithMockUser(username = "test", authorities = {"ROLE_USER"})
    void getStudentAnswerLogByQuestionId_WhenNotAnswered_ReturnsNotAnswered() throws Exception {
        // Given
        Long questionId = 1L;
        Long studentId = 1L;
        StudentAnswerLogResponseDTO responseDTO = new StudentAnswerLogResponseDTO(
            null, studentId, questionId, null, null, null, null, false
        );

        when(studentAnswerLogService.findByStudentIdAndQuestionId(eq(studentId), eq(questionId)))
            .thenReturn(responseDTO);

        // When & Then
        restStudentAnswerLogMockMvc
            .perform(get("/api/student-answer-logs/by-question/{questionId}", questionId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").doesNotExist())
            .andExpect(jsonPath("$.studentId").value(studentId))
            .andExpect(jsonPath("$.questionId").value(questionId))
            .andExpect(jsonPath("$.answer").doesNotExist())
            .andExpect(jsonPath("$.correct").doesNotExist())
            .andExpect(jsonPath("$.isAnswered").value(false));
    }
}
