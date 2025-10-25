package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.Question} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionDTO implements Serializable {

    private Long id;

    private Double points;

    private String description;

    private String solution;

    private String solutionExternalLink;

    private Instant createDate;

    private Instant updateDate;

    private String createBy;

    private Long createByUserId;

    private String answer;

    private Double level;

    private QuestionCategoryDTO questionCategory;

    private QuestionTypeDTO type;

    private StudentGradeDTO grade;

    private List<QuestionOptionDTO> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSolutionExternalLink() {
        return solutionExternalLink;
    }

    public void setSolutionExternalLink(String solutionExternalLink) {
        this.solutionExternalLink = solutionExternalLink;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCreateByUserId() {
        return createByUserId;
    }

    public void setCreateByUserId(Long createByUserId) {
        this.createByUserId = createByUserId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public QuestionCategoryDTO getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(QuestionCategoryDTO questionCategory) {
        this.questionCategory = questionCategory;
    }

    public QuestionTypeDTO getType() {
        return type;
    }

    public void setType(QuestionTypeDTO type) {
        this.type = type;
    }

    public StudentGradeDTO getGrade() {
        return grade;
    }

    public void setGrade(StudentGradeDTO grade) {
        this.grade = grade;
    }

    public List<QuestionOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOptionDTO> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", description='" + getDescription() + "'" +
            ", solution='" + getSolution() + "'" +
            ", solutionExternalLink='" + getSolutionExternalLink() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createByUserId=" + getCreateByUserId() +
            ", answer='" + getAnswer() + "'" +
            ", level=" + getLevel() +
            ", questionCategory=" + getQuestionCategory() +
            ", type=" + getType() +
            ", grade=" + getGrade() +
            "}";
    }
}
