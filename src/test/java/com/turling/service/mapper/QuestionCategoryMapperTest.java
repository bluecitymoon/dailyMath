package com.turling.service.mapper;

import static com.turling.domain.QuestionCategoryAsserts.*;
import static com.turling.domain.QuestionCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionCategoryMapperTest {

    private QuestionCategoryMapper questionCategoryMapper;

    @BeforeEach
    void setUp() {
        questionCategoryMapper = new QuestionCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionCategorySample1();
        var actual = questionCategoryMapper.toEntity(questionCategoryMapper.toDto(expected));
        assertQuestionCategoryAllPropertiesEquals(expected, actual);
    }
}
