package com.turling.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.turling.domain.Distinct} entity. This class is used
 * in {@link com.turling.web.rest.DistinctResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /distincts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DistinctCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter pinyin;

    private LongFilter communitiesId;

    private Boolean distinct;

    public DistinctCriteria() {}

    public DistinctCriteria(DistinctCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.pinyin = other.optionalPinyin().map(StringFilter::copy).orElse(null);
        this.communitiesId = other.optionalCommunitiesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DistinctCriteria copy() {
        return new DistinctCriteria(this);
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

    public StringFilter getPinyin() {
        return pinyin;
    }

    public Optional<StringFilter> optionalPinyin() {
        return Optional.ofNullable(pinyin);
    }

    public StringFilter pinyin() {
        if (pinyin == null) {
            setPinyin(new StringFilter());
        }
        return pinyin;
    }

    public void setPinyin(StringFilter pinyin) {
        this.pinyin = pinyin;
    }

    public LongFilter getCommunitiesId() {
        return communitiesId;
    }

    public Optional<LongFilter> optionalCommunitiesId() {
        return Optional.ofNullable(communitiesId);
    }

    public LongFilter communitiesId() {
        if (communitiesId == null) {
            setCommunitiesId(new LongFilter());
        }
        return communitiesId;
    }

    public void setCommunitiesId(LongFilter communitiesId) {
        this.communitiesId = communitiesId;
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
        final DistinctCriteria that = (DistinctCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(pinyin, that.pinyin) &&
            Objects.equals(communitiesId, that.communitiesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pinyin, communitiesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DistinctCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPinyin().map(f -> "pinyin=" + f + ", ").orElse("") +
            optionalCommunitiesId().map(f -> "communitiesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
