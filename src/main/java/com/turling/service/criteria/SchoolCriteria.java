package com.turling.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.turling.domain.School} entity. This class is used
 * in {@link com.turling.web.rest.SchoolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /schools?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchoolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter registeredStudentsCount;

    private StringFilter pinyin;

    private LongFilter distinctId;

    private Boolean distinct;

    public SchoolCriteria() {}

    public SchoolCriteria(SchoolCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.registeredStudentsCount = other.optionalRegisteredStudentsCount().map(IntegerFilter::copy).orElse(null);
        this.pinyin = other.optionalPinyin().map(StringFilter::copy).orElse(null);
        this.distinctId = other.optionalDistinctId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SchoolCriteria copy() {
        return new SchoolCriteria(this);
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

    public IntegerFilter getRegisteredStudentsCount() {
        return registeredStudentsCount;
    }

    public Optional<IntegerFilter> optionalRegisteredStudentsCount() {
        return Optional.ofNullable(registeredStudentsCount);
    }

    public IntegerFilter registeredStudentsCount() {
        if (registeredStudentsCount == null) {
            setRegisteredStudentsCount(new IntegerFilter());
        }
        return registeredStudentsCount;
    }

    public void setRegisteredStudentsCount(IntegerFilter registeredStudentsCount) {
        this.registeredStudentsCount = registeredStudentsCount;
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
        final SchoolCriteria that = (SchoolCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(registeredStudentsCount, that.registeredStudentsCount) &&
            Objects.equals(pinyin, that.pinyin) &&
            Objects.equals(distinctId, that.distinctId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, registeredStudentsCount, pinyin, distinctId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalRegisteredStudentsCount().map(f -> "registeredStudentsCount=" + f + ", ").orElse("") +
            optionalPinyin().map(f -> "pinyin=" + f + ", ").orElse("") +
            optionalDistinctId().map(f -> "distinctId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
