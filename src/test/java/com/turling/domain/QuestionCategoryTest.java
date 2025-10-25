package com.turling.domain;

import static com.turling.domain.QuestionCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionCategory.class);
        QuestionCategory questionCategory1 = getQuestionCategorySample1();
        QuestionCategory questionCategory2 = new QuestionCategory();
        assertThat(questionCategory1).isNotEqualTo(questionCategory2);

        questionCategory2.setId(questionCategory1.getId());
        assertThat(questionCategory1).isEqualTo(questionCategory2);

        questionCategory2 = getQuestionCategorySample2();
        assertThat(questionCategory1).isNotEqualTo(questionCategory2);
    }
}
