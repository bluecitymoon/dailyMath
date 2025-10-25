package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.Community} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunityDTO implements Serializable {

    private Long id;

    private String name;

    private Double lat;

    private Double lon;

    private Integer studentsCount;

    private Instant createDate;

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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Integer getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(Integer studentsCount) {
        this.studentsCount = studentsCount;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
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
        if (!(o instanceof CommunityDTO)) {
            return false;
        }

        CommunityDTO communityDTO = (CommunityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, communityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", studentsCount=" + getStudentsCount() +
            ", createDate='" + getCreateDate() + "'" +
            ", distinct=" + getDistinct() +
            "}";
    }
}
