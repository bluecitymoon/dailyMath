package com.turling.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentAnswerLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentAnswerLogDTO.class);
        StudentAnswerLogDTO studentAnswerLogDTO1 = new StudentAnswerLogDTO();
        studentAnswerLogDTO1.setId(1L);
        StudentAnswerLogDTO studentAnswerLogDTO2 = new StudentAnswerLogDTO();
        assertThat(studentAnswerLogDTO1).isNotEqualTo(studentAnswerLogDTO2);
        studentAnswerLogDTO2.setId(studentAnswerLogDTO1.getId());
        assertThat(studentAnswerLogDTO1).isEqualTo(studentAnswerLogDTO2);
        studentAnswerLogDTO2.setId(2L);
        assertThat(studentAnswerLogDTO1).isNotEqualTo(studentAnswerLogDTO2);
        studentAnswerLogDTO1.setId(null);
        assertThat(studentAnswerLogDTO1).isNotEqualTo(studentAnswerLogDTO2);
    }
}
