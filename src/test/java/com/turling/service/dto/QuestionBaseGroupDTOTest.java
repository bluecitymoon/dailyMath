package com.turling.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionBaseGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionBaseGroupDTO.class);
        QuestionBaseGroupDTO questionBaseGroupDTO1 = new QuestionBaseGroupDTO();
        questionBaseGroupDTO1.setId(1L);
        QuestionBaseGroupDTO questionBaseGroupDTO2 = new QuestionBaseGroupDTO();
        assertThat(questionBaseGroupDTO1).isNotEqualTo(questionBaseGroupDTO2);
        questionBaseGroupDTO2.setId(questionBaseGroupDTO1.getId());
        assertThat(questionBaseGroupDTO1).isEqualTo(questionBaseGroupDTO2);
        questionBaseGroupDTO2.setId(2L);
        assertThat(questionBaseGroupDTO1).isNotEqualTo(questionBaseGroupDTO2);
        questionBaseGroupDTO1.setId(null);
        assertThat(questionBaseGroupDTO1).isNotEqualTo(questionBaseGroupDTO2);
    }
}
