package com.turling.service.mapper;

import com.turling.domain.Distinct;
import com.turling.service.dto.DistinctDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Distinct} and its DTO {@link DistinctDTO}.
 */
@Mapper(componentModel = "spring")
public interface DistinctMapper extends EntityMapper<DistinctDTO, Distinct> {}
