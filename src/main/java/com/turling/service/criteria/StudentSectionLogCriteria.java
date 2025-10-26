package com.turling.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.turling.domain.StudentSectionLog} entity. This class is used
 * in {@link com.turling.web.rest.StudentSectionLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-section-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentSectionLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter studentId;

    private LongFilter sectionId;

    private IntegerFilter totalCount;

    private IntegerFilter finishedCount;

    private DoubleFilter correctRate;

    private InstantFilter createDate;

    private InstantFilter updateDate;

    private Boolean distinct;

    public StudentSectionLogCriteria() {}

    public StudentSectionLogCriteria(StudentSectionLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.studentId = other.optionalStudentId().map(LongFilter::copy).orElse(null);
        this.sectionId = other.optionalSectionId().map(LongFilter::copy).orElse(null);
        this.totalCount = other.optionalTotalCount().map(IntegerFilter::copy).orElse(null);
        this.finishedCount = other.optionalFinishedCount().map(IntegerFilter::copy).orElse(null);
        this.correctRate = other.optionalCorrectRate().map(DoubleFilter::copy).orElse(null);
        this.createDate = other.optionalCreateDate().map(InstantFilter::copy).orElse(null);
        this.updateDate = other.optionalUpdateDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StudentSectionLogCriteria copy() {
        return new StudentSectionLogCriteria(this);
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

    public LongFilter getSectionId() {
        return sectionId;
    }

    public Optional<LongFilter> optionalSectionId() {
        return Optional.ofNullable(sectionId);
    }

    public LongFilter sectionId() {
        if (sectionId == null) {
            setSectionId(new LongFilter());
        }
        return sectionId;
    }

    public void setSectionId(LongFilter sectionId) {
        this.sectionId = sectionId;
    }

    public IntegerFilter getTotalCount() {
        return totalCount;
    }

    public Optional<IntegerFilter> optionalTotalCount() {
        return Optional.ofNullable(totalCount);
    }

    public IntegerFilter totalCount() {
        if (totalCount == null) {
            setTotalCount(new IntegerFilter());
        }
        return totalCount;
    }

    public void setTotalCount(IntegerFilter totalCount) {
        this.totalCount = totalCount;
    }

    public IntegerFilter getFinishedCount() {
        return finishedCount;
    }

    public Optional<IntegerFilter> optionalFinishedCount() {
        return Optional.ofNullable(finishedCount);
    }

    public IntegerFilter finishedCount() {
        if (finishedCount == null) {
            setFinishedCount(new IntegerFilter());
        }
        return finishedCount;
    }

    public void setFinishedCount(IntegerFilter finishedCount) {
        this.finishedCount = finishedCount;
    }

    public DoubleFilter getCorrectRate() {
        return correctRate;
    }

    public Optional<DoubleFilter> optionalCorrectRate() {
        return Optional.ofNullable(correctRate);
    }

    public DoubleFilter correctRate() {
        if (correctRate == null) {
            setCorrectRate(new DoubleFilter());
        }
        return correctRate;
    }

    public void setCorrectRate(DoubleFilter correctRate) {
        this.correctRate = correctRate;
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

    public InstantFilter getUpdateDate() {
        return updateDate;
    }

    public Optional<InstantFilter> optionalUpdateDate() {
        return Optional.ofNullable(updateDate);
    }

    public InstantFilter updateDate() {
        if (updateDate == null) {
            setUpdateDate(new InstantFilter());
        }
        return updateDate;
    }

    public void setUpdateDate(InstantFilter updateDate) {
        this.updateDate = updateDate;
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
        final StudentSectionLogCriteria that = (StudentSectionLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(sectionId, that.sectionId) &&
            Objects.equals(totalCount, that.totalCount) &&
            Objects.equals(finishedCount, that.finishedCount) &&
            Objects.equals(correctRate, that.correctRate) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(updateDate, that.updateDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, sectionId, totalCount, finishedCount, correctRate, createDate, updateDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentSectionLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStudentId().map(f -> "studentId=" + f + ", ").orElse("") +
            optionalSectionId().map(f -> "sectionId=" + f + ", ").orElse("") +
            optionalTotalCount().map(f -> "totalCount=" + f + ", ").orElse("") +
            optionalFinishedCount().map(f -> "finishedCount=" + f + ", ").orElse("") +
            optionalCorrectRate().map(f -> "correctRate=" + f + ", ").orElse("") +
            optionalCreateDate().map(f -> "createDate=" + f + ", ").orElse("") +
            optionalUpdateDate().map(f -> "updateDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
