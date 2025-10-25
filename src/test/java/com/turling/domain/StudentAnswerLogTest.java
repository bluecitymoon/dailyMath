package com.turling.domain;

import static com.turling.domain.StudentAnswerLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentAnswerLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentAnswerLog.class);
        StudentAnswerLog studentAnswerLog1 = getStudentAnswerLogSample1();
        StudentAnswerLog studentAnswerLog2 = new StudentAnswerLog();
        assertThat(studentAnswerLog1).isNotEqualTo(studentAnswerLog2);

        studentAnswerLog2.setId(studentAnswerLog1.getId());
        assertThat(studentAnswerLog1).isEqualTo(studentAnswerLog2);

        studentAnswerLog2 = getStudentAnswerLogSample2();
        assertThat(studentAnswerLog1).isNotEqualTo(studentAnswerLog2);
    }
}
