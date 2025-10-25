package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuestionSectionGroupCriteriaTest {

    @Test
    void newQuestionSectionGroupCriteriaHasAllFiltersNullTest() {
        var questionSectionGroupCriteria = new QuestionSectionGroupCriteria();
        assertThat(questionSectionGroupCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void questionSectionGroupCriteriaFluentMethodsCreatesFiltersTest() {
        var questionSectionGroupCriteria = new QuestionSectionGroupCriteria();

        setAllFilters(questionSectionGroupCriteria);

        assertThat(questionSectionGroupCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void questionSectionGroupCriteriaCopyCreatesNullFilterTest() {
        var questionSectionGroupCriteria = new QuestionSectionGroupCriteria();
        var copy = questionSectionGroupCriteria.copy();

        assertThat(questionSectionGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(questionSectionGroupCriteria)
        );
    }

    @Test
    void questionSectionGroupCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var questionSectionGroupCriteria = new QuestionSectionGroupCriteria();
        setAllFilters(questionSectionGroupCriteria);

        var copy = questionSectionGroupCriteria.copy();

        assertThat(questionSectionGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(questionSectionGroupCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var questionSectionGroupCriteria = new QuestionSectionGroupCriteria();

        assertThat(questionSectionGroupCriteria).hasToString("QuestionSectionGroupCriteria{}");
    }

    private static void setAllFilters(QuestionSectionGroupCriteria questionSectionGroupCriteria) {
        questionSectionGroupCriteria.id();
        questionSectionGroupCriteria.title();
        questionSectionGroupCriteria.baseGroupIds();
        questionSectionGroupCriteria.gradeId();
        questionSectionGroupCriteria.distinct();
    }

    private static Condition<QuestionSectionGroupCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getBaseGroupIds()) &&
                condition.apply(criteria.getGradeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuestionSectionGroupCriteria> copyFiltersAre(
        QuestionSectionGroupCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getBaseGroupIds(), copy.getBaseGroupIds()) &&
                condition.apply(criteria.getGradeId(), copy.getGradeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
