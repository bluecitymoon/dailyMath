package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.QuestionBaseGroup;
import com.turling.repository.QuestionBaseGroupRepository;
import com.turling.service.criteria.QuestionBaseGroupCriteria;
import com.turling.service.dto.QuestionBaseGroupDTO;
import com.turling.service.mapper.QuestionBaseGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link QuestionBaseGroup} entities in the database.
 * The main input is a {@link QuestionBaseGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuestionBaseGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionBaseGroupQueryService extends QueryService<QuestionBaseGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionBaseGroupQueryService.class);

    private final QuestionBaseGroupRepository questionBaseGroupRepository;

    private final QuestionBaseGroupMapper questionBaseGroupMapper;

    public QuestionBaseGroupQueryService(
        QuestionBaseGroupRepository questionBaseGroupRepository,
        QuestionBaseGroupMapper questionBaseGroupMapper
    ) {
        this.questionBaseGroupRepository = questionBaseGroupRepository;
        this.questionBaseGroupMapper = questionBaseGroupMapper;
    }

    /**
     * Return a {@link Page} of {@link QuestionBaseGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionBaseGroupDTO> findByCriteria(QuestionBaseGroupCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestionBaseGroup> specification = createSpecification(criteria);
        return questionBaseGroupRepository.findAll(specification, page).map(questionBaseGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionBaseGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuestionBaseGroup> specification = createSpecification(criteria);
        return questionBaseGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionBaseGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestionBaseGroup> createSpecification(QuestionBaseGroupCriteria criteria) {
        Specification<QuestionBaseGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), QuestionBaseGroup_.id),
                buildStringSpecification(criteria.getTitle(), QuestionBaseGroup_.title),
                buildStringSpecification(criteria.getQuestionIds(), QuestionBaseGroup_.questionIds)
            );
        }
        return specification;
    }
}
