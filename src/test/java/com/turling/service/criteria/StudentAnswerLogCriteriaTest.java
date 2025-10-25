package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StudentAnswerLogCriteriaTest {

    @Test
    void newStudentAnswerLogCriteriaHasAllFiltersNullTest() {
        var studentAnswerLogCriteria = new StudentAnswerLogCriteria();
        assertThat(studentAnswerLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void studentAnswerLogCriteriaFluentMethodsCreatesFiltersTest() {
        var studentAnswerLogCriteria = new StudentAnswerLogCriteria();

        setAllFilters(studentAnswerLogCriteria);

        assertThat(studentAnswerLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void studentAnswerLogCriteriaCopyCreatesNullFilterTest() {
        var studentAnswerLogCriteria = new StudentAnswerLogCriteria();
        var copy = studentAnswerLogCriteria.copy();

        assertThat(studentAnswerLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(studentAnswerLogCriteria)
        );
    }

    @Test
    void studentAnswerLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var studentAnswerLogCriteria = new StudentAnswerLogCriteria();
        setAllFilters(studentAnswerLogCriteria);

        var copy = studentAnswerLogCriteria.copy();

        assertThat(studentAnswerLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(studentAnswerLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var studentAnswerLogCriteria = new StudentAnswerLogCriteria();

        assertThat(studentAnswerLogCriteria).hasToString("StudentAnswerLogCriteria{}");
    }

    private static void setAllFilters(StudentAnswerLogCriteria studentAnswerLogCriteria) {
        studentAnswerLogCriteria.id();
        studentAnswerLogCriteria.studentId();
        studentAnswerLogCriteria.questionId();
        studentAnswerLogCriteria.answer();
        studentAnswerLogCriteria.correct();
        studentAnswerLogCriteria.createDate();
        studentAnswerLogCriteria.winPoints();
        studentAnswerLogCriteria.distinct();
    }

    private static Condition<StudentAnswerLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStudentId()) &&
                condition.apply(criteria.getQuestionId()) &&
                condition.apply(criteria.getAnswer()) &&
                condition.apply(criteria.getCorrect()) &&
                condition.apply(criteria.getCreateDate()) &&
                condition.apply(criteria.getWinPoints()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StudentAnswerLogCriteria> copyFiltersAre(
        StudentAnswerLogCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStudentId(), copy.getStudentId()) &&
                condition.apply(criteria.getQuestionId(), copy.getQuestionId()) &&
                condition.apply(criteria.getAnswer(), copy.getAnswer()) &&
                condition.apply(criteria.getCorrect(), copy.getCorrect()) &&
                condition.apply(criteria.getCreateDate(), copy.getCreateDate()) &&
                condition.apply(criteria.getWinPoints(), copy.getWinPoints()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
