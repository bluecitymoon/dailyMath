package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.QuestionCategory;
import com.turling.repository.QuestionCategoryRepository;
import com.turling.service.criteria.QuestionCategoryCriteria;
import com.turling.service.dto.QuestionCategoryDTO;
import com.turling.service.mapper.QuestionCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link QuestionCategory} entities in the database.
 * The main input is a {@link QuestionCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuestionCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionCategoryQueryService extends QueryService<QuestionCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionCategoryQueryService.class);

    private final QuestionCategoryRepository questionCategoryRepository;

    private final QuestionCategoryMapper questionCategoryMapper;

    public QuestionCategoryQueryService(
        QuestionCategoryRepository questionCategoryRepository,
        QuestionCategoryMapper questionCategoryMapper
    ) {
        this.questionCategoryRepository = questionCategoryRepository;
        this.questionCategoryMapper = questionCategoryMapper;
    }

    /**
     * Return a {@link Page} of {@link QuestionCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionCategoryDTO> findByCriteria(QuestionCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestionCategory> specification = createSpecification(criteria);
        return questionCategoryRepository.findAll(specification, page).map(questionCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuestionCategory> specification = createSpecification(criteria);
        return questionCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestionCategory> createSpecification(QuestionCategoryCriteria criteria) {
        Specification<QuestionCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), QuestionCategory_.id),
                buildStringSpecification(criteria.getName(), QuestionCategory_.name)
            );
        }
        return specification;
    }
}
