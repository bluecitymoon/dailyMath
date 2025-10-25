package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the response of student answer log query.
 */
public class StudentAnswerLogResponseDTO implements Serializable {

    private Long id;
    private Long studentId;
    private Long questionId;
    private String answer;
    private Integer correct;
    private Instant createDate;
    private Double winPoints;
    private Boolean isAnswered;

    public StudentAnswerLogResponseDTO() {}

    public StudentAnswerLogResponseDTO(Long id, Long studentId, Long questionId, String answer, 
                                     Integer correct, Instant createDate, Double winPoints, Boolean isAnswered) {
        this.id = id;
        this.studentId = studentId;
        this.questionId = questionId;
        this.answer = answer;
        this.correct = correct;
        this.createDate = createDate;
        this.winPoints = winPoints;
        this.isAnswered = isAnswered;
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

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    @Override
    public String toString() {
        return "StudentAnswerLogResponseDTO{" +
            "id=" + id +
            ", studentId=" + studentId +
            ", questionId=" + questionId +
            ", answer='" + answer + '\'' +
            ", correct=" + correct +
            ", createDate=" + createDate +
            ", winPoints=" + winPoints +
            ", isAnswered=" + isAnswered +
            '}';
    }
}
