package com.turling.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.turling.domain.Community} entity. This class is used
 * in {@link com.turling.web.rest.CommunityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /communities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DoubleFilter lat;

    private DoubleFilter lon;

    private IntegerFilter studentsCount;

    private InstantFilter createDate;

    private LongFilter distinctId;

    private Boolean distinct;

    public CommunityCriteria() {}

    public CommunityCriteria(CommunityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.lat = other.optionalLat().map(DoubleFilter::copy).orElse(null);
        this.lon = other.optionalLon().map(DoubleFilter::copy).orElse(null);
        this.studentsCount = other.optionalStudentsCount().map(IntegerFilter::copy).orElse(null);
        this.createDate = other.optionalCreateDate().map(InstantFilter::copy).orElse(null);
        this.distinctId = other.optionalDistinctId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CommunityCriteria copy() {
        return new CommunityCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getLat() {
        return lat;
    }

    public Optional<DoubleFilter> optionalLat() {
        return Optional.ofNullable(lat);
    }

    public DoubleFilter lat() {
        if (lat == null) {
            setLat(new DoubleFilter());
        }
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public DoubleFilter getLon() {
        return lon;
    }

    public Optional<DoubleFilter> optionalLon() {
        return Optional.ofNullable(lon);
    }

    public DoubleFilter lon() {
        if (lon == null) {
            setLon(new DoubleFilter());
        }
        return lon;
    }

    public void setLon(DoubleFilter lon) {
        this.lon = lon;
    }

    public IntegerFilter getStudentsCount() {
        return studentsCount;
    }

    public Optional<IntegerFilter> optionalStudentsCount() {
        return Optional.ofNullable(studentsCount);
    }

    public IntegerFilter studentsCount() {
        if (studentsCount == null) {
            setStudentsCount(new IntegerFilter());
        }
        return studentsCount;
    }

    public void setStudentsCount(IntegerFilter studentsCount) {
        this.studentsCount = studentsCount;
    }

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public Optional<InstantFilter> optionalCreateDate() {
        return Optional.ofNullable(createDate);
    }

    public InstantFilter createDate() {
        if (createDate == null) {
            setCreateDate(new InstantFilter());
        }
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
    }

    public LongFilter getDistinctId() {
        return distinctId;
    }

    public Optional<LongFilter> optionalDistinctId() {
        return Optional.ofNullable(distinctId);
    }

    public LongFilter distinctId() {
        if (distinctId == null) {
            setDistinctId(new LongFilter());
        }
        return distinctId;
    }

    public void setDistinctId(LongFilter distinctId) {
        this.distinctId = distinctId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommunityCriteria that = (CommunityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(studentsCount, that.studentsCount) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(distinctId, that.distinctId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lat, lon, studentsCount, createDate, distinctId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalLat().map(f -> "lat=" + f + ", ").orElse("") +
            optionalLon().map(f -> "lon=" + f + ", ").orElse("") +
            optionalStudentsCount().map(f -> "studentsCount=" + f + ", ").orElse("") +
            optionalCreateDate().map(f -> "createDate=" + f + ", ").orElse("") +
            optionalDistinctId().map(f -> "distinctId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
