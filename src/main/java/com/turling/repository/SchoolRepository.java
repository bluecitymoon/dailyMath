package com.turling.repository;

import com.turling.domain.School;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the School entity.
 */
@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School> {
    default Optional<School> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<School> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<School> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select school from School school left join fetch school.distinct",
        countQuery = "select count(school) from School school"
    )
    Page<School> findAllWithToOneRelationships(Pageable pageable);

    @Query("select school from School school left join fetch school.distinct")
    List<School> findAllWithToOneRelationships();

    @Query("select school from School school left join fetch school.distinct where school.id =:id")
    Optional<School> findOneWithToOneRelationships(@Param("id") Long id);
}
