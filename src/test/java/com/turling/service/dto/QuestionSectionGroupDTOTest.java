package com.turling.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionSectionGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionSectionGroupDTO.class);
        QuestionSectionGroupDTO questionSectionGroupDTO1 = new QuestionSectionGroupDTO();
        questionSectionGroupDTO1.setId(1L);
        QuestionSectionGroupDTO questionSectionGroupDTO2 = new QuestionSectionGroupDTO();
        assertThat(questionSectionGroupDTO1).isNotEqualTo(questionSectionGroupDTO2);
        questionSectionGroupDTO2.setId(questionSectionGroupDTO1.getId());
        assertThat(questionSectionGroupDTO1).isEqualTo(questionSectionGroupDTO2);
        questionSectionGroupDTO2.setId(2L);
        assertThat(questionSectionGroupDTO1).isNotEqualTo(questionSectionGroupDTO2);
        questionSectionGroupDTO1.setId(null);
        assertThat(questionSectionGroupDTO1).isNotEqualTo(questionSectionGroupDTO2);
    }
}
