package com.turling.service;

import com.turling.service.dto.StudentAnswerLogDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.turling.domain.StudentAnswerLog}.
 */
public interface StudentAnswerLogService {
    /**
     * Save a studentAnswerLog.
     *
     * @param studentAnswerLogDTO the entity to save.
     * @return the persisted entity.
     */
    StudentAnswerLogDTO save(StudentAnswerLogDTO studentAnswerLogDTO);

    /**
     * Updates a studentAnswerLog.
     *
     * @param studentAnswerLogDTO the entity to update.
     * @return the persisted entity.
     */
    StudentAnswerLogDTO update(StudentAnswerLogDTO studentAnswerLogDTO);

    /**
     * Partially updates a studentAnswerLog.
     *
     * @param studentAnswerLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentAnswerLogDTO> partialUpdate(StudentAnswerLogDTO studentAnswerLogDTO);

    /**
     * Get the "id" studentAnswerLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentAnswerLogDTO> findOne(Long id);

    /**
     * Delete the "id" studentAnswerLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
