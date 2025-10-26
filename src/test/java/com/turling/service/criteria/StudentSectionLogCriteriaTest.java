package com.turling.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StudentSectionLogCriteriaTest {

    @Test
    void newStudentSectionLogCriteriaHasAllFiltersNullTest() {
        var studentSectionLogCriteria = new StudentSectionLogCriteria();
        assertThat(studentSectionLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void studentSectionLogCriteriaFluentMethodsCreatesFiltersTest() {
        var studentSectionLogCriteria = new StudentSectionLogCriteria();

        setAllFilters(studentSectionLogCriteria);

        assertThat(studentSectionLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void studentSectionLogCriteriaCopyCreatesNullFilterTest() {
        var studentSectionLogCriteria = new StudentSectionLogCriteria();
        var copy = studentSectionLogCriteria.copy();

        assertThat(studentSectionLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(studentSectionLogCriteria)
        );
    }

    @Test
    void studentSectionLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var studentSectionLogCriteria = new StudentSectionLogCriteria();
        setAllFilters(studentSectionLogCriteria);

        var copy = studentSectionLogCriteria.copy();

        assertThat(studentSectionLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(studentSectionLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var studentSectionLogCriteria = new StudentSectionLogCriteria();

        assertThat(studentSectionLogCriteria).hasToString("StudentSectionLogCriteria{}");
    }

    private static void setAllFilters(StudentSectionLogCriteria studentSectionLogCriteria) {
        studentSectionLogCriteria.id();
        studentSectionLogCriteria.studentId();
        studentSectionLogCriteria.sectionId();
        studentSectionLogCriteria.totalCount();
        studentSectionLogCriteria.finishedCount();
        studentSectionLogCriteria.correctRate();
        studentSectionLogCriteria.createDate();
        studentSectionLogCriteria.updateDate();
        studentSectionLogCriteria.distinct();
    }

    private static Condition<StudentSectionLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStudentId()) &&
                condition.apply(criteria.getSectionId()) &&
                condition.apply(criteria.getTotalCount()) &&
                condition.apply(criteria.getFinishedCount()) &&
                condition.apply(criteria.getCorrectRate()) &&
                condition.apply(criteria.getCreateDate()) &&
                condition.apply(criteria.getUpdateDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StudentSectionLogCriteria> copyFiltersAre(
        StudentSectionLogCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStudentId(), copy.getStudentId()) &&
                condition.apply(criteria.getSectionId(), copy.getSectionId()) &&
                condition.apply(criteria.getTotalCount(), copy.getTotalCount()) &&
                condition.apply(criteria.getFinishedCount(), copy.getFinishedCount()) &&
                condition.apply(criteria.getCorrectRate(), copy.getCorrectRate()) &&
                condition.apply(criteria.getCreateDate(), copy.getCreateDate()) &&
                condition.apply(criteria.getUpdateDate(), copy.getUpdateDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
