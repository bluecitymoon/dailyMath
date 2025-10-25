package com.turling.web.rest;

import static com.turling.domain.QuestionTypeAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.QuestionType;
import com.turling.repository.QuestionTypeRepository;
import com.turling.service.dto.QuestionTypeDTO;
import com.turling.service.mapper.QuestionTypeMapper;
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
 * Integration tests for the {@link QuestionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionTypeRepository questionTypeRepository;

    @Autowired
    private QuestionTypeMapper questionTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionTypeMockMvc;

    private QuestionType questionType;

    private QuestionType insertedQuestionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionType createEntity() {
        return new QuestionType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionType createUpdatedEntity() {
        return new QuestionType().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        questionType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestionType != null) {
            questionTypeRepository.delete(insertedQuestionType);
            insertedQuestionType = null;
        }
    }

    @Test
    @Transactional
    void createQuestionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);
        var returnedQuestionTypeDTO = om.readValue(
            restQuestionTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionTypeDTO.class
        );

        // Validate the QuestionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestionType = questionTypeMapper.toEntity(returnedQuestionTypeDTO);
        assertQuestionTypeUpdatableFieldsEquals(returnedQuestionType, getPersistedQuestionType(returnedQuestionType));

        insertedQuestionType = returnedQuestionType;
    }

    @Test
    @Transactional
    void createQuestionTypeWithExistingId() throws Exception {
        // Create the QuestionType with an existing ID
        questionType.setId(1L);
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionTypes() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getQuestionType() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.saveAndFlush(questionType);

        // Get the questionType
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, questionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingQuestionType() throws Exception {
        // Get the questionType
        restQuestionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionType() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.saveAndFlush(questionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionType
        QuestionType updatedQuestionType = questionTypeRepository.findById(questionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestionType are not directly saved in db
        em.detach(updatedQuestionType);
        updatedQuestionType.name(UPDATED_NAME);
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(updatedQuestionType);

        restQuestionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionTypeToMatchAllProperties(updatedQuestionType);
    }

    @Test
    @Transactional
    void putNonExistingQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.saveAndFlush(questionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionType using partial update
        QuestionType partialUpdatedQuestionType = new QuestionType();
        partialUpdatedQuestionType.setId(questionType.getId());

        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionType))
            )
            .andExpect(status().isOk());

        // Validate the QuestionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuestionType, questionType),
            getPersistedQuestionType(questionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuestionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.saveAndFlush(questionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionType using partial update
        QuestionType partialUpdatedQuestionType = new QuestionType();
        partialUpdatedQuestionType.setId(questionType.getId());

        partialUpdatedQuestionType.name(UPDATED_NAME);

        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionType))
            )
            .andExpect(status().isOk());

        // Validate the QuestionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionTypeUpdatableFieldsEquals(partialUpdatedQuestionType, getPersistedQuestionType(partialUpdatedQuestionType));
    }

    @Test
    @Transactional
    void patchNonExistingQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionType() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.saveAndFlush(questionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the questionType
        restQuestionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionTypeRepository.count();
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

    protected QuestionType getPersistedQuestionType(QuestionType questionType) {
        return questionTypeRepository.findById(questionType.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionTypeToMatchAllProperties(QuestionType expectedQuestionType) {
        assertQuestionTypeAllPropertiesEquals(expectedQuestionType, getPersistedQuestionType(expectedQuestionType));
    }

    protected void assertPersistedQuestionTypeToMatchUpdatableProperties(QuestionType expectedQuestionType) {
        assertQuestionTypeAllUpdatablePropertiesEquals(expectedQuestionType, getPersistedQuestionType(expectedQuestionType));
    }
}
