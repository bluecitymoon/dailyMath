package com.turling.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.turling.domain.Student} entity. This class is used
 * in {@link com.turling.web.rest.StudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter gender;

    private InstantFilter birthday;

    private InstantFilter registerDate;

    private InstantFilter updateDate;

    private InstantFilter latestContractEndDate;

    private StringFilter contactNumber;

    private StringFilter parentsName;

    private LongFilter schoolId;

    private LongFilter communityId;

    private Boolean distinct;

    public StudentCriteria() {}

    public StudentCriteria(StudentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(StringFilter::copy).orElse(null);
        this.birthday = other.optionalBirthday().map(InstantFilter::copy).orElse(null);
        this.registerDate = other.optionalRegisterDate().map(InstantFilter::copy).orElse(null);
        this.updateDate = other.optionalUpdateDate().map(InstantFilter::copy).orElse(null);
        this.latestContractEndDate = other.optionalLatestContractEndDate().map(InstantFilter::copy).orElse(null);
        this.contactNumber = other.optionalContactNumber().map(StringFilter::copy).orElse(null);
        this.parentsName = other.optionalParentsName().map(StringFilter::copy).orElse(null);
        this.schoolId = other.optionalSchoolId().map(LongFilter::copy).orElse(null);
        this.communityId = other.optionalCommunityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StudentCriteria copy() {
        return new StudentCriteria(this);
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

    public StringFilter getGender() {
        return gender;
    }

    public Optional<StringFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public StringFilter gender() {
        if (gender == null) {
            setGender(new StringFilter());
        }
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
    }

    public InstantFilter getBirthday() {
        return birthday;
    }

    public Optional<InstantFilter> optionalBirthday() {
        return Optional.ofNullable(birthday);
    }

    public InstantFilter birthday() {
        if (birthday == null) {
            setBirthday(new InstantFilter());
        }
        return birthday;
    }

    public void setBirthday(InstantFilter birthday) {
        this.birthday = birthday;
    }

    public InstantFilter getRegisterDate() {
        return registerDate;
    }

    public Optional<InstantFilter> optionalRegisterDate() {
        return Optional.ofNullable(registerDate);
    }

    public InstantFilter registerDate() {
        if (registerDate == null) {
            setRegisterDate(new InstantFilter());
        }
        return registerDate;
    }

    public void setRegisterDate(InstantFilter registerDate) {
        this.registerDate = registerDate;
    }

    public InstantFilter getUpdateDate() {
        return updateDate;
    }

    public Optional<InstantFilter> optionalUpdateDate() {
        return Optional.ofNullable(updateDate);
    }

    public InstantFilter updateDate() {
        if (updateDate == null) {
            setUpdateDate(new InstantFilter());
        }
        return updateDate;
    }

    public void setUpdateDate(InstantFilter updateDate) {
        this.updateDate = updateDate;
    }

    public InstantFilter getLatestContractEndDate() {
        return latestContractEndDate;
    }

    public Optional<InstantFilter> optionalLatestContractEndDate() {
        return Optional.ofNullable(latestContractEndDate);
    }

    public InstantFilter latestContractEndDate() {
        if (latestContractEndDate == null) {
            setLatestContractEndDate(new InstantFilter());
        }
        return latestContractEndDate;
    }

    public void setLatestContractEndDate(InstantFilter latestContractEndDate) {
        this.latestContractEndDate = latestContractEndDate;
    }

    public StringFilter getContactNumber() {
        return contactNumber;
    }

    public Optional<StringFilter> optionalContactNumber() {
        return Optional.ofNullable(contactNumber);
    }

    public StringFilter contactNumber() {
        if (contactNumber == null) {
            setContactNumber(new StringFilter());
        }
        return contactNumber;
    }

    public void setContactNumber(StringFilter contactNumber) {
        this.contactNumber = contactNumber;
    }

    public StringFilter getParentsName() {
        return parentsName;
    }

    public Optional<StringFilter> optionalParentsName() {
        return Optional.ofNullable(parentsName);
    }

    public StringFilter parentsName() {
        if (parentsName == null) {
            setParentsName(new StringFilter());
        }
        return parentsName;
    }

    public void setParentsName(StringFilter parentsName) {
        this.parentsName = parentsName;
    }

    public LongFilter getSchoolId() {
        return schoolId;
    }

    public Optional<LongFilter> optionalSchoolId() {
        return Optional.ofNullable(schoolId);
    }

    public LongFilter schoolId() {
        if (schoolId == null) {
            setSchoolId(new LongFilter());
        }
        return schoolId;
    }

    public void setSchoolId(LongFilter schoolId) {
        this.schoolId = schoolId;
    }

    public LongFilter getCommunityId() {
        return communityId;
    }

    public Optional<LongFilter> optionalCommunityId() {
        return Optional.ofNullable(communityId);
    }

    public LongFilter communityId() {
        if (communityId == null) {
            setCommunityId(new LongFilter());
        }
        return communityId;
    }

    public void setCommunityId(LongFilter communityId) {
        this.communityId = communityId;
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
        final StudentCriteria that = (StudentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(registerDate, that.registerDate) &&
            Objects.equals(updateDate, that.updateDate) &&
            Objects.equals(latestContractEndDate, that.latestContractEndDate) &&
            Objects.equals(contactNumber, that.contactNumber) &&
            Objects.equals(parentsName, that.parentsName) &&
            Objects.equals(schoolId, that.schoolId) &&
            Objects.equals(communityId, that.communityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            gender,
            birthday,
            registerDate,
            updateDate,
            latestContractEndDate,
            contactNumber,
            parentsName,
            schoolId,
            communityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalBirthday().map(f -> "birthday=" + f + ", ").orElse("") +
            optionalRegisterDate().map(f -> "registerDate=" + f + ", ").orElse("") +
            optionalUpdateDate().map(f -> "updateDate=" + f + ", ").orElse("") +
            optionalLatestContractEndDate().map(f -> "latestContractEndDate=" + f + ", ").orElse("") +
            optionalContactNumber().map(f -> "contactNumber=" + f + ", ").orElse("") +
            optionalParentsName().map(f -> "parentsName=" + f + ", ").orElse("") +
            optionalSchoolId().map(f -> "schoolId=" + f + ", ").orElse("") +
            optionalCommunityId().map(f -> "communityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
