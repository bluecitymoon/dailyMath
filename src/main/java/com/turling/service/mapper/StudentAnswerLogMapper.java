package com.turling.service.mapper;

import com.turling.domain.StudentAnswerLog;
import com.turling.service.dto.StudentAnswerLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentAnswerLog} and its DTO {@link StudentAnswerLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentAnswerLogMapper extends EntityMapper<StudentAnswerLogDTO, StudentAnswerLog> {}
