package com.turling.service.mapper;

import static com.turling.domain.SchoolAsserts.*;
import static com.turling.domain.SchoolTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchoolMapperTest {

    private SchoolMapper schoolMapper;

    @BeforeEach
    void setUp() {
        schoolMapper = new SchoolMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSchoolSample1();
        var actual = schoolMapper.toEntity(schoolMapper.toDto(expected));
        assertSchoolAllPropertiesEquals(expected, actual);
    }
}
