package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for Question with BaseGroup information.
 * This class combines question data with its associated base group information.
 */
public class QuestionWithBaseGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Question fields
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

    // BaseGroup fields
    private Long baseGroupId;
    private String baseGroupTitle;
    private String baseGroupQuestionIds;

    public QuestionWithBaseGroupDTO() {}

    public QuestionWithBaseGroupDTO(
        Long id,
        Double points,
        String description,
        String solution,
        String solutionExternalLink,
        Instant createDate,
        Instant updateDate,
        String createBy,
        Long createByUserId,
        String answer,
        Double level,
        QuestionCategoryDTO questionCategory,
        QuestionTypeDTO type,
        StudentGradeDTO grade,
        List<QuestionOptionDTO> options,
        Long baseGroupId,
        String baseGroupTitle,
        String baseGroupQuestionIds
    ) {
        this.id = id;
        this.points = points;
        this.description = description;
        this.solution = solution;
        this.solutionExternalLink = solutionExternalLink;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createBy = createBy;
        this.createByUserId = createByUserId;
        this.answer = answer;
        this.level = level;
        this.questionCategory = questionCategory;
        this.type = type;
        this.grade = grade;
        this.options = options;
        this.baseGroupId = baseGroupId;
        this.baseGroupTitle = baseGroupTitle;
        this.baseGroupQuestionIds = baseGroupQuestionIds;
    }

    // Question getters and setters
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

    // BaseGroup getters and setters
    public Long getBaseGroupId() {
        return baseGroupId;
    }

    public void setBaseGroupId(Long baseGroupId) {
        this.baseGroupId = baseGroupId;
    }

    public String getBaseGroupTitle() {
        return baseGroupTitle;
    }

    public void setBaseGroupTitle(String baseGroupTitle) {
        this.baseGroupTitle = baseGroupTitle;
    }

    public String getBaseGroupQuestionIds() {
        return baseGroupQuestionIds;
    }

    public void setBaseGroupQuestionIds(String baseGroupQuestionIds) {
        this.baseGroupQuestionIds = baseGroupQuestionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionWithBaseGroupDTO)) {
            return false;
        }
        QuestionWithBaseGroupDTO that = (QuestionWithBaseGroupDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return (
            "QuestionWithBaseGroupDTO{" +
            "id=" +
            id +
            ", points=" +
            points +
            ", description='" +
            description +
            '\'' +
            ", solution='" +
            solution +
            '\'' +
            ", solutionExternalLink='" +
            solutionExternalLink +
            '\'' +
            ", createDate=" +
            createDate +
            ", updateDate=" +
            updateDate +
            ", createBy='" +
            createBy +
            '\'' +
            ", createByUserId=" +
            createByUserId +
            ", answer='" +
            answer +
            '\'' +
            ", level=" +
            level +
            ", questionCategory=" +
            questionCategory +
            ", type=" +
            type +
            ", grade=" +
            grade +
            ", options=" +
            options +
            ", baseGroupId=" +
            baseGroupId +
            ", baseGroupTitle='" +
            baseGroupTitle +
            '\'' +
            ", baseGroupQuestionIds='" +
            baseGroupQuestionIds +
            '\'' +
            '}'
        );
    }
}
