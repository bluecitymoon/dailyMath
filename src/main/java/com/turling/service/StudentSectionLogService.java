package com.turling.service;

import com.turling.service.dto.StudentSectionLogDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.turling.domain.StudentSectionLog}.
 */
public interface StudentSectionLogService {
    /**
     * Save a studentSectionLog.
     *
     * @param studentSectionLogDTO the entity to save.
     * @return the persisted entity.
     */
    StudentSectionLogDTO save(StudentSectionLogDTO studentSectionLogDTO);

    /**
     * Updates a studentSectionLog.
     *
     * @param studentSectionLogDTO the entity to update.
     * @return the persisted entity.
     */
    StudentSectionLogDTO update(StudentSectionLogDTO studentSectionLogDTO);

    /**
     * Partially updates a studentSectionLog.
     *
     * @param studentSectionLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentSectionLogDTO> partialUpdate(StudentSectionLogDTO studentSectionLogDTO);

    /**
     * Get the "id" studentSectionLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentSectionLogDTO> findOne(Long id);

    /**
     * Delete the "id" studentSectionLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
