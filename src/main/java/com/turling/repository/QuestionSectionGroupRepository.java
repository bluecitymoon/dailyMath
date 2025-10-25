package com.turling.repository;

import com.turling.domain.QuestionSectionGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuestionSectionGroup entity.
 */
@Repository
public interface QuestionSectionGroupRepository
    extends JpaRepository<QuestionSectionGroup, Long>, JpaSpecificationExecutor<QuestionSectionGroup> {
    default Optional<QuestionSectionGroup> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QuestionSectionGroup> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QuestionSectionGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select questionSectionGroup from QuestionSectionGroup questionSectionGroup left join fetch questionSectionGroup.grade",
        countQuery = "select count(questionSectionGroup) from QuestionSectionGroup questionSectionGroup"
    )
    Page<QuestionSectionGroup> findAllWithToOneRelationships(Pageable pageable);

    @Query("select questionSectionGroup from QuestionSectionGroup questionSectionGroup left join fetch questionSectionGroup.grade")
    List<QuestionSectionGroup> findAllWithToOneRelationships();

    @Query(
        "select questionSectionGroup from QuestionSectionGroup questionSectionGroup left join fetch questionSectionGroup.grade where questionSectionGroup.id =:id"
    )
    Optional<QuestionSectionGroup> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select questionSectionGroup from QuestionSectionGroup questionSectionGroup left join fetch questionSectionGroup.grade order by questionSectionGroup.displayOrder ASC NULLS LAST, questionSectionGroup.id ASC"
    )
    List<QuestionSectionGroup> findAllOrderByDisplayOrder();

    @Query(
        value = "select questionSectionGroup from QuestionSectionGroup questionSectionGroup left join fetch questionSectionGroup.grade order by questionSectionGroup.displayOrder ASC NULLS LAST, questionSectionGroup.id ASC",
        countQuery = "select count(questionSectionGroup) from QuestionSectionGroup questionSectionGroup"
    )
    Page<QuestionSectionGroup> findAllOrderByDisplayOrder(Pageable pageable);

    @Query("select max(qsg.displayOrder) from QuestionSectionGroup qsg")
    Optional<Integer> findMaxDisplayOrder();

    @Query(
        "select questionSectionGroup from QuestionSectionGroup questionSectionGroup left join fetch questionSectionGroup.grade where questionSectionGroup.grade.id = :gradeId order by questionSectionGroup.displayOrder ASC NULLS LAST, questionSectionGroup.id ASC"
    )
    List<QuestionSectionGroup> findByGradeIdOrderByDisplayOrder(@Param("gradeId") Long gradeId);

    @Query(
        value = "select questionSectionGroup from QuestionSectionGroup questionSectionGroup left join fetch questionSectionGroup.grade where questionSectionGroup.grade.id = :gradeId order by questionSectionGroup.displayOrder ASC NULLS LAST, questionSectionGroup.id ASC",
        countQuery = "select count(questionSectionGroup) from QuestionSectionGroup questionSectionGroup where questionSectionGroup.grade.id = :gradeId"
    )
    Page<QuestionSectionGroup> findByGradeIdOrderByDisplayOrder(@Param("gradeId") Long gradeId, Pageable pageable);
}
