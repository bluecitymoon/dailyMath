package com.turling.service.mapper;

import static com.turling.domain.QuestionSectionGroupAsserts.*;
import static com.turling.domain.QuestionSectionGroupTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionSectionGroupMapperTest {

    private QuestionSectionGroupMapper questionSectionGroupMapper;

    @BeforeEach
    void setUp() {
        questionSectionGroupMapper = new QuestionSectionGroupMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionSectionGroupSample1();
        var actual = questionSectionGroupMapper.toEntity(questionSectionGroupMapper.toDto(expected));
        assertQuestionSectionGroupAllPropertiesEquals(expected, actual);
    }
}
