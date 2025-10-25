package com.turling.service;

import com.turling.service.dto.QuestionBaseGroupDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.turling.domain.QuestionBaseGroup}.
 */
public interface QuestionBaseGroupService {
    /**
     * Save a questionBaseGroup.
     *
     * @param questionBaseGroupDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionBaseGroupDTO save(QuestionBaseGroupDTO questionBaseGroupDTO);

    /**
     * Updates a questionBaseGroup.
     *
     * @param questionBaseGroupDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionBaseGroupDTO update(QuestionBaseGroupDTO questionBaseGroupDTO);

    /**
     * Partially updates a questionBaseGroup.
     *
     * @param questionBaseGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionBaseGroupDTO> partialUpdate(QuestionBaseGroupDTO questionBaseGroupDTO);

    /**
     * Get the "id" questionBaseGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionBaseGroupDTO> findOne(Long id);

    /**
     * Delete the "id" questionBaseGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
