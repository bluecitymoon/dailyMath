package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuestionCriteriaTest {

    @Test
    void newQuestionCriteriaHasAllFiltersNullTest() {
        var questionCriteria = new QuestionCriteria();
        assertThat(questionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void questionCriteriaFluentMethodsCreatesFiltersTest() {
        var questionCriteria = new QuestionCriteria();

        setAllFilters(questionCriteria);

        assertThat(questionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void questionCriteriaCopyCreatesNullFilterTest() {
        var questionCriteria = new QuestionCriteria();
        var copy = questionCriteria.copy();

        assertThat(questionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(questionCriteria)
        );
    }

    @Test
    void questionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var questionCriteria = new QuestionCriteria();
        setAllFilters(questionCriteria);

        var copy = questionCriteria.copy();

        assertThat(questionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(questionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var questionCriteria = new QuestionCriteria();

        assertThat(questionCriteria).hasToString("QuestionCriteria{}");
    }

    private static void setAllFilters(QuestionCriteria questionCriteria) {
        questionCriteria.id();
        questionCriteria.points();
        questionCriteria.description();
        questionCriteria.solution();
        questionCriteria.solutionExternalLink();
        questionCriteria.createDate();
        questionCriteria.updateDate();
        questionCriteria.createBy();
        questionCriteria.createByUserId();
        questionCriteria.questionCategoryId();
        questionCriteria.typeId();
        questionCriteria.gradeId();
        questionCriteria.optionsId();
        questionCriteria.distinct();
    }

    private static Condition<QuestionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPoints()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getSolution()) &&
                condition.apply(criteria.getSolutionExternalLink()) &&
                condition.apply(criteria.getCreateDate()) &&
                condition.apply(criteria.getUpdateDate()) &&
                condition.apply(criteria.getCreateBy()) &&
                condition.apply(criteria.getCreateByUserId()) &&
                condition.apply(criteria.getQuestionCategoryId()) &&
                condition.apply(criteria.getTypeId()) &&
                condition.apply(criteria.getGradeId()) &&
                condition.apply(criteria.getOptionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuestionCriteria> copyFiltersAre(QuestionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPoints(), copy.getPoints()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getSolution(), copy.getSolution()) &&
                condition.apply(criteria.getSolutionExternalLink(), copy.getSolutionExternalLink()) &&
                condition.apply(criteria.getCreateDate(), copy.getCreateDate()) &&
                condition.apply(criteria.getUpdateDate(), copy.getUpdateDate()) &&
                condition.apply(criteria.getCreateBy(), copy.getCreateBy()) &&
                condition.apply(criteria.getCreateByUserId(), copy.getCreateByUserId()) &&
                condition.apply(criteria.getQuestionCategoryId(), copy.getQuestionCategoryId()) &&
                condition.apply(criteria.getTypeId(), copy.getTypeId()) &&
                condition.apply(criteria.getGradeId(), copy.getGradeId()) &&
                condition.apply(criteria.getOptionsId(), copy.getOptionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
