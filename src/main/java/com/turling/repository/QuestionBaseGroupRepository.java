package com.turling.repository;

import com.turling.domain.QuestionBaseGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuestionBaseGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionBaseGroupRepository extends JpaRepository<QuestionBaseGroup, Long>, JpaSpecificationExecutor<QuestionBaseGroup> {}
