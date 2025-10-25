package com.turling.service.mapper;

import static com.turling.domain.DistinctAsserts.*;
import static com.turling.domain.DistinctTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DistinctMapperTest {

    private DistinctMapper distinctMapper;

    @BeforeEach
    void setUp() {
        distinctMapper = new DistinctMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDistinctSample1();
        var actual = distinctMapper.toEntity(distinctMapper.toDto(expected));
        assertDistinctAllPropertiesEquals(expected, actual);
    }
}
