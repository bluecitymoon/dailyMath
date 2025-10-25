package com.turling.service;

import com.turling.service.dto.DistinctDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.turling.domain.Distinct}.
 */
public interface DistinctService {
    /**
     * Save a distinct.
     *
     * @param distinctDTO the entity to save.
     * @return the persisted entity.
     */
    DistinctDTO save(DistinctDTO distinctDTO);

    /**
     * Updates a distinct.
     *
     * @param distinctDTO the entity to update.
     * @return the persisted entity.
     */
    DistinctDTO update(DistinctDTO distinctDTO);

    /**
     * Partially updates a distinct.
     *
     * @param distinctDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DistinctDTO> partialUpdate(DistinctDTO distinctDTO);

    /**
     * Get the "id" distinct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DistinctDTO> findOne(Long id);

    /**
     * Delete the "id" distinct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
