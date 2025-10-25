package com.turling.domain;

import static com.turling.domain.CommunityTestSamples.*;
import static com.turling.domain.DistinctTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DistinctTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distinct.class);
        Distinct distinct1 = getDistinctSample1();
        Distinct distinct2 = new Distinct();
        assertThat(distinct1).isNotEqualTo(distinct2);

        distinct2.setId(distinct1.getId());
        assertThat(distinct1).isEqualTo(distinct2);

        distinct2 = getDistinctSample2();
        assertThat(distinct1).isNotEqualTo(distinct2);
    }

    @Test
    void communitiesTest() {
        Distinct distinct = getDistinctRandomSampleGenerator();
        Community communityBack = getCommunityRandomSampleGenerator();

        distinct.addCommunities(communityBack);
        assertThat(distinct.getCommunities()).containsOnly(communityBack);
        assertThat(communityBack.getDistinct()).isEqualTo(distinct);

        distinct.removeCommunities(communityBack);
        assertThat(distinct.getCommunities()).doesNotContain(communityBack);
        assertThat(communityBack.getDistinct()).isNull();

        distinct.communities(new HashSet<>(Set.of(communityBack)));
        assertThat(distinct.getCommunities()).containsOnly(communityBack);
        assertThat(communityBack.getDistinct()).isEqualTo(distinct);

        distinct.setCommunities(new HashSet<>());
        assertThat(distinct.getCommunities()).doesNotContain(communityBack);
        assertThat(communityBack.getDistinct()).isNull();
    }
}
