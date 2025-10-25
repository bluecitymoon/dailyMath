package com.turling.service.mapper;

import static com.turling.domain.QuestionBaseGroupAsserts.*;
import static com.turling.domain.QuestionBaseGroupTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionBaseGroupMapperTest {

    private QuestionBaseGroupMapper questionBaseGroupMapper;

    @BeforeEach
    void setUp() {
        questionBaseGroupMapper = new QuestionBaseGroupMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionBaseGroupSample1();
        var actual = questionBaseGroupMapper.toEntity(questionBaseGroupMapper.toDto(expected));
        assertQuestionBaseGroupAllPropertiesEquals(expected, actual);
    }
}
