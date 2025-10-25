package com.turling.service.impl;

import com.turling.domain.Question;
import com.turling.domain.QuestionOption;
import com.turling.domain.StudentAnswerLog;
import com.turling.repository.QuestionRepository;
import com.turling.repository.StudentAnswerLogRepository;
import com.turling.service.AnswerService;
import com.turling.service.dto.AnswerQuestionRequestDTO;
import com.turling.service.dto.AnswerQuestionResponseDTO;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for answering questions.
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final StudentAnswerLogRepository studentAnswerLogRepository;
    private final QuestionRepository questionRepository;

    public AnswerServiceImpl(StudentAnswerLogRepository studentAnswerLogRepository, QuestionRepository questionRepository) {
        this.studentAnswerLogRepository = studentAnswerLogRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public AnswerQuestionResponseDTO answerQuestion(Long studentId, AnswerQuestionRequestDTO requestDTO) {
        LOG.debug("Student {} answering question {} with answer: {}", studentId, requestDTO.getQuestionId(), requestDTO.getAnswer());

        // Check if student has already answered this question
        Optional<StudentAnswerLog> existingAnswer = studentAnswerLogRepository.findByStudentIdAndQuestionId(
            studentId,
            requestDTO.getQuestionId()
        );

        if (existingAnswer.isPresent()) {
            LOG.debug("Student {} has already answered question {}", studentId, requestDTO.getQuestionId());
            StudentAnswerLog log = existingAnswer.get();
            return new AnswerQuestionResponseDTO(
                log.getId(),
                log.getStudentId(),
                log.getQuestionId(),
                log.getAnswer(),
                log.getCorrect(),
                log.getCreateDate(),
                log.getWinPoints(),
                true,
                "您已经回答过这道题了"
            );
        }

        // Get question with options
        Question question = questionRepository
            .findOneWithOptions(requestDTO.getQuestionId())
            .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + requestDTO.getQuestionId()));

        // Check answer correctness
        boolean isCorrect = checkAnswer(question, requestDTO.getAnswer());

        // Calculate points
        Double points = isCorrect ? question.getPoints() : 0.0;

        // Create new answer log
        StudentAnswerLog answerLog = new StudentAnswerLog();
        answerLog.setStudentId(studentId);
        answerLog.setQuestionId(requestDTO.getQuestionId());
        answerLog.setAnswer(requestDTO.getAnswer());
        answerLog.setCorrect(isCorrect ? 1 : 0);
        answerLog.setCreateDate(Instant.now());
        answerLog.setWinPoints(points);

        answerLog = studentAnswerLogRepository.save(answerLog);
        LOG.debug("Saved answer log with id: {}", answerLog.getId());

        return new AnswerQuestionResponseDTO(
            answerLog.getId(),
            answerLog.getStudentId(),
            answerLog.getQuestionId(),
            answerLog.getAnswer(),
            answerLog.getCorrect(),
            answerLog.getCreateDate(),
            answerLog.getWinPoints(),
            false,
            isCorrect ? "回答正确！" : "回答错误"
        );
    }

    /**
     * Check if the provided answer is correct.
     *
     * @param question the question
     * @param userAnswer the user's answer
     * @return true if correct, false otherwise
     */
    private boolean checkAnswer(Question question, String userAnswer) {
        Set<QuestionOption> options = question.getOptions();

        if (options != null && !options.isEmpty()) {
            // Multiple choice question - check against options
            LOG.debug("Checking answer for multiple choice question");

            // Get all correct answers (options with isAnswer = true)
            Set<String> correctAnswers = options
                .stream()
                .filter(option -> Boolean.TRUE.equals(option.getIsAnswer()))
                .map(option -> option.getName().trim().toUpperCase())
                .collect(java.util.stream.Collectors.toSet());

            if (correctAnswers.isEmpty()) {
                LOG.warn("Question {} has no correct answer options defined", question.getId());
                return false;
            }

            // Parse user's answer (split by comma and trim)
            Set<String> userAnswers = java.util.Arrays.stream(userAnswer.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toSet());

            // Check if user's answers exactly match all correct answers
            boolean isCorrect = correctAnswers.equals(userAnswers);
            LOG.debug("Correct answers: {}, User answers: {}, Match: {}", correctAnswers, userAnswers, isCorrect);

            return isCorrect;
        } else {
            // Free text question - check against answer field
            LOG.debug("Checking answer for free text question");
            String correctAnswer = question.getAnswer();
            if (correctAnswer == null) {
                LOG.warn("Question {} has no correct answer defined", question.getId());
                return false;
            }
            return correctAnswer.trim().equalsIgnoreCase(userAnswer.trim());
        }
    }
}
