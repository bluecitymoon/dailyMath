package com.turling.web.rest;

import static com.turling.domain.QuestionCategoryAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.QuestionCategory;
import com.turling.repository.QuestionCategoryRepository;
import com.turling.service.dto.QuestionCategoryDTO;
import com.turling.service.mapper.QuestionCategoryMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

    @Autowired
    private QuestionCategoryMapper questionCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionCategoryMockMvc;

    private QuestionCategory questionCategory;

    private QuestionCategory insertedQuestionCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionCategory createEntity() {
        return new QuestionCategory().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionCategory createUpdatedEntity() {
        return new QuestionCategory().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        questionCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestionCategory != null) {
            questionCategoryRepository.delete(insertedQuestionCategory);
            insertedQuestionCategory = null;
        }
    }

    @Test
    @Transactional
    void createQuestionCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);
        var returnedQuestionCategoryDTO = om.readValue(
            restQuestionCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionCategoryDTO.class
        );

        // Validate the QuestionCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestionCategory = questionCategoryMapper.toEntity(returnedQuestionCategoryDTO);
        assertQuestionCategoryUpdatableFieldsEquals(returnedQuestionCategory, getPersistedQuestionCategory(returnedQuestionCategory));

        insertedQuestionCategory = returnedQuestionCategory;
    }

    @Test
    @Transactional
    void createQuestionCategoryWithExistingId() throws Exception {
        // Create the QuestionCategory with an existing ID
        questionCategory.setId(1L);
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionCategories() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        // Get all the questionCategoryList
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getQuestionCategory() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        // Get the questionCategory
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, questionCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getQuestionCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        Long id = questionCategory.getId();

        defaultQuestionCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuestionCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuestionCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        // Get all the questionCategoryList where name equals to
        defaultQuestionCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        // Get all the questionCategoryList where name in
        defaultQuestionCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        // Get all the questionCategoryList where name is not null
        defaultQuestionCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        // Get all the questionCategoryList where name contains
        defaultQuestionCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        // Get all the questionCategoryList where name does not contain
        defaultQuestionCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultQuestionCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuestionCategoryShouldBeFound(shouldBeFound);
        defaultQuestionCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionCategoryShouldBeFound(String filter) throws Exception {
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionCategoryShouldNotBeFound(String filter) throws Exception {
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionCategory() throws Exception {
        // Get the questionCategory
        restQuestionCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionCategory() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionCategory
        QuestionCategory updatedQuestionCategory = questionCategoryRepository.findById(questionCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestionCategory are not directly saved in db
        em.detach(updatedQuestionCategory);
        updatedQuestionCategory.name(UPDATED_NAME);
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(updatedQuestionCategory);

        restQuestionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionCategoryToMatchAllProperties(updatedQuestionCategory);
    }

    @Test
    @Transactional
    void putNonExistingQuestionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionCategory.setId(longCount.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionCategory.setId(longCount.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionCategory.setId(longCount.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionCategory using partial update
        QuestionCategory partialUpdatedQuestionCategory = new QuestionCategory();
        partialUpdatedQuestionCategory.setId(questionCategory.getId());

        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionCategory))
            )
            .andExpect(status().isOk());

        // Validate the QuestionCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuestionCategory, questionCategory),
            getPersistedQuestionCategory(questionCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuestionCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionCategory using partial update
        QuestionCategory partialUpdatedQuestionCategory = new QuestionCategory();
        partialUpdatedQuestionCategory.setId(questionCategory.getId());

        partialUpdatedQuestionCategory.name(UPDATED_NAME);

        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionCategory))
            )
            .andExpect(status().isOk());

        // Validate the QuestionCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionCategoryUpdatableFieldsEquals(
            partialUpdatedQuestionCategory,
            getPersistedQuestionCategory(partialUpdatedQuestionCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingQuestionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionCategory.setId(longCount.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionCategory.setId(longCount.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionCategory.setId(longCount.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionCategory() throws Exception {
        // Initialize the database
        insertedQuestionCategory = questionCategoryRepository.saveAndFlush(questionCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the questionCategory
        restQuestionCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionCategoryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected QuestionCategory getPersistedQuestionCategory(QuestionCategory questionCategory) {
        return questionCategoryRepository.findById(questionCategory.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionCategoryToMatchAllProperties(QuestionCategory expectedQuestionCategory) {
        assertQuestionCategoryAllPropertiesEquals(expectedQuestionCategory, getPersistedQuestionCategory(expectedQuestionCategory));
    }

    protected void assertPersistedQuestionCategoryToMatchUpdatableProperties(QuestionCategory expectedQuestionCategory) {
        assertQuestionCategoryAllUpdatablePropertiesEquals(
            expectedQuestionCategory,
            getPersistedQuestionCategory(expectedQuestionCategory)
        );
    }
}
