package com.turling.service;

import com.turling.service.dto.QuestionCategoryDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.turling.domain.QuestionCategory}.
 */
public interface QuestionCategoryService {
    /**
     * Save a questionCategory.
     *
     * @param questionCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionCategoryDTO save(QuestionCategoryDTO questionCategoryDTO);

    /**
     * Updates a questionCategory.
     *
     * @param questionCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionCategoryDTO update(QuestionCategoryDTO questionCategoryDTO);

    /**
     * Partially updates a questionCategory.
     *
     * @param questionCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionCategoryDTO> partialUpdate(QuestionCategoryDTO questionCategoryDTO);

    /**
     * Get the "id" questionCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" questionCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
