package com.turling.service.mapper;

import com.turling.domain.QuestionType;
import com.turling.service.dto.QuestionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionType} and its DTO {@link QuestionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionTypeMapper extends EntityMapper<QuestionTypeDTO, QuestionType> {}
