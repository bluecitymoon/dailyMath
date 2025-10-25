package com.turling.service;

import com.turling.service.dto.QuestionTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.turling.domain.QuestionType}.
 */
public interface QuestionTypeService {
    /**
     * Save a questionType.
     *
     * @param questionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionTypeDTO save(QuestionTypeDTO questionTypeDTO);

    /**
     * Updates a questionType.
     *
     * @param questionTypeDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionTypeDTO update(QuestionTypeDTO questionTypeDTO);

    /**
     * Partially updates a questionType.
     *
     * @param questionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionTypeDTO> partialUpdate(QuestionTypeDTO questionTypeDTO);

    /**
     * Get all the questionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" questionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionTypeDTO> findOne(Long id);

    /**
     * Delete the "id" questionType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
