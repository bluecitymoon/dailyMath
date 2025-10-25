package com.turling.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Response DTO for grade-based section group queries.
 * This class provides a flexible structure for returning section group information by grade.
 */
public class GradeSectionGroupResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long gradeId;
    private String gradeName;
    private Integer gradeIndex;
    private Integer gradeTerm;
    private List<QuestionSectionGroupDTO> sectionGroups;
    private Integer totalCount;

    public GradeSectionGroupResponse() {}

    public GradeSectionGroupResponse(
        Long gradeId,
        String gradeName,
        Integer gradeIndex,
        Integer gradeTerm,
        List<QuestionSectionGroupDTO> sectionGroups,
        Integer totalCount
    ) {
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.gradeIndex = gradeIndex;
        this.gradeTerm = gradeTerm;
        this.sectionGroups = sectionGroups;
        this.totalCount = totalCount;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getGradeIndex() {
        return gradeIndex;
    }

    public void setGradeIndex(Integer gradeIndex) {
        this.gradeIndex = gradeIndex;
    }

    public Integer getGradeTerm() {
        return gradeTerm;
    }

    public void setGradeTerm(Integer gradeTerm) {
        this.gradeTerm = gradeTerm;
    }

    public List<QuestionSectionGroupDTO> getSectionGroups() {
        return sectionGroups;
    }

    public void setSectionGroups(List<QuestionSectionGroupDTO> sectionGroups) {
        this.sectionGroups = sectionGroups;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeSectionGroupResponse)) {
            return false;
        }
        GradeSectionGroupResponse that = (GradeSectionGroupResponse) o;
        return gradeId != null && gradeId.equals(that.gradeId);
    }

    @Override
    public int hashCode() {
        return gradeId != null ? gradeId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return (
            "GradeSectionGroupResponse{" +
            "gradeId=" +
            gradeId +
            ", gradeName='" +
            gradeName +
            '\'' +
            ", gradeIndex=" +
            gradeIndex +
            ", gradeTerm=" +
            gradeTerm +
            ", sectionGroups=" +
            sectionGroups +
            ", totalCount=" +
            totalCount +
            '}'
        );
    }
}
