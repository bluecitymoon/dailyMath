package com.turling.repository;

import com.turling.domain.Community;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Community entity.
 */
@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>, JpaSpecificationExecutor<Community> {
    default Optional<Community> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Community> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Community> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select community from Community community left join fetch community.distinct",
        countQuery = "select count(community) from Community community"
    )
    Page<Community> findAllWithToOneRelationships(Pageable pageable);

    @Query("select community from Community community left join fetch community.distinct")
    List<Community> findAllWithToOneRelationships();

    @Query("select community from Community community left join fetch community.distinct where community.id =:id")
    Optional<Community> findOneWithToOneRelationships(@Param("id") Long id);
}
