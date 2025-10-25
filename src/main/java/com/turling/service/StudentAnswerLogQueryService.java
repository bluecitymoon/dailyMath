package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.StudentAnswerLog;
import com.turling.repository.StudentAnswerLogRepository;
import com.turling.service.criteria.StudentAnswerLogCriteria;
import com.turling.service.dto.StudentAnswerLogDTO;
import com.turling.service.mapper.StudentAnswerLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StudentAnswerLog} entities in the database.
 * The main input is a {@link StudentAnswerLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StudentAnswerLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentAnswerLogQueryService extends QueryService<StudentAnswerLog> {

    private static final Logger LOG = LoggerFactory.getLogger(StudentAnswerLogQueryService.class);

    private final StudentAnswerLogRepository studentAnswerLogRepository;

    private final StudentAnswerLogMapper studentAnswerLogMapper;

    public StudentAnswerLogQueryService(
        StudentAnswerLogRepository studentAnswerLogRepository,
        StudentAnswerLogMapper studentAnswerLogMapper
    ) {
        this.studentAnswerLogRepository = studentAnswerLogRepository;
        this.studentAnswerLogMapper = studentAnswerLogMapper;
    }

    /**
     * Return a {@link Page} of {@link StudentAnswerLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentAnswerLogDTO> findByCriteria(StudentAnswerLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentAnswerLog> specification = createSpecification(criteria);
        return studentAnswerLogRepository.findAll(specification, page).map(studentAnswerLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentAnswerLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StudentAnswerLog> specification = createSpecification(criteria);
        return studentAnswerLogRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentAnswerLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentAnswerLog> createSpecification(StudentAnswerLogCriteria criteria) {
        Specification<StudentAnswerLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), StudentAnswerLog_.id),
                buildRangeSpecification(criteria.getStudentId(), StudentAnswerLog_.studentId),
                buildRangeSpecification(criteria.getQuestionId(), StudentAnswerLog_.questionId),
                buildStringSpecification(criteria.getAnswer(), StudentAnswerLog_.answer),
                buildRangeSpecification(criteria.getCorrect(), StudentAnswerLog_.correct),
                buildRangeSpecification(criteria.getCreateDate(), StudentAnswerLog_.createDate),
                buildRangeSpecification(criteria.getWinPoints(), StudentAnswerLog_.winPoints)
            );
        }
        return specification;
    }
}
