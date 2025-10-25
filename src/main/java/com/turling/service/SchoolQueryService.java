package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.School;
import com.turling.repository.SchoolRepository;
import com.turling.service.criteria.SchoolCriteria;
import com.turling.service.dto.SchoolDTO;
import com.turling.service.mapper.SchoolMapper;
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
 * Service for executing complex queries for {@link School} entities in the database.
 * The main input is a {@link SchoolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SchoolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolQueryService extends QueryService<School> {

    private static final Logger LOG = LoggerFactory.getLogger(SchoolQueryService.class);

    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    public SchoolQueryService(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
    }

    /**
     * Return a {@link Page} of {@link SchoolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolDTO> findByCriteria(SchoolCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<School> specification = createSpecification(criteria);
        return schoolRepository.findAll(specification, page).map(schoolMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<School> specification = createSpecification(criteria);
        return schoolRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<School> createSpecification(SchoolCriteria criteria) {
        Specification<School> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), School_.id),
                buildStringSpecification(criteria.getName(), School_.name),
                buildRangeSpecification(criteria.getRegisteredStudentsCount(), School_.registeredStudentsCount),
                buildStringSpecification(criteria.getPinyin(), School_.pinyin),
                buildSpecification(criteria.getDistinctId(), root -> root.join(School_.distinct, JoinType.LEFT).get(Distinct_.id))
            );
        }
        return specification;
    }
}
