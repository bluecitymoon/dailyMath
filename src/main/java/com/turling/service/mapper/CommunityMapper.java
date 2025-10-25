package com.turling.service.mapper;

import com.turling.domain.Community;
import com.turling.domain.Distinct;
import com.turling.service.dto.CommunityDTO;
import com.turling.service.dto.DistinctDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Community} and its DTO {@link CommunityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommunityMapper extends EntityMapper<CommunityDTO, Community> {
    @Mapping(target = "distinct", source = "distinct", qualifiedByName = "distinctName")
    CommunityDTO toDto(Community s);

    @Named("distinctName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DistinctDTO toDtoDistinctName(Distinct distinct);
}
