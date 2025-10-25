package com.turling.service;

import com.turling.service.dto.QuestionSectionGroupDTO;
import com.turling.service.dto.SectionGroupQuestionsResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.turling.domain.QuestionSectionGroup}.
 */
public interface QuestionSectionGroupService {
    /**
     * Save a questionSectionGroup.
     *
     * @param questionSectionGroupDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionSectionGroupDTO save(QuestionSectionGroupDTO questionSectionGroupDTO);

    /**
     * Updates a questionSectionGroup.
     *
     * @param questionSectionGroupDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionSectionGroupDTO update(QuestionSectionGroupDTO questionSectionGroupDTO);

    /**
     * Partially updates a questionSectionGroup.
     *
     * @param questionSectionGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionSectionGroupDTO> partialUpdate(QuestionSectionGroupDTO questionSectionGroupDTO);

    /**
     * Get all the questionSectionGroups with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionSectionGroupDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" questionSectionGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionSectionGroupDTO> findOne(Long id);

    /**
     * Delete the "id" questionSectionGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Update the display order of question section groups for a specific page.
     *
     * @param orderedIds the list of IDs in the new order for the current page.
     * @param page the current page number (0-based).
     * @param size the page size.
     */
    void updateDisplayOrderForPage(List<Long> orderedIds, int page, int size);

    /**
     * Get all question section groups by grade ID.
     *
     * @param gradeId the grade ID to filter by.
     * @return the list of section groups for the specified grade.
     */
    List<QuestionSectionGroupDTO> findByGradeId(Long gradeId);

    /**
     * Get all question section groups by grade ID with pagination.
     *
     * @param gradeId the grade ID to filter by.
     * @param pageable the pagination information.
     * @return the page of section groups for the specified grade.
     */
    Page<QuestionSectionGroupDTO> findByGradeId(Long gradeId, Pageable pageable);

    /**
     * Get all questions with base group information for a specific section group.
     *
     * @param sectionGroupId the section group ID to get questions for.
     * @return the response containing section group info and associated questions with base group details.
     */
    SectionGroupQuestionsResponse getQuestionsBySectionGroupId(Long sectionGroupId);

    /**
     * Get all questions with base group information for a specific section group with pagination.
     *
     * @param sectionGroupId the section group ID to get questions for.
     * @param pageable the pagination information.
     * @return the response containing section group info and associated questions with base group details.
     */
    SectionGroupQuestionsResponse getQuestionsBySectionGroupId(Long sectionGroupId, Pageable pageable);
}
