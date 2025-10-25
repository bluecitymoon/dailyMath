package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.QuestionSectionGroup;
import com.turling.repository.QuestionSectionGroupRepository;
import com.turling.service.criteria.QuestionSectionGroupCriteria;
import com.turling.service.dto.QuestionSectionGroupDTO;
import com.turling.service.mapper.QuestionSectionGroupMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link QuestionSectionGroup} entities in the database.
 * The main input is a {@link QuestionSectionGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuestionSectionGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionSectionGroupQueryService extends QueryService<QuestionSectionGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionSectionGroupQueryService.class);

    private final QuestionSectionGroupRepository questionSectionGroupRepository;

    private final QuestionSectionGroupMapper questionSectionGroupMapper;

    public QuestionSectionGroupQueryService(
        QuestionSectionGroupRepository questionSectionGroupRepository,
        QuestionSectionGroupMapper questionSectionGroupMapper
    ) {
        this.questionSectionGroupRepository = questionSectionGroupRepository;
        this.questionSectionGroupMapper = questionSectionGroupMapper;
    }

    /**
     * Return a {@link Page} of {@link QuestionSectionGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionSectionGroupDTO> findByCriteria(QuestionSectionGroupCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestionSectionGroup> specification = createSpecification(criteria);
        return questionSectionGroupRepository.findAll(specification, page).map(questionSectionGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionSectionGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuestionSectionGroup> specification = createSpecification(criteria);
        return questionSectionGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionSectionGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestionSectionGroup> createSpecification(QuestionSectionGroupCriteria criteria) {
        Specification<QuestionSectionGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), QuestionSectionGroup_.id),
                buildStringSpecification(criteria.getTitle(), QuestionSectionGroup_.title),
                buildStringSpecification(criteria.getBaseGroupIds(), QuestionSectionGroup_.baseGroupIds),
                buildSpecification(criteria.getGradeId(), root ->
                    root.join(QuestionSectionGroup_.grade, JoinType.LEFT).get(StudentGrade_.id)
                )
            );
        }
        return specification;
    }
}
