package com.turling.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentGradeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentGradeDTO.class);
        StudentGradeDTO studentGradeDTO1 = new StudentGradeDTO();
        studentGradeDTO1.setId(1L);
        StudentGradeDTO studentGradeDTO2 = new StudentGradeDTO();
        assertThat(studentGradeDTO1).isNotEqualTo(studentGradeDTO2);
        studentGradeDTO2.setId(studentGradeDTO1.getId());
        assertThat(studentGradeDTO1).isEqualTo(studentGradeDTO2);
        studentGradeDTO2.setId(2L);
        assertThat(studentGradeDTO1).isNotEqualTo(studentGradeDTO2);
        studentGradeDTO1.setId(null);
        assertThat(studentGradeDTO1).isNotEqualTo(studentGradeDTO2);
    }
}
