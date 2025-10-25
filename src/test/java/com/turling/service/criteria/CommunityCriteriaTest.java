package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CommunityCriteriaTest {

    @Test
    void newCommunityCriteriaHasAllFiltersNullTest() {
        var communityCriteria = new CommunityCriteria();
        assertThat(communityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void communityCriteriaFluentMethodsCreatesFiltersTest() {
        var communityCriteria = new CommunityCriteria();

        setAllFilters(communityCriteria);

        assertThat(communityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void communityCriteriaCopyCreatesNullFilterTest() {
        var communityCriteria = new CommunityCriteria();
        var copy = communityCriteria.copy();

        assertThat(communityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(communityCriteria)
        );
    }

    @Test
    void communityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var communityCriteria = new CommunityCriteria();
        setAllFilters(communityCriteria);

        var copy = communityCriteria.copy();

        assertThat(communityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(communityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var communityCriteria = new CommunityCriteria();

        assertThat(communityCriteria).hasToString("CommunityCriteria{}");
    }

    private static void setAllFilters(CommunityCriteria communityCriteria) {
        communityCriteria.id();
        communityCriteria.name();
        communityCriteria.lat();
        communityCriteria.lon();
        communityCriteria.studentsCount();
        communityCriteria.createDate();
        communityCriteria.distinctId();
        communityCriteria.distinct();
    }

    private static Condition<CommunityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLat()) &&
                condition.apply(criteria.getLon()) &&
                condition.apply(criteria.getStudentsCount()) &&
                condition.apply(criteria.getCreateDate()) &&
                condition.apply(criteria.getDistinctId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CommunityCriteria> copyFiltersAre(CommunityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLat(), copy.getLat()) &&
                condition.apply(criteria.getLon(), copy.getLon()) &&
                condition.apply(criteria.getStudentsCount(), copy.getStudentsCount()) &&
                condition.apply(criteria.getCreateDate(), copy.getCreateDate()) &&
                condition.apply(criteria.getDistinctId(), copy.getDistinctId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
