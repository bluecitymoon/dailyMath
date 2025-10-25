package com.turling.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A StudentAnswerLog.
 */
@Entity
@Table(name = "student_answer_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentAnswerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "correct")
    private Integer correct;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "win_points")
    private Double winPoints;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentAnswerLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public StudentAnswerLog studentId(Long studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public StudentAnswerLog questionId(Long questionId) {
        this.setQuestionId(questionId);
        return this;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return this.answer;
    }

    public StudentAnswerLog answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getCorrect() {
        return this.correct;
    }

    public StudentAnswerLog correct(Integer correct) {
        this.setCorrect(correct);
        return this;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public StudentAnswerLog createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Double getWinPoints() {
        return this.winPoints;
    }

    public StudentAnswerLog winPoints(Double winPoints) {
        this.setWinPoints(winPoints);
        return this;
    }

    public void setWinPoints(Double winPoints) {
        this.winPoints = winPoints;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentAnswerLog)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentAnswerLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentAnswerLog{" +
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
