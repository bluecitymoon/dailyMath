package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.StudentAnswerLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentAnswerLogDTO implements Serializable {

    private Long id;

    private Long studentId;

    private Long questionId;

    private String answer;

    private Integer correct;

    private Instant createDate;

    private Double winPoints;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentAnswerLogDTO)) {
            return false;
        }

        StudentAnswerLogDTO studentAnswerLogDTO = (StudentAnswerLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentAnswerLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentAnswerLogDTO{" +
            "id=" + getId() +
            ", studentId=" + getStudentId() +
            ", questionId=" + getQuestionId() +
            ", answer='" + getAnswer() + "'" +
            ", correct=" + getCorrect() +
            ", createDate='" + getCreateDate() + "'" +
            ", winPoints=" + getWinPoints() +
            "}";
    }
}
