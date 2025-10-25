package com.turling.service.mapper;

import static com.turling.domain.QuestionAsserts.*;
import static com.turling.domain.QuestionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionMapperTest {

    private QuestionMapper questionMapper;

    @BeforeEach
    void setUp() {
        questionMapper = new QuestionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionSample1();
        var actual = questionMapper.toEntity(questionMapper.toDto(expected));
        assertQuestionAllPropertiesEquals(expected, actual);
    }
}
