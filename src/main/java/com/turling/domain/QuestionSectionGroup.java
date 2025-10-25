package com.turling.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A QuestionSectionGroup.
 */
@Entity
@Table(name = "question_section_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionSectionGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "base_group_ids")
    private String baseGroupIds;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudentGrade grade;

    @Column(name = "display_order")
    private Integer displayOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionSectionGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public QuestionSectionGroup title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBaseGroupIds() {
        return this.baseGroupIds;
    }

    public QuestionSectionGroup baseGroupIds(String baseGroupIds) {
        this.setBaseGroupIds(baseGroupIds);
        return this;
    }

    public void setBaseGroupIds(String baseGroupIds) {
        this.baseGroupIds = baseGroupIds;
    }

    public StudentGrade getGrade() {
        return this.grade;
    }

    public void setGrade(StudentGrade studentGrade) {
        this.grade = studentGrade;
    }

    public QuestionSectionGroup grade(StudentGrade studentGrade) {
        this.setGrade(studentGrade);
        return this;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public QuestionSectionGroup displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionSectionGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((QuestionSectionGroup) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionSectionGroup{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", baseGroupIds='" + getBaseGroupIds() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            "}";
    }
}
