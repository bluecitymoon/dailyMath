package com.turling.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.Distinct} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DistinctDTO implements Serializable {

    private Long id;

    private String name;

    private String pinyin;

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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DistinctDTO)) {
            return false;
        }

        DistinctDTO distinctDTO = (DistinctDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, distinctDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DistinctDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pinyin='" + getPinyin() + "'" +
            "}";
    }
}
