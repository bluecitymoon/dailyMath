package com.turling.repository;

import com.turling.domain.StudentSectionLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentSectionLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentSectionLogRepository extends JpaRepository<StudentSectionLog, Long>, JpaSpecificationExecutor<StudentSectionLog> {}
