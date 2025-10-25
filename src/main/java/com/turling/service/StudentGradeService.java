package com.turling.service;

import com.turling.service.dto.StudentGradeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.turling.domain.StudentGrade}.
 */
public interface StudentGradeService {
    /**
     * Save a studentGrade.
     *
     * @param studentGradeDTO the entity to save.
     * @return the persisted entity.
     */
    StudentGradeDTO save(StudentGradeDTO studentGradeDTO);

    /**
     * Updates a studentGrade.
     *
     * @param studentGradeDTO the entity to update.
     * @return the persisted entity.
     */
    StudentGradeDTO update(StudentGradeDTO studentGradeDTO);

    /**
     * Partially updates a studentGrade.
     *
     * @param studentGradeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentGradeDTO> partialUpdate(StudentGradeDTO studentGradeDTO);

    /**
     * Get all the studentGrades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentGradeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" studentGrade.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentGradeDTO> findOne(Long id);

    /**
     * Delete the "id" studentGrade.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
