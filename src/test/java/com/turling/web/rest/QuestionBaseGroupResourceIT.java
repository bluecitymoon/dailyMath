package com.turling.web.rest;

import static com.turling.domain.QuestionBaseGroupAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.QuestionBaseGroup;
import com.turling.repository.QuestionBaseGroupRepository;
import com.turling.service.dto.QuestionBaseGroupDTO;
import com.turling.service.mapper.QuestionBaseGroupMapper;
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
 * Integration tests for the {@link QuestionBaseGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionBaseGroupResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_IDS = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_IDS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-base-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionBaseGroupRepository questionBaseGroupRepository;

    @Autowired
    private QuestionBaseGroupMapper questionBaseGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionBaseGroupMockMvc;

    private QuestionBaseGroup questionBaseGroup;

    private QuestionBaseGroup insertedQuestionBaseGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionBaseGroup createEntity() {
        return new QuestionBaseGroup().title(DEFAULT_TITLE).questionIds(DEFAULT_QUESTION_IDS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionBaseGroup createUpdatedEntity() {
        return new QuestionBaseGroup().title(UPDATED_TITLE).questionIds(UPDATED_QUESTION_IDS);
    }

    @BeforeEach
    void initTest() {
        questionBaseGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestionBaseGroup != null) {
            questionBaseGroupRepository.delete(insertedQuestionBaseGroup);
            insertedQuestionBaseGroup = null;
        }
    }

    @Test
    @Transactional
    void createQuestionBaseGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuestionBaseGroup
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);
        var returnedQuestionBaseGroupDTO = om.readValue(
            restQuestionBaseGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionBaseGroupDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionBaseGroupDTO.class
        );

        // Validate the QuestionBaseGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestionBaseGroup = questionBaseGroupMapper.toEntity(returnedQuestionBaseGroupDTO);
        assertQuestionBaseGroupUpdatableFieldsEquals(returnedQuestionBaseGroup, getPersistedQuestionBaseGroup(returnedQuestionBaseGroup));

        insertedQuestionBaseGroup = returnedQuestionBaseGroup;
    }

    @Test
    @Transactional
    void createQuestionBaseGroupWithExistingId() throws Exception {
        // Create the QuestionBaseGroup with an existing ID
        questionBaseGroup.setId(1L);
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionBaseGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionBaseGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroups() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList
        restQuestionBaseGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionBaseGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].questionIds").value(hasItem(DEFAULT_QUESTION_IDS)));
    }

    @Test
    @Transactional
    void getQuestionBaseGroup() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get the questionBaseGroup
        restQuestionBaseGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, questionBaseGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionBaseGroup.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.questionIds").value(DEFAULT_QUESTION_IDS));
    }

    @Test
    @Transactional
    void getQuestionBaseGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        Long id = questionBaseGroup.getId();

        defaultQuestionBaseGroupFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuestionBaseGroupFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuestionBaseGroupFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where title equals to
        defaultQuestionBaseGroupFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where title in
        defaultQuestionBaseGroupFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where title is not null
        defaultQuestionBaseGroupFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where title contains
        defaultQuestionBaseGroupFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where title does not contain
        defaultQuestionBaseGroupFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByQuestionIdsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where questionIds equals to
        defaultQuestionBaseGroupFiltering("questionIds.equals=" + DEFAULT_QUESTION_IDS, "questionIds.equals=" + UPDATED_QUESTION_IDS);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByQuestionIdsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where questionIds in
        defaultQuestionBaseGroupFiltering(
            "questionIds.in=" + DEFAULT_QUESTION_IDS + "," + UPDATED_QUESTION_IDS,
            "questionIds.in=" + UPDATED_QUESTION_IDS
        );
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByQuestionIdsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where questionIds is not null
        defaultQuestionBaseGroupFiltering("questionIds.specified=true", "questionIds.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByQuestionIdsContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where questionIds contains
        defaultQuestionBaseGroupFiltering("questionIds.contains=" + DEFAULT_QUESTION_IDS, "questionIds.contains=" + UPDATED_QUESTION_IDS);
    }

    @Test
    @Transactional
    void getAllQuestionBaseGroupsByQuestionIdsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        // Get all the questionBaseGroupList where questionIds does not contain
        defaultQuestionBaseGroupFiltering(
            "questionIds.doesNotContain=" + UPDATED_QUESTION_IDS,
            "questionIds.doesNotContain=" + DEFAULT_QUESTION_IDS
        );
    }

    private void defaultQuestionBaseGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuestionBaseGroupShouldBeFound(shouldBeFound);
        defaultQuestionBaseGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionBaseGroupShouldBeFound(String filter) throws Exception {
        restQuestionBaseGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionBaseGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].questionIds").value(hasItem(DEFAULT_QUESTION_IDS)));

        // Check, that the count call also returns 1
        restQuestionBaseGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionBaseGroupShouldNotBeFound(String filter) throws Exception {
        restQuestionBaseGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionBaseGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionBaseGroup() throws Exception {
        // Get the questionBaseGroup
        restQuestionBaseGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionBaseGroup() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionBaseGroup
        QuestionBaseGroup updatedQuestionBaseGroup = questionBaseGroupRepository.findById(questionBaseGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestionBaseGroup are not directly saved in db
        em.detach(updatedQuestionBaseGroup);
        updatedQuestionBaseGroup.title(UPDATED_TITLE).questionIds(UPDATED_QUESTION_IDS);
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(updatedQuestionBaseGroup);

        restQuestionBaseGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionBaseGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionBaseGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionBaseGroupToMatchAllProperties(updatedQuestionBaseGroup);
    }

    @Test
    @Transactional
    void putNonExistingQuestionBaseGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionBaseGroup.setId(longCount.incrementAndGet());

        // Create the QuestionBaseGroup
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionBaseGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionBaseGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionBaseGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionBaseGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionBaseGroup.setId(longCount.incrementAndGet());

        // Create the QuestionBaseGroup
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionBaseGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionBaseGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionBaseGroup.setId(longCount.incrementAndGet());

        // Create the QuestionBaseGroup
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionBaseGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionBaseGroupWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionBaseGroup using partial update
        QuestionBaseGroup partialUpdatedQuestionBaseGroup = new QuestionBaseGroup();
        partialUpdatedQuestionBaseGroup.setId(questionBaseGroup.getId());

        partialUpdatedQuestionBaseGroup.title(UPDATED_TITLE).questionIds(UPDATED_QUESTION_IDS);

        restQuestionBaseGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionBaseGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionBaseGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionBaseGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionBaseGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuestionBaseGroup, questionBaseGroup),
            getPersistedQuestionBaseGroup(questionBaseGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuestionBaseGroupWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionBaseGroup using partial update
        QuestionBaseGroup partialUpdatedQuestionBaseGroup = new QuestionBaseGroup();
        partialUpdatedQuestionBaseGroup.setId(questionBaseGroup.getId());

        partialUpdatedQuestionBaseGroup.title(UPDATED_TITLE).questionIds(UPDATED_QUESTION_IDS);

        restQuestionBaseGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionBaseGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionBaseGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionBaseGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionBaseGroupUpdatableFieldsEquals(
            partialUpdatedQuestionBaseGroup,
            getPersistedQuestionBaseGroup(partialUpdatedQuestionBaseGroup)
        );
    }

    @Test
    @Transactional
    void patchNonExistingQuestionBaseGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionBaseGroup.setId(longCount.incrementAndGet());

        // Create the QuestionBaseGroup
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionBaseGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionBaseGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionBaseGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionBaseGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionBaseGroup.setId(longCount.incrementAndGet());

        // Create the QuestionBaseGroup
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionBaseGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionBaseGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionBaseGroup.setId(longCount.incrementAndGet());

        // Create the QuestionBaseGroup
        QuestionBaseGroupDTO questionBaseGroupDTO = questionBaseGroupMapper.toDto(questionBaseGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionBaseGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionBaseGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionBaseGroup() throws Exception {
        // Initialize the database
        insertedQuestionBaseGroup = questionBaseGroupRepository.saveAndFlush(questionBaseGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the questionBaseGroup
        restQuestionBaseGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionBaseGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionBaseGroupRepository.count();
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

    protected QuestionBaseGroup getPersistedQuestionBaseGroup(QuestionBaseGroup questionBaseGroup) {
        return questionBaseGroupRepository.findById(questionBaseGroup.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionBaseGroupToMatchAllProperties(QuestionBaseGroup expectedQuestionBaseGroup) {
        assertQuestionBaseGroupAllPropertiesEquals(expectedQuestionBaseGroup, getPersistedQuestionBaseGroup(expectedQuestionBaseGroup));
    }

    protected void assertPersistedQuestionBaseGroupToMatchUpdatableProperties(QuestionBaseGroup expectedQuestionBaseGroup) {
        assertQuestionBaseGroupAllUpdatablePropertiesEquals(
            expectedQuestionBaseGroup,
            getPersistedQuestionBaseGroup(expectedQuestionBaseGroup)
        );
    }
}
