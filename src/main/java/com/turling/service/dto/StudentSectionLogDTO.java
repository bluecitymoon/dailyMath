package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.StudentSectionLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentSectionLogDTO implements Serializable {

    private Long id;

    private Long studentId;

    private Long sectionId;

    private Integer totalCount;

    private Integer finishedCount;

    private Double correctRate;

    private Instant createDate;

    private Instant updateDate;

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

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
    }

    public Double getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(Double correctRate) {
        this.correctRate = correctRate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentSectionLogDTO)) {
            return false;
        }

        StudentSectionLogDTO studentSectionLogDTO = (StudentSectionLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentSectionLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentSectionLogDTO{" +
            "id=" + getId() +
            ", studentId=" + getStudentId() +
            ", sectionId=" + getSectionId() +
            ", totalCount=" + getTotalCount() +
            ", finishedCount=" + getFinishedCount() +
            ", correctRate=" + getCorrectRate() +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            "}";
    }
}
