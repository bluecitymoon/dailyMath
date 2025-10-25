package com.turling.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.turling.domain.Question} entity. This class is used
 * in {@link com.turling.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter points;

    private StringFilter description;

    private StringFilter solution;

    private StringFilter solutionExternalLink;

    private InstantFilter createDate;

    private InstantFilter updateDate;

    private StringFilter createBy;

    private LongFilter createByUserId;

    private StringFilter answer;

    private LongFilter questionCategoryId;

    private LongFilter typeId;

    private LongFilter gradeId;

    private LongFilter optionsId;

    private Boolean distinct;

    public QuestionCriteria() {}

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.points = other.optionalPoints().map(DoubleFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.solution = other.optionalSolution().map(StringFilter::copy).orElse(null);
        this.solutionExternalLink = other.optionalSolutionExternalLink().map(StringFilter::copy).orElse(null);
        this.createDate = other.optionalCreateDate().map(InstantFilter::copy).orElse(null);
        this.updateDate = other.optionalUpdateDate().map(InstantFilter::copy).orElse(null);
        this.createBy = other.optionalCreateBy().map(StringFilter::copy).orElse(null);
        this.createByUserId = other.optionalCreateByUserId().map(LongFilter::copy).orElse(null);
        this.answer = other.optionalAnswer().map(StringFilter::copy).orElse(null);
        this.questionCategoryId = other.optionalQuestionCategoryId().map(LongFilter::copy).orElse(null);
        this.typeId = other.optionalTypeId().map(LongFilter::copy).orElse(null);
        this.gradeId = other.optionalGradeId().map(LongFilter::copy).orElse(null);
        this.optionsId = other.optionalOptionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
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

    public DoubleFilter getPoints() {
        return points;
    }

    public Optional<DoubleFilter> optionalPoints() {
        return Optional.ofNullable(points);
    }

    public DoubleFilter points() {
        if (points == null) {
            setPoints(new DoubleFilter());
        }
        return points;
    }

    public void setPoints(DoubleFilter points) {
        this.points = points;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getSolution() {
        return solution;
    }

    public Optional<StringFilter> optionalSolution() {
        return Optional.ofNullable(solution);
    }

    public StringFilter solution() {
        if (solution == null) {
            setSolution(new StringFilter());
        }
        return solution;
    }

    public void setSolution(StringFilter solution) {
        this.solution = solution;
    }

    public StringFilter getSolutionExternalLink() {
        return solutionExternalLink;
    }

    public Optional<StringFilter> optionalSolutionExternalLink() {
        return Optional.ofNullable(solutionExternalLink);
    }

    public StringFilter solutionExternalLink() {
        if (solutionExternalLink == null) {
            setSolutionExternalLink(new StringFilter());
        }
        return solutionExternalLink;
    }

    public void setSolutionExternalLink(StringFilter solutionExternalLink) {
        this.solutionExternalLink = solutionExternalLink;
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

    public StringFilter getCreateBy() {
        return createBy;
    }

    public Optional<StringFilter> optionalCreateBy() {
        return Optional.ofNullable(createBy);
    }

    public StringFilter createBy() {
        if (createBy == null) {
            setCreateBy(new StringFilter());
        }
        return createBy;
    }

    public void setCreateBy(StringFilter createBy) {
        this.createBy = createBy;
    }

    public LongFilter getCreateByUserId() {
        return createByUserId;
    }

    public Optional<LongFilter> optionalCreateByUserId() {
        return Optional.ofNullable(createByUserId);
    }

    public LongFilter createByUserId() {
        if (createByUserId == null) {
            setCreateByUserId(new LongFilter());
        }
        return createByUserId;
    }

    public void setCreateByUserId(LongFilter createByUserId) {
        this.createByUserId = createByUserId;
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

    public LongFilter getQuestionCategoryId() {
        return questionCategoryId;
    }

    public Optional<LongFilter> optionalQuestionCategoryId() {
        return Optional.ofNullable(questionCategoryId);
    }

    public LongFilter questionCategoryId() {
        if (questionCategoryId == null) {
            setQuestionCategoryId(new LongFilter());
        }
        return questionCategoryId;
    }

    public void setQuestionCategoryId(LongFilter questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public Optional<LongFilter> optionalTypeId() {
        return Optional.ofNullable(typeId);
    }

    public LongFilter typeId() {
        if (typeId == null) {
            setTypeId(new LongFilter());
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getGradeId() {
        return gradeId;
    }

    public Optional<LongFilter> optionalGradeId() {
        return Optional.ofNullable(gradeId);
    }

    public LongFilter gradeId() {
        if (gradeId == null) {
            setGradeId(new LongFilter());
        }
        return gradeId;
    }

    public void setGradeId(LongFilter gradeId) {
        this.gradeId = gradeId;
    }

    public LongFilter getOptionsId() {
        return optionsId;
    }

    public Optional<LongFilter> optionalOptionsId() {
        return Optional.ofNullable(optionsId);
    }

    public LongFilter optionsId() {
        if (optionsId == null) {
            setOptionsId(new LongFilter());
        }
        return optionsId;
    }

    public void setOptionsId(LongFilter optionsId) {
        this.optionsId = optionsId;
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
        final QuestionCriteria that = (QuestionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(description, that.description) &&
            Objects.equals(solution, that.solution) &&
            Objects.equals(solutionExternalLink, that.solutionExternalLink) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(updateDate, that.updateDate) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(createByUserId, that.createByUserId) &&
            Objects.equals(answer, that.answer) &&
            Objects.equals(questionCategoryId, that.questionCategoryId) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(gradeId, that.gradeId) &&
            Objects.equals(optionsId, that.optionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            points,
            description,
            solution,
            solutionExternalLink,
            createDate,
            updateDate,
            createBy,
            createByUserId,
            answer,
            questionCategoryId,
            typeId,
            gradeId,
            optionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPoints().map(f -> "points=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalSolution().map(f -> "solution=" + f + ", ").orElse("") +
            optionalSolutionExternalLink().map(f -> "solutionExternalLink=" + f + ", ").orElse("") +
            optionalCreateDate().map(f -> "createDate=" + f + ", ").orElse("") +
            optionalUpdateDate().map(f -> "updateDate=" + f + ", ").orElse("") +
            optionalCreateBy().map(f -> "createBy=" + f + ", ").orElse("") +
            optionalCreateByUserId().map(f -> "createByUserId=" + f + ", ").orElse("") +
            optionalAnswer().map(f -> "answer=" + f + ", ").orElse("") +
            optionalQuestionCategoryId().map(f -> "questionCategoryId=" + f + ", ").orElse("") +
            optionalTypeId().map(f -> "typeId=" + f + ", ").orElse("") +
            optionalGradeId().map(f -> "gradeId=" + f + ", ").orElse("") +
            optionalOptionsId().map(f -> "optionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
