package com.turling.service.mapper;

import com.turling.domain.QuestionBaseGroup;
import com.turling.service.dto.QuestionBaseGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionBaseGroup} and its DTO {@link QuestionBaseGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionBaseGroupMapper extends EntityMapper<QuestionBaseGroupDTO, QuestionBaseGroup> {}
