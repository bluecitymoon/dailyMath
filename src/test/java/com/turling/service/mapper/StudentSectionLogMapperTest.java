package com.turling.service.mapper;

import static com.turling.domain.StudentSectionLogAsserts.*;
import static com.turling.domain.StudentSectionLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentSectionLogMapperTest {

    private StudentSectionLogMapper studentSectionLogMapper;

    @BeforeEach
    void setUp() {
        studentSectionLogMapper = new StudentSectionLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudentSectionLogSample1();
        var actual = studentSectionLogMapper.toEntity(studentSectionLogMapper.toDto(expected));
        assertStudentSectionLogAllPropertiesEquals(expected, actual);
    }
}
