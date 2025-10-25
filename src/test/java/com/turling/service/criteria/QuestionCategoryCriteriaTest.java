package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuestionCategoryCriteriaTest {

    @Test
    void newQuestionCategoryCriteriaHasAllFiltersNullTest() {
        var questionCategoryCriteria = new QuestionCategoryCriteria();
        assertThat(questionCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void questionCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var questionCategoryCriteria = new QuestionCategoryCriteria();

        setAllFilters(questionCategoryCriteria);

        assertThat(questionCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void questionCategoryCriteriaCopyCreatesNullFilterTest() {
        var questionCategoryCriteria = new QuestionCategoryCriteria();
        var copy = questionCategoryCriteria.copy();

        assertThat(questionCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(questionCategoryCriteria)
        );
    }

    @Test
    void questionCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var questionCategoryCriteria = new QuestionCategoryCriteria();
        setAllFilters(questionCategoryCriteria);

        var copy = questionCategoryCriteria.copy();

        assertThat(questionCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(questionCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var questionCategoryCriteria = new QuestionCategoryCriteria();

        assertThat(questionCategoryCriteria).hasToString("QuestionCategoryCriteria{}");
    }

    private static void setAllFilters(QuestionCategoryCriteria questionCategoryCriteria) {
        questionCategoryCriteria.id();
        questionCategoryCriteria.name();
        questionCategoryCriteria.distinct();
    }

    private static Condition<QuestionCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuestionCategoryCriteria> copyFiltersAre(
        QuestionCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
