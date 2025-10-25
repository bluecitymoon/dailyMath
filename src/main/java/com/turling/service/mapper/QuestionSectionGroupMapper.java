package com.turling.service.mapper;

import com.turling.domain.QuestionSectionGroup;
import com.turling.domain.StudentGrade;
import com.turling.service.dto.QuestionSectionGroupDTO;
import com.turling.service.dto.StudentGradeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionSectionGroup} and its DTO {@link QuestionSectionGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionSectionGroupMapper extends EntityMapper<QuestionSectionGroupDTO, QuestionSectionGroup> {
    @Mapping(target = "grade", source = "grade", qualifiedByName = "studentGradeName")
    QuestionSectionGroupDTO toDto(QuestionSectionGroup s);

    @Named("studentGradeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StudentGradeDTO toDtoStudentGradeName(StudentGrade studentGrade);
}
