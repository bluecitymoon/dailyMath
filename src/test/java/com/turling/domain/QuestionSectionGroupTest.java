package com.turling.domain;

import static com.turling.domain.QuestionSectionGroupTestSamples.*;
import static com.turling.domain.StudentGradeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionSectionGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionSectionGroup.class);
        QuestionSectionGroup questionSectionGroup1 = getQuestionSectionGroupSample1();
        QuestionSectionGroup questionSectionGroup2 = new QuestionSectionGroup();
        assertThat(questionSectionGroup1).isNotEqualTo(questionSectionGroup2);

        questionSectionGroup2.setId(questionSectionGroup1.getId());
        assertThat(questionSectionGroup1).isEqualTo(questionSectionGroup2);

        questionSectionGroup2 = getQuestionSectionGroupSample2();
        assertThat(questionSectionGroup1).isNotEqualTo(questionSectionGroup2);
    }

    @Test
    void gradeTest() {
        QuestionSectionGroup questionSectionGroup = getQuestionSectionGroupRandomSampleGenerator();
        StudentGrade studentGradeBack = getStudentGradeRandomSampleGenerator();

        questionSectionGroup.setGrade(studentGradeBack);
        assertThat(questionSectionGroup.getGrade()).isEqualTo(studentGradeBack);

        questionSectionGroup.grade(null);
        assertThat(questionSectionGroup.getGrade()).isNull();
    }
}
