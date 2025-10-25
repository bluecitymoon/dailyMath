package com.turling.web.rest;

import com.turling.security.SecurityUtils;
import com.turling.service.AnswerService;
import com.turling.service.dto.AnswerQuestionRequestDTO;
import com.turling.service.dto.AnswerQuestionResponseDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for answering questions.
 */
@RestController
@RequestMapping("/api/answer")
public class AnswerResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnswerResource.class);

    private final AnswerService answerService;

    public AnswerResource(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * {@code POST  /answer} : Submit an answer to a question.
     *
     * @param requestDTO the answer request DTO containing question ID and answer
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answer response DTO
     */
    @PostMapping("")
    public ResponseEntity<AnswerQuestionResponseDTO> answerQuestion(@Valid @RequestBody AnswerQuestionRequestDTO requestDTO) {
        LOG.debug("REST request to answer question : {}", requestDTO);

        // Get current user ID from JWT token
        Long studentId = SecurityUtils.getCurrentUserId().orElseThrow(() -> new IllegalStateException("Current user not found"));

        AnswerQuestionResponseDTO response = answerService.answerQuestion(studentId, requestDTO);

        return ResponseEntity.ok(response);
    }
}
