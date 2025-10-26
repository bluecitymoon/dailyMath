package com.turling.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentSectionLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentSectionLogDTO.class);
        StudentSectionLogDTO studentSectionLogDTO1 = new StudentSectionLogDTO();
        studentSectionLogDTO1.setId(1L);
        StudentSectionLogDTO studentSectionLogDTO2 = new StudentSectionLogDTO();
        assertThat(studentSectionLogDTO1).isNotEqualTo(studentSectionLogDTO2);
        studentSectionLogDTO2.setId(studentSectionLogDTO1.getId());
        assertThat(studentSectionLogDTO1).isEqualTo(studentSectionLogDTO2);
        studentSectionLogDTO2.setId(2L);
        assertThat(studentSectionLogDTO1).isNotEqualTo(studentSectionLogDTO2);
        studentSectionLogDTO1.setId(null);
        assertThat(studentSectionLogDTO1).isNotEqualTo(studentSectionLogDTO2);
    }
}
