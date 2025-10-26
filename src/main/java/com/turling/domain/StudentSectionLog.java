package com.turling.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A StudentSectionLog.
 */
@Entity
@Table(name = "student_section_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentSectionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "section_id")
    private Long sectionId;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "finished_count")
    private Integer finishedCount;

    @Column(name = "correct_rate")
    private Double correctRate;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentSectionLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public StudentSectionLog studentId(Long studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSectionId() {
        return this.sectionId;
    }

    public StudentSectionLog sectionId(Long sectionId) {
        this.setSectionId(sectionId);
        return this;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public StudentSectionLog totalCount(Integer totalCount) {
        this.setTotalCount(totalCount);
        return this;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getFinishedCount() {
        return this.finishedCount;
    }

    public StudentSectionLog finishedCount(Integer finishedCount) {
        this.setFinishedCount(finishedCount);
        return this;
    }

    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
    }

    public Double getCorrectRate() {
        return this.correctRate;
    }

    public StudentSectionLog correctRate(Double correctRate) {
        this.setCorrectRate(correctRate);
        return this;
    }

    public void setCorrectRate(Double correctRate) {
        this.correctRate = correctRate;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public StudentSectionLog createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public StudentSectionLog updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentSectionLog)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentSectionLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentSectionLog{" +
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
