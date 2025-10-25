package com.turling.domain;

import static com.turling.domain.CommunityTestSamples.*;
import static com.turling.domain.DistinctTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Community.class);
        Community community1 = getCommunitySample1();
        Community community2 = new Community();
        assertThat(community1).isNotEqualTo(community2);

        community2.setId(community1.getId());
        assertThat(community1).isEqualTo(community2);

        community2 = getCommunitySample2();
        assertThat(community1).isNotEqualTo(community2);
    }

    @Test
    void distinctTest() {
        Community community = getCommunityRandomSampleGenerator();
        Distinct distinctBack = getDistinctRandomSampleGenerator();

        community.setDistinct(distinctBack);
        assertThat(community.getDistinct()).isEqualTo(distinctBack);

        community.distinct(null);
        assertThat(community.getDistinct()).isNull();
    }
}
