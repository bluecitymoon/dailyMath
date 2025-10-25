package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuestionBaseGroupCriteriaTest {

    @Test
    void newQuestionBaseGroupCriteriaHasAllFiltersNullTest() {
        var questionBaseGroupCriteria = new QuestionBaseGroupCriteria();
        assertThat(questionBaseGroupCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void questionBaseGroupCriteriaFluentMethodsCreatesFiltersTest() {
        var questionBaseGroupCriteria = new QuestionBaseGroupCriteria();

        setAllFilters(questionBaseGroupCriteria);

        assertThat(questionBaseGroupCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void questionBaseGroupCriteriaCopyCreatesNullFilterTest() {
        var questionBaseGroupCriteria = new QuestionBaseGroupCriteria();
        var copy = questionBaseGroupCriteria.copy();

        assertThat(questionBaseGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(questionBaseGroupCriteria)
        );
    }

    @Test
    void questionBaseGroupCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var questionBaseGroupCriteria = new QuestionBaseGroupCriteria();
        setAllFilters(questionBaseGroupCriteria);

        var copy = questionBaseGroupCriteria.copy();

        assertThat(questionBaseGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(questionBaseGroupCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var questionBaseGroupCriteria = new QuestionBaseGroupCriteria();

        assertThat(questionBaseGroupCriteria).hasToString("QuestionBaseGroupCriteria{}");
    }

    private static void setAllFilters(QuestionBaseGroupCriteria questionBaseGroupCriteria) {
        questionBaseGroupCriteria.id();
        questionBaseGroupCriteria.title();
        questionBaseGroupCriteria.questionIds();
        questionBaseGroupCriteria.distinct();
    }

    private static Condition<QuestionBaseGroupCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getQuestionIds()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuestionBaseGroupCriteria> copyFiltersAre(
        QuestionBaseGroupCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getQuestionIds(), copy.getQuestionIds()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
