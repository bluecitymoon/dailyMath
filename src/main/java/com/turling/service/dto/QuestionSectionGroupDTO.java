package com.turling.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.QuestionSectionGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionSectionGroupDTO implements Serializable {

    private Long id;

    private String title;

    private String baseGroupIds;

    private StudentGradeDTO grade;

    private Integer displayOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBaseGroupIds() {
        return baseGroupIds;
    }

    public void setBaseGroupIds(String baseGroupIds) {
        this.baseGroupIds = baseGroupIds;
    }

    public StudentGradeDTO getGrade() {
        return grade;
    }

    public void setGrade(StudentGradeDTO grade) {
        this.grade = grade;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionSectionGroupDTO)) {
            return false;
        }

        QuestionSectionGroupDTO questionSectionGroupDTO = (QuestionSectionGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionSectionGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionSectionGroupDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", baseGroupIds='" + getBaseGroupIds() + "'" +
            ", grade=" + getGrade() +
            ", displayOrder=" + getDisplayOrder() +
            "}";
    }
}
