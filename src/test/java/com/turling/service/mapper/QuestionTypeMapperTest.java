package com.turling.service.mapper;

import static com.turling.domain.QuestionTypeAsserts.*;
import static com.turling.domain.QuestionTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionTypeMapperTest {

    private QuestionTypeMapper questionTypeMapper;

    @BeforeEach
    void setUp() {
        questionTypeMapper = new QuestionTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionTypeSample1();
        var actual = questionTypeMapper.toEntity(questionTypeMapper.toDto(expected));
        assertQuestionTypeAllPropertiesEquals(expected, actual);
    }
}
