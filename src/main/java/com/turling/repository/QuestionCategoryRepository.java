package com.turling.repository;

import com.turling.domain.QuestionCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuestionCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long>, JpaSpecificationExecutor<QuestionCategory> {}
