package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StudentCriteriaTest {

    @Test
    void newStudentCriteriaHasAllFiltersNullTest() {
        var studentCriteria = new StudentCriteria();
        assertThat(studentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void studentCriteriaFluentMethodsCreatesFiltersTest() {
        var studentCriteria = new StudentCriteria();

        setAllFilters(studentCriteria);

        assertThat(studentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void studentCriteriaCopyCreatesNullFilterTest() {
        var studentCriteria = new StudentCriteria();
        var copy = studentCriteria.copy();

        assertThat(studentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(studentCriteria)
        );
    }

    @Test
    void studentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var studentCriteria = new StudentCriteria();
        setAllFilters(studentCriteria);

        var copy = studentCriteria.copy();

        assertThat(studentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(studentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var studentCriteria = new StudentCriteria();

        assertThat(studentCriteria).hasToString("StudentCriteria{}");
    }

    private static void setAllFilters(StudentCriteria studentCriteria) {
        studentCriteria.id();
        studentCriteria.name();
        studentCriteria.gender();
        studentCriteria.birthday();
        studentCriteria.registerDate();
        studentCriteria.updateDate();
        studentCriteria.latestContractEndDate();
        studentCriteria.contactNumber();
        studentCriteria.parentsName();
        studentCriteria.schoolId();
        studentCriteria.communityId();
        studentCriteria.distinct();
    }

    private static Condition<StudentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getBirthday()) &&
                condition.apply(criteria.getRegisterDate()) &&
                condition.apply(criteria.getUpdateDate()) &&
                condition.apply(criteria.getLatestContractEndDate()) &&
                condition.apply(criteria.getContactNumber()) &&
                condition.apply(criteria.getParentsName()) &&
                condition.apply(criteria.getSchoolId()) &&
                condition.apply(criteria.getCommunityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StudentCriteria> copyFiltersAre(StudentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getBirthday(), copy.getBirthday()) &&
                condition.apply(criteria.getRegisterDate(), copy.getRegisterDate()) &&
                condition.apply(criteria.getUpdateDate(), copy.getUpdateDate()) &&
                condition.apply(criteria.getLatestContractEndDate(), copy.getLatestContractEndDate()) &&
                condition.apply(criteria.getContactNumber(), copy.getContactNumber()) &&
                condition.apply(criteria.getParentsName(), copy.getParentsName()) &&
                condition.apply(criteria.getSchoolId(), copy.getSchoolId()) &&
                condition.apply(criteria.getCommunityId(), copy.getCommunityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
