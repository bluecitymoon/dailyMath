package com.turling.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.School} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchoolDTO implements Serializable {

    private Long id;

    private String name;

    private Integer registeredStudentsCount;

    private String pinyin;

    private DistinctDTO distinct;

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

    public Integer getRegisteredStudentsCount() {
        return registeredStudentsCount;
    }

    public void setRegisteredStudentsCount(Integer registeredStudentsCount) {
        this.registeredStudentsCount = registeredStudentsCount;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public DistinctDTO getDistinct() {
        return distinct;
    }

    public void setDistinct(DistinctDTO distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolDTO)) {
            return false;
        }

        SchoolDTO schoolDTO = (SchoolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schoolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", registeredStudentsCount=" + getRegisteredStudentsCount() +
            ", pinyin='" + getPinyin() + "'" +
            ", distinct=" + getDistinct() +
            "}";
    }
}
