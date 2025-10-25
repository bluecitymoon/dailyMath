package com.turling.web.rest;

import static com.turling.domain.QuestionOptionAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.QuestionOption;
import com.turling.repository.QuestionOptionRepository;
import com.turling.service.dto.QuestionOptionDTO;
import com.turling.service.mapper.QuestionOptionMapper;
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
 * Integration tests for the {@link QuestionOptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionOptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private QuestionOptionMapper questionOptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionOptionMockMvc;

    private QuestionOption questionOption;

    private QuestionOption insertedQuestionOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionOption createEntity() {
        return new QuestionOption().name(DEFAULT_NAME).type(DEFAULT_TYPE).imageUrl(DEFAULT_IMAGE_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionOption createUpdatedEntity() {
        return new QuestionOption().name(UPDATED_NAME).type(UPDATED_TYPE).imageUrl(UPDATED_IMAGE_URL);
    }

    @BeforeEach
    void initTest() {
        questionOption = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestionOption != null) {
            questionOptionRepository.delete(insertedQuestionOption);
            insertedQuestionOption = null;
        }
    }

    @Test
    @Transactional
    void createQuestionOption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);
        var returnedQuestionOptionDTO = om.readValue(
            restQuestionOptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionOptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionOptionDTO.class
        );

        // Validate the QuestionOption in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestionOption = questionOptionMapper.toEntity(returnedQuestionOptionDTO);
        assertQuestionOptionUpdatableFieldsEquals(returnedQuestionOption, getPersistedQuestionOption(returnedQuestionOption));

        insertedQuestionOption = returnedQuestionOption;
    }

    @Test
    @Transactional
    void createQuestionOptionWithExistingId() throws Exception {
        // Create the QuestionOption with an existing ID
        questionOption.setId(1L);
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionOptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionOptions() throws Exception {
        // Initialize the database
        insertedQuestionOption = questionOptionRepository.saveAndFlush(questionOption);

        // Get all the questionOptionList
        restQuestionOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getQuestionOption() throws Exception {
        // Initialize the database
        insertedQuestionOption = questionOptionRepository.saveAndFlush(questionOption);

        // Get the questionOption
        restQuestionOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, questionOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionOption.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getNonExistingQuestionOption() throws Exception {
        // Get the questionOption
        restQuestionOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionOption() throws Exception {
        // Initialize the database
        insertedQuestionOption = questionOptionRepository.saveAndFlush(questionOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionOption
        QuestionOption updatedQuestionOption = questionOptionRepository.findById(questionOption.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestionOption are not directly saved in db
        em.detach(updatedQuestionOption);
        updatedQuestionOption.name(UPDATED_NAME).type(UPDATED_TYPE).imageUrl(UPDATED_IMAGE_URL);
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(updatedQuestionOption);

        restQuestionOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionOptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionOptionToMatchAllProperties(updatedQuestionOption);
    }

    @Test
    @Transactional
    void putNonExistingQuestionOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionOption.setId(longCount.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionOption.setId(longCount.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionOption.setId(longCount.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionOptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionOptionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionOption = questionOptionRepository.saveAndFlush(questionOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionOption using partial update
        QuestionOption partialUpdatedQuestionOption = new QuestionOption();
        partialUpdatedQuestionOption.setId(questionOption.getId());

        partialUpdatedQuestionOption.imageUrl(UPDATED_IMAGE_URL);

        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionOption))
            )
            .andExpect(status().isOk());

        // Validate the QuestionOption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionOptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuestionOption, questionOption),
            getPersistedQuestionOption(questionOption)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuestionOptionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionOption = questionOptionRepository.saveAndFlush(questionOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionOption using partial update
        QuestionOption partialUpdatedQuestionOption = new QuestionOption();
        partialUpdatedQuestionOption.setId(questionOption.getId());

        partialUpdatedQuestionOption.name(UPDATED_NAME).type(UPDATED_TYPE).imageUrl(UPDATED_IMAGE_URL);

        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionOption))
            )
            .andExpect(status().isOk());

        // Validate the QuestionOption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionOptionUpdatableFieldsEquals(partialUpdatedQuestionOption, getPersistedQuestionOption(partialUpdatedQuestionOption));
    }

    @Test
    @Transactional
    void patchNonExistingQuestionOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionOption.setId(longCount.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionOptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionOption.setId(longCount.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionOption.setId(longCount.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionOptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionOption() throws Exception {
        // Initialize the database
        insertedQuestionOption = questionOptionRepository.saveAndFlush(questionOption);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the questionOption
        restQuestionOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionOptionRepository.count();
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

    protected QuestionOption getPersistedQuestionOption(QuestionOption questionOption) {
        return questionOptionRepository.findById(questionOption.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionOptionToMatchAllProperties(QuestionOption expectedQuestionOption) {
        assertQuestionOptionAllPropertiesEquals(expectedQuestionOption, getPersistedQuestionOption(expectedQuestionOption));
    }

    protected void assertPersistedQuestionOptionToMatchUpdatableProperties(QuestionOption expectedQuestionOption) {
        assertQuestionOptionAllUpdatablePropertiesEquals(expectedQuestionOption, getPersistedQuestionOption(expectedQuestionOption));
    }
}
