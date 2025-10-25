package com.turling.repository;

import com.turling.domain.Distinct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Distinct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistinctRepository extends JpaRepository<Distinct, Long>, JpaSpecificationExecutor<Distinct> {}
