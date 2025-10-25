package com.turling.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.turling.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DistinctDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DistinctDTO.class);
        DistinctDTO distinctDTO1 = new DistinctDTO();
        distinctDTO1.setId(1L);
        DistinctDTO distinctDTO2 = new DistinctDTO();
        assertThat(distinctDTO1).isNotEqualTo(distinctDTO2);
        distinctDTO2.setId(distinctDTO1.getId());
        assertThat(distinctDTO1).isEqualTo(distinctDTO2);
        distinctDTO2.setId(2L);
        assertThat(distinctDTO1).isNotEqualTo(distinctDTO2);
        distinctDTO1.setId(null);
        assertThat(distinctDTO1).isNotEqualTo(distinctDTO2);
    }
}
