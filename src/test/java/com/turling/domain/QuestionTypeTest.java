package com.turling.domain;

import static com.turling.domain.QuestionTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionType.class);
        QuestionType questionType1 = getQuestionTypeSample1();
        QuestionType questionType2 = new QuestionType();
        assertThat(questionType1).isNotEqualTo(questionType2);

        questionType2.setId(questionType1.getId());
        assertThat(questionType1).isEqualTo(questionType2);

        questionType2 = getQuestionTypeSample2();
        assertThat(questionType1).isNotEqualTo(questionType2);
    }
}
