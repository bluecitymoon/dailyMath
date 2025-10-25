package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DistinctCriteriaTest {

    @Test
    void newDistinctCriteriaHasAllFiltersNullTest() {
        var distinctCriteria = new DistinctCriteria();
        assertThat(distinctCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void distinctCriteriaFluentMethodsCreatesFiltersTest() {
        var distinctCriteria = new DistinctCriteria();

        setAllFilters(distinctCriteria);

        assertThat(distinctCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void distinctCriteriaCopyCreatesNullFilterTest() {
        var distinctCriteria = new DistinctCriteria();
        var copy = distinctCriteria.copy();

        assertThat(distinctCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(distinctCriteria)
        );
    }

    @Test
    void distinctCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var distinctCriteria = new DistinctCriteria();
        setAllFilters(distinctCriteria);

        var copy = distinctCriteria.copy();

        assertThat(distinctCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(distinctCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var distinctCriteria = new DistinctCriteria();

        assertThat(distinctCriteria).hasToString("DistinctCriteria{}");
    }

    private static void setAllFilters(DistinctCriteria distinctCriteria) {
        distinctCriteria.id();
        distinctCriteria.name();
        distinctCriteria.pinyin();
        distinctCriteria.communitiesId();
        distinctCriteria.distinct();
    }

    private static Condition<DistinctCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPinyin()) &&
                condition.apply(criteria.getCommunitiesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DistinctCriteria> copyFiltersAre(DistinctCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPinyin(), copy.getPinyin()) &&
                condition.apply(criteria.getCommunitiesId(), copy.getCommunitiesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
