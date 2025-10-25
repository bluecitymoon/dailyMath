package com.turling.service.mapper;

import static com.turling.domain.StudentAnswerLogAsserts.*;
import static com.turling.domain.StudentAnswerLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentAnswerLogMapperTest {

    private StudentAnswerLogMapper studentAnswerLogMapper;

    @BeforeEach
    void setUp() {
        studentAnswerLogMapper = new StudentAnswerLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudentAnswerLogSample1();
        var actual = studentAnswerLogMapper.toEntity(studentAnswerLogMapper.toDto(expected));
        assertStudentAnswerLogAllPropertiesEquals(expected, actual);
    }
}
