package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.Distinct;
import com.turling.repository.DistinctRepository;
import com.turling.service.criteria.DistinctCriteria;
import com.turling.service.dto.DistinctDTO;
import com.turling.service.mapper.DistinctMapper;
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
 * Service for executing complex queries for {@link Distinct} entities in the database.
 * The main input is a {@link DistinctCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DistinctDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DistinctQueryService extends QueryService<Distinct> {

    private static final Logger LOG = LoggerFactory.getLogger(DistinctQueryService.class);

    private final DistinctRepository distinctRepository;

    private final DistinctMapper distinctMapper;

    public DistinctQueryService(DistinctRepository distinctRepository, DistinctMapper distinctMapper) {
        this.distinctRepository = distinctRepository;
        this.distinctMapper = distinctMapper;
    }

    /**
     * Return a {@link Page} of {@link DistinctDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DistinctDTO> findByCriteria(DistinctCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Distinct> specification = createSpecification(criteria);
        return distinctRepository.findAll(specification, page).map(distinctMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DistinctCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Distinct> specification = createSpecification(criteria);
        return distinctRepository.count(specification);
    }

    /**
     * Function to convert {@link DistinctCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Distinct> createSpecification(DistinctCriteria criteria) {
        Specification<Distinct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Distinct_.id),
                buildStringSpecification(criteria.getName(), Distinct_.name),
                buildStringSpecification(criteria.getPinyin(), Distinct_.pinyin),
                buildSpecification(criteria.getCommunitiesId(), root -> root.join(Distinct_.communities, JoinType.LEFT).get(Community_.id))
            );
        }
        return specification;
    }
}
