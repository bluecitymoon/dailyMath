package com.turling.domain;

import static com.turling.domain.StudentGradeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentGradeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentGrade.class);
        StudentGrade studentGrade1 = getStudentGradeSample1();
        StudentGrade studentGrade2 = new StudentGrade();
        assertThat(studentGrade1).isNotEqualTo(studentGrade2);

        studentGrade2.setId(studentGrade1.getId());
        assertThat(studentGrade1).isEqualTo(studentGrade2);

        studentGrade2 = getStudentGradeSample2();
        assertThat(studentGrade1).isNotEqualTo(studentGrade2);
    }
}
