package com.turling.service;

import com.turling.service.dto.QuestionOptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.turling.domain.QuestionOption}.
 */
public interface QuestionOptionService {
    /**
     * Save a questionOption.
     *
     * @param questionOptionDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionOptionDTO save(QuestionOptionDTO questionOptionDTO);

    /**
     * Updates a questionOption.
     *
     * @param questionOptionDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionOptionDTO update(QuestionOptionDTO questionOptionDTO);

    /**
     * Partially updates a questionOption.
     *
     * @param questionOptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionOptionDTO> partialUpdate(QuestionOptionDTO questionOptionDTO);

    /**
     * Get all the questionOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionOptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" questionOption.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionOptionDTO> findOne(Long id);

    /**
     * Delete the "id" questionOption.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
