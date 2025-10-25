package com.turling.service.mapper;

import com.turling.domain.Distinct;
import com.turling.domain.School;
import com.turling.service.dto.DistinctDTO;
import com.turling.service.dto.SchoolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link School} and its DTO {@link SchoolDTO}.
 */
@Mapper(componentModel = "spring")
public interface SchoolMapper extends EntityMapper<SchoolDTO, School> {
    @Mapping(target = "distinct", source = "distinct", qualifiedByName = "distinctName")
    SchoolDTO toDto(School s);

    @Named("distinctName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DistinctDTO toDtoDistinctName(Distinct distinct);
}
