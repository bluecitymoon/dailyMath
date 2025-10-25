package com.turling.service;

import com.turling.domain.*; // for static metamodels
import com.turling.domain.Student;
import com.turling.repository.StudentRepository;
import com.turling.service.criteria.StudentCriteria;
import com.turling.service.dto.StudentDTO;
import com.turling.service.mapper.StudentMapper;
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
 * Service for executing complex queries for {@link Student} entities in the database.
 * The main input is a {@link StudentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StudentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentQueryService extends QueryService<Student> {

    private static final Logger LOG = LoggerFactory.getLogger(StudentQueryService.class);

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public StudentQueryService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    /**
     * Return a {@link Page} of {@link StudentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentDTO> findByCriteria(StudentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification, page).map(studentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Student> createSpecification(StudentCriteria criteria) {
        Specification<Student> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Student_.id),
                buildStringSpecification(criteria.getName(), Student_.name),
                buildStringSpecification(criteria.getGender(), Student_.gender),
                buildRangeSpecification(criteria.getBirthday(), Student_.birthday),
                buildRangeSpecification(criteria.getRegisterDate(), Student_.registerDate),
                buildRangeSpecification(criteria.getUpdateDate(), Student_.updateDate),
                buildRangeSpecification(criteria.getLatestContractEndDate(), Student_.latestContractEndDate),
                buildStringSpecification(criteria.getContactNumber(), Student_.contactNumber),
                buildStringSpecification(criteria.getParentsName(), Student_.parentsName),
                buildSpecification(criteria.getSchoolId(), root -> root.join(Student_.school, JoinType.LEFT).get(School_.id)),
                buildSpecification(criteria.getCommunityId(), root -> root.join(Student_.community, JoinType.LEFT).get(Community_.id))
            );
        }
        return specification;
    }
}
