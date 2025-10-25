package com.turling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A School.
 */
@Entity
@Table(name = "school")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "registered_students_count")
    private Integer registeredStudentsCount;

    @Column(name = "pinyin")
    private String pinyin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "communities" }, allowSetters = true)
    private Distinct distinct;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public School id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public School name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegisteredStudentsCount() {
        return this.registeredStudentsCount;
    }

    public School registeredStudentsCount(Integer registeredStudentsCount) {
        this.setRegisteredStudentsCount(registeredStudentsCount);
        return this;
    }

    public void setRegisteredStudentsCount(Integer registeredStudentsCount) {
        this.registeredStudentsCount = registeredStudentsCount;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public School pinyin(String pinyin) {
        this.setPinyin(pinyin);
        return this;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Distinct getDistinct() {
        return this.distinct;
    }

    public void setDistinct(Distinct distinct) {
        this.distinct = distinct;
    }

    public School distinct(Distinct distinct) {
        this.setDistinct(distinct);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof School)) {
            return false;
        }
        return getId() != null && getId().equals(((School) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "School{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", registeredStudentsCount=" + getRegisteredStudentsCount() +
            ", pinyin='" + getPinyin() + "'" +
            "}";
    }
}
