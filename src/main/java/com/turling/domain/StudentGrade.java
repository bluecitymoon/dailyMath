package com.turling.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A StudentGrade.
 */
@Entity
@Table(name = "student_grade")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_index")
    private Integer index;

    @Column(name = "term")
    private Integer term;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentGrade id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public StudentGrade name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return this.index;
    }

    public StudentGrade index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getTerm() {
        return this.term;
    }

    public StudentGrade term(Integer term) {
        this.setTerm(term);
        return this;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentGrade)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentGrade) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentGrade{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", index=" + getIndex() +
            ", term=" + getTerm() +
            "}";
    }
}
