package com.turling.service.mapper;

import static com.turling.domain.CommunityAsserts.*;
import static com.turling.domain.CommunityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommunityMapperTest {

    private CommunityMapper communityMapper;

    @BeforeEach
    void setUp() {
        communityMapper = new CommunityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommunitySample1();
        var actual = communityMapper.toEntity(communityMapper.toDto(expected));
        assertCommunityAllPropertiesEquals(expected, actual);
    }
}
