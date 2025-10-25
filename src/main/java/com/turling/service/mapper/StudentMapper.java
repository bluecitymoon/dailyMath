package com.turling.service.mapper;

import com.turling.domain.Community;
import com.turling.domain.School;
import com.turling.domain.Student;
import com.turling.service.dto.CommunityDTO;
import com.turling.service.dto.SchoolDTO;
import com.turling.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {
    @Mapping(target = "school", source = "school", qualifiedByName = "schoolName")
    @Mapping(target = "community", source = "community", qualifiedByName = "communityName")
    StudentDTO toDto(Student s);

    @Named("schoolName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SchoolDTO toDtoSchoolName(School school);

    @Named("communityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CommunityDTO toDtoCommunityName(Community community);
}
