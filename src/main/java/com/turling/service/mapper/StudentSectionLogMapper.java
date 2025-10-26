package com.turling.service.mapper;

import com.turling.domain.StudentSectionLog;
import com.turling.service.dto.StudentSectionLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentSectionLog} and its DTO {@link StudentSectionLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentSectionLogMapper extends EntityMapper<StudentSectionLogDTO, StudentSectionLog> {}
