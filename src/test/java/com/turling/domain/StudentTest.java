package com.turling.domain;

import static com.turling.domain.CommunityTestSamples.*;
import static com.turling.domain.SchoolTestSamples.*;
import static com.turling.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void schoolTest() {
        Student student = getStudentRandomSampleGenerator();
        School schoolBack = getSchoolRandomSampleGenerator();

        student.setSchool(schoolBack);
        assertThat(student.getSchool()).isEqualTo(schoolBack);

        student.school(null);
        assertThat(student.getSchool()).isNull();
    }

    @Test
    void communityTest() {
        Student student = getStudentRandomSampleGenerator();
        Community communityBack = getCommunityRandomSampleGenerator();

        student.setCommunity(communityBack);
        assertThat(student.getCommunity()).isEqualTo(communityBack);

        student.community(null);
        assertThat(student.getCommunity()).isNull();
    }
}
