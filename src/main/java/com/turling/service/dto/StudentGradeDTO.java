package com.turling.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.StudentGrade} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentGradeDTO implements Serializable {

    private Long id;

    private String name;

    private Integer index;

    private Integer term;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentGradeDTO)) {
            return false;
        }

        StudentGradeDTO studentGradeDTO = (StudentGradeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentGradeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentGradeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", index=" + getIndex() +
            ", term=" + getTerm() +
            "}";
    }
}
