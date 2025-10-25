package com.turling.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Response DTO for section group questions query.
 * This class provides a structured response containing section group information and associated questions with base group details.
 */
public class SectionGroupQuestionsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sectionGroupId;
    private String sectionGroupTitle;
    private String sectionGroupBaseGroupIds;
    private StudentGradeDTO sectionGroupGrade;
    private Integer sectionGroupDisplayOrder;
    private List<QuestionWithBaseGroupDTO> questions;
    private Integer totalQuestionCount;

    public SectionGroupQuestionsResponse() {}

    public SectionGroupQuestionsResponse(
        Long sectionGroupId,
        String sectionGroupTitle,
        String sectionGroupBaseGroupIds,
        StudentGradeDTO sectionGroupGrade,
        Integer sectionGroupDisplayOrder,
        List<QuestionWithBaseGroupDTO> questions,
        Integer totalQuestionCount
    ) {
        this.sectionGroupId = sectionGroupId;
        this.sectionGroupTitle = sectionGroupTitle;
        this.sectionGroupBaseGroupIds = sectionGroupBaseGroupIds;
        this.sectionGroupGrade = sectionGroupGrade;
        this.sectionGroupDisplayOrder = sectionGroupDisplayOrder;
        this.questions = questions;
        this.totalQuestionCount = totalQuestionCount;
    }

    public Long getSectionGroupId() {
        return sectionGroupId;
    }

    public void setSectionGroupId(Long sectionGroupId) {
        this.sectionGroupId = sectionGroupId;
    }

    public String getSectionGroupTitle() {
        return sectionGroupTitle;
    }

    public void setSectionGroupTitle(String sectionGroupTitle) {
        this.sectionGroupTitle = sectionGroupTitle;
    }

    public String getSectionGroupBaseGroupIds() {
        return sectionGroupBaseGroupIds;
    }

    public void setSectionGroupBaseGroupIds(String sectionGroupBaseGroupIds) {
        this.sectionGroupBaseGroupIds = sectionGroupBaseGroupIds;
    }

    public StudentGradeDTO getSectionGroupGrade() {
        return sectionGroupGrade;
    }

    public void setSectionGroupGrade(StudentGradeDTO sectionGroupGrade) {
        this.sectionGroupGrade = sectionGroupGrade;
    }

    public Integer getSectionGroupDisplayOrder() {
        return sectionGroupDisplayOrder;
    }

    public void setSectionGroupDisplayOrder(Integer sectionGroupDisplayOrder) {
        this.sectionGroupDisplayOrder = sectionGroupDisplayOrder;
    }

    public List<QuestionWithBaseGroupDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionWithBaseGroupDTO> questions) {
        this.questions = questions;
    }

    public Integer getTotalQuestionCount() {
        return totalQuestionCount;
    }

    public void setTotalQuestionCount(Integer totalQuestionCount) {
        this.totalQuestionCount = totalQuestionCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SectionGroupQuestionsResponse)) {
            return false;
        }
        SectionGroupQuestionsResponse that = (SectionGroupQuestionsResponse) o;
        return sectionGroupId != null && sectionGroupId.equals(that.sectionGroupId);
    }

    @Override
    public int hashCode() {
        return sectionGroupId != null ? sectionGroupId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return (
            "SectionGroupQuestionsResponse{" +
            "sectionGroupId=" +
            sectionGroupId +
            ", sectionGroupTitle='" +
            sectionGroupTitle +
            '\'' +
            ", sectionGroupBaseGroupIds='" +
            sectionGroupBaseGroupIds +
            '\'' +
            ", sectionGroupGrade=" +
            sectionGroupGrade +
            ", sectionGroupDisplayOrder=" +
            sectionGroupDisplayOrder +
            ", questions=" +
            questions +
            ", totalQuestionCount=" +
            totalQuestionCount +
            '}'
        );
    }
}
