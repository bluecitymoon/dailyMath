package com.turling.service.mapper;

import com.turling.domain.StudentGrade;
import com.turling.service.dto.StudentGradeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentGrade} and its DTO {@link StudentGradeDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentGradeMapper extends EntityMapper<StudentGradeDTO, StudentGrade> {}
