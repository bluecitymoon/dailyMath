package com.turling.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionCategoryDTO.class);
        QuestionCategoryDTO questionCategoryDTO1 = new QuestionCategoryDTO();
        questionCategoryDTO1.setId(1L);
        QuestionCategoryDTO questionCategoryDTO2 = new QuestionCategoryDTO();
        assertThat(questionCategoryDTO1).isNotEqualTo(questionCategoryDTO2);
        questionCategoryDTO2.setId(questionCategoryDTO1.getId());
        assertThat(questionCategoryDTO1).isEqualTo(questionCategoryDTO2);
        questionCategoryDTO2.setId(2L);
        assertThat(questionCategoryDTO1).isNotEqualTo(questionCategoryDTO2);
        questionCategoryDTO1.setId(null);
        assertThat(questionCategoryDTO1).isNotEqualTo(questionCategoryDTO2);
    }
}
