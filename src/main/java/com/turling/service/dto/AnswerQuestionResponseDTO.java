package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for answer question response
 */
public class AnswerQuestionResponseDTO implements Serializable {

    private Long id;
    private Long studentId;
    private Long questionId;
    private String answer;
    private Integer correct;
    private Instant createDate;
    private Double winPoints;
    private Boolean alreadyAnswered;
    private String message;

    public AnswerQuestionResponseDTO() {}

    public AnswerQuestionResponseDTO(
        Long id,
        Long studentId,
        Long questionId,
        String answer,
        Integer correct,
        Instant createDate,
        Double winPoints,
        Boolean alreadyAnswered,
        String message
    ) {
        this.id = id;
        this.studentId = studentId;
        this.questionId = questionId;
        this.answer = answer;
        this.correct = correct;
        this.createDate = createDate;
        this.winPoints = winPoints;
        this.alreadyAnswered = alreadyAnswered;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Double getWinPoints() {
        return winPoints;
    }

    public void setWinPoints(Double winPoints) {
        this.winPoints = winPoints;
    }

    public Boolean getAlreadyAnswered() {
        return alreadyAnswered;
    }

    public void setAlreadyAnswered(Boolean alreadyAnswered) {
        this.alreadyAnswered = alreadyAnswered;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (
            "AnswerQuestionResponseDTO{" +
            "id=" +
            id +
            ", studentId=" +
            studentId +
            ", questionId=" +
            questionId +
            ", answer='" +
            answer +
            '\'' +
            ", correct=" +
            correct +
            ", createDate=" +
            createDate +
            ", winPoints=" +
            winPoints +
            ", alreadyAnswered=" +
            alreadyAnswered +
            ", message='" +
            message +
            '\'' +
            '}'
        );
    }
}
