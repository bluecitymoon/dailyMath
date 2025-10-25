package com.turling.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.turling.domain.StudentAnswerLog} entity. This class is used
 * in {@link com.turling.web.rest.StudentAnswerLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-answer-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentAnswerLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter studentId;

    private LongFilter questionId;

    private StringFilter answer;

    private IntegerFilter correct;

    private InstantFilter createDate;

    private DoubleFilter winPoints;

    private Boolean distinct;

    public StudentAnswerLogCriteria() {}

    public StudentAnswerLogCriteria(StudentAnswerLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.studentId = other.optionalStudentId().map(LongFilter::copy).orElse(null);
        this.questionId = other.optionalQuestionId().map(LongFilter::copy).orElse(null);
        this.answer = other.optionalAnswer().map(StringFilter::copy).orElse(null);
        this.correct = other.optionalCorrect().map(IntegerFilter::copy).orElse(null);
        this.createDate = other.optionalCreateDate().map(InstantFilter::copy).orElse(null);
        this.winPoints = other.optionalWinPoints().map(DoubleFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StudentAnswerLogCriteria copy() {
        return new StudentAnswerLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public Optional<LongFilter> optionalStudentId() {
        return Optional.ofNullable(studentId);
    }

    public LongFilter studentId() {
        if (studentId == null) {
            setStudentId(new LongFilter());
        }
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public Optional<LongFilter> optionalQuestionId() {
        return Optional.ofNullable(questionId);
    }

    public LongFilter questionId() {
        if (questionId == null) {
            setQuestionId(new LongFilter());
        }
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
    }

    public StringFilter getAnswer() {
        return answer;
    }

    public Optional<StringFilter> optionalAnswer() {
        return Optional.ofNullable(answer);
    }

    public StringFilter answer() {
        if (answer == null) {
            setAnswer(new StringFilter());
        }
        return answer;
    }

    public void setAnswer(StringFilter answer) {
        this.answer = answer;
    }

    public IntegerFilter getCorrect() {
        return correct;
    }

    public Optional<IntegerFilter> optionalCorrect() {
        return Optional.ofNullable(correct);
    }

    public IntegerFilter correct() {
        if (correct == null) {
            setCorrect(new IntegerFilter());
        }
        return correct;
    }

    public void setCorrect(IntegerFilter correct) {
        this.correct = correct;
    }

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public Optional<InstantFilter> optionalCreateDate() {
        return Optional.ofNullable(createDate);
    }

    public InstantFilter createDate() {
        if (createDate == null) {
            setCreateDate(new InstantFilter());
        }
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
    }

    public DoubleFilter getWinPoints() {
        return winPoints;
    }

    public Optional<DoubleFilter> optionalWinPoints() {
        return Optional.ofNullable(winPoints);
    }

    public DoubleFilter winPoints() {
        if (winPoints == null) {
            setWinPoints(new DoubleFilter());
        }
        return winPoints;
    }

    public void setWinPoints(DoubleFilter winPoints) {
        this.winPoints = winPoints;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentAnswerLogCriteria that = (StudentAnswerLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(answer, that.answer) &&
            Objects.equals(correct, that.correct) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(winPoints, that.winPoints) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, questionId, answer, correct, createDate, winPoints, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentAnswerLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStudentId().map(f -> "studentId=" + f + ", ").orElse("") +
            optionalQuestionId().map(f -> "questionId=" + f + ", ").orElse("") +
            optionalAnswer().map(f -> "answer=" + f + ", ").orElse("") +
            optionalCorrect().map(f -> "correct=" + f + ", ").orElse("") +
            optionalCreateDate().map(f -> "createDate=" + f + ", ").orElse("") +
            optionalWinPoints().map(f -> "winPoints=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
