package com.turling.domain;

import static com.turling.domain.QuestionCategoryTestSamples.*;
import static com.turling.domain.QuestionOptionTestSamples.*;
import static com.turling.domain.QuestionTestSamples.*;
import static com.turling.domain.QuestionTypeTestSamples.*;
import static com.turling.domain.StudentGradeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void questionCategoryTest() {
        Question question = getQuestionRandomSampleGenerator();
        QuestionCategory questionCategoryBack = getQuestionCategoryRandomSampleGenerator();

        question.setQuestionCategory(questionCategoryBack);
        assertThat(question.getQuestionCategory()).isEqualTo(questionCategoryBack);

        question.questionCategory(null);
        assertThat(question.getQuestionCategory()).isNull();
    }

    @Test
    void typeTest() {
        Question question = getQuestionRandomSampleGenerator();
        QuestionType questionTypeBack = getQuestionTypeRandomSampleGenerator();

        question.setType(questionTypeBack);
        assertThat(question.getType()).isEqualTo(questionTypeBack);

        question.type(null);
        assertThat(question.getType()).isNull();
    }

    @Test
    void gradeTest() {
        Question question = getQuestionRandomSampleGenerator();
        StudentGrade studentGradeBack = getStudentGradeRandomSampleGenerator();

        question.setGrade(studentGradeBack);
        assertThat(question.getGrade()).isEqualTo(studentGradeBack);

        question.grade(null);
        assertThat(question.getGrade()).isNull();
    }

    @Test
    void optionsTest() {
        Question question = getQuestionRandomSampleGenerator();
        QuestionOption questionOptionBack = getQuestionOptionRandomSampleGenerator();

        question.addOptions(questionOptionBack);
        assertThat(question.getOptions()).containsOnly(questionOptionBack);
        assertThat(questionOptionBack.getQuestion()).isEqualTo(question);

        question.removeOptions(questionOptionBack);
        assertThat(question.getOptions()).doesNotContain(questionOptionBack);
        assertThat(questionOptionBack.getQuestion()).isNull();

        question.options(new HashSet<>(Set.of(questionOptionBack)));
        assertThat(question.getOptions()).containsOnly(questionOptionBack);
        assertThat(questionOptionBack.getQuestion()).isEqualTo(question);

        question.setOptions(new HashSet<>());
        assertThat(question.getOptions()).doesNotContain(questionOptionBack);
        assertThat(questionOptionBack.getQuestion()).isNull();
    }
}
