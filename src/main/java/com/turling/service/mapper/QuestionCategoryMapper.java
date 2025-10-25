package com.turling.service.mapper;

import com.turling.domain.QuestionCategory;
import com.turling.service.dto.QuestionCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionCategory} and its DTO {@link QuestionCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionCategoryMapper extends EntityMapper<QuestionCategoryDTO, QuestionCategory> {}
