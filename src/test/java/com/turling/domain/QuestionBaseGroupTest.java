package com.turling.domain;

import static com.turling.domain.QuestionBaseGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionBaseGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionBaseGroup.class);
        QuestionBaseGroup questionBaseGroup1 = getQuestionBaseGroupSample1();
        QuestionBaseGroup questionBaseGroup2 = new QuestionBaseGroup();
        assertThat(questionBaseGroup1).isNotEqualTo(questionBaseGroup2);

        questionBaseGroup2.setId(questionBaseGroup1.getId());
        assertThat(questionBaseGroup1).isEqualTo(questionBaseGroup2);

        questionBaseGroup2 = getQuestionBaseGroupSample2();
        assertThat(questionBaseGroup1).isNotEqualTo(questionBaseGroup2);
    }
}
