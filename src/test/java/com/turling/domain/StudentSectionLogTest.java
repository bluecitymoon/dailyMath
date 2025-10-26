package com.turling.domain;

import static com.turling.domain.StudentSectionLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentSectionLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentSectionLog.class);
        StudentSectionLog studentSectionLog1 = getStudentSectionLogSample1();
        StudentSectionLog studentSectionLog2 = new StudentSectionLog();
        assertThat(studentSectionLog1).isNotEqualTo(studentSectionLog2);

        studentSectionLog2.setId(studentSectionLog1.getId());
        assertThat(studentSectionLog1).isEqualTo(studentSectionLog2);

        studentSectionLog2 = getStudentSectionLogSample2();
        assertThat(studentSectionLog1).isNotEqualTo(studentSectionLog2);
    }
}
