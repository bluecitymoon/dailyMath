package com.turling.service;

import com.turling.service.dto.AnswerQuestionRequestDTO;
import com.turling.service.dto.AnswerQuestionResponseDTO;

/**
 * Service Interface for answering questions.
 */
public interface AnswerService {
    /**
     * Submit an answer to a question.
     *
     * @param studentId the student ID
     * @param requestDTO the answer request DTO
     * @return the answer response DTO
     */
    AnswerQuestionResponseDTO answerQuestion(Long studentId, AnswerQuestionRequestDTO requestDTO);
}
