package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SchoolCriteriaTest {

    @Test
    void newSchoolCriteriaHasAllFiltersNullTest() {
        var schoolCriteria = new SchoolCriteria();
        assertThat(schoolCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void schoolCriteriaFluentMethodsCreatesFiltersTest() {
        var schoolCriteria = new SchoolCriteria();

        setAllFilters(schoolCriteria);

        assertThat(schoolCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void schoolCriteriaCopyCreatesNullFilterTest() {
        var schoolCriteria = new SchoolCriteria();
        var copy = schoolCriteria.copy();

        assertThat(schoolCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(schoolCriteria)
        );
    }

    @Test
    void schoolCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var schoolCriteria = new SchoolCriteria();
        setAllFilters(schoolCriteria);

        var copy = schoolCriteria.copy();

        assertThat(schoolCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(schoolCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var schoolCriteria = new SchoolCriteria();

        assertThat(schoolCriteria).hasToString("SchoolCriteria{}");
    }

    private static void setAllFilters(SchoolCriteria schoolCriteria) {
        schoolCriteria.id();
        schoolCriteria.name();
        schoolCriteria.registeredStudentsCount();
        schoolCriteria.pinyin();
        schoolCriteria.distinctId();
        schoolCriteria.distinct();
    }

    private static Condition<SchoolCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getRegisteredStudentsCount()) &&
                condition.apply(criteria.getPinyin()) &&
                condition.apply(criteria.getDistinctId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SchoolCriteria> copyFiltersAre(SchoolCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getRegisteredStudentsCount(), copy.getRegisteredStudentsCount()) &&
                condition.apply(criteria.getPinyin(), copy.getPinyin()) &&
                condition.apply(criteria.getDistinctId(), copy.getDistinctId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
