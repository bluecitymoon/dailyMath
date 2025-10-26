package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.StudentSectionLog;
import com.turling.repository.StudentSectionLogRepository;
import com.turling.service.criteria.StudentSectionLogCriteria;
import com.turling.service.dto.StudentSectionLogDTO;
import com.turling.service.mapper.StudentSectionLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StudentSectionLog} entities in the database.
 * The main input is a {@link StudentSectionLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StudentSectionLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentSectionLogQueryService extends QueryService<StudentSectionLog> {

    private static final Logger LOG = LoggerFactory.getLogger(StudentSectionLogQueryService.class);

    private final StudentSectionLogRepository studentSectionLogRepository;

    private final StudentSectionLogMapper studentSectionLogMapper;

    public StudentSectionLogQueryService(
        StudentSectionLogRepository studentSectionLogRepository,
        StudentSectionLogMapper studentSectionLogMapper
    ) {
        this.studentSectionLogRepository = studentSectionLogRepository;
        this.studentSectionLogMapper = studentSectionLogMapper;
    }

    /**
     * Return a {@link Page} of {@link StudentSectionLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentSectionLogDTO> findByCriteria(StudentSectionLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentSectionLog> specification = createSpecification(criteria);
        return studentSectionLogRepository.findAll(specification, page).map(studentSectionLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentSectionLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StudentSectionLog> specification = createSpecification(criteria);
        return studentSectionLogRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentSectionLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentSectionLog> createSpecification(StudentSectionLogCriteria criteria) {
        Specification<StudentSectionLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), StudentSectionLog_.id),
                buildRangeSpecification(criteria.getStudentId(), StudentSectionLog_.studentId),
                buildRangeSpecification(criteria.getSectionId(), StudentSectionLog_.sectionId),
                buildRangeSpecification(criteria.getTotalCount(), StudentSectionLog_.totalCount),
                buildRangeSpecification(criteria.getFinishedCount(), StudentSectionLog_.finishedCount),
                buildRangeSpecification(criteria.getCorrectRate(), StudentSectionLog_.correctRate),
                buildRangeSpecification(criteria.getCreateDate(), StudentSectionLog_.createDate),
                buildRangeSpecification(criteria.getUpdateDate(), StudentSectionLog_.updateDate)
            );
        }
        return specification;
    }
}
