package com.turling.service.mapper;

import static com.turling.domain.StudentGradeAsserts.*;
import static com.turling.domain.StudentGradeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentGradeMapperTest {

    private StudentGradeMapper studentGradeMapper;

    @BeforeEach
    void setUp() {
        studentGradeMapper = new StudentGradeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudentGradeSample1();
        var actual = studentGradeMapper.toEntity(studentGradeMapper.toDto(expected));
        assertStudentGradeAllPropertiesEquals(expected, actual);
    }
}
