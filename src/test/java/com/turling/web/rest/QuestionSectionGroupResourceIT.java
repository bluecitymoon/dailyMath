package com.turling.web.rest;

import static com.turling.domain.QuestionSectionGroupAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.QuestionSectionGroup;
import com.turling.domain.StudentGrade;
import com.turling.repository.QuestionSectionGroupRepository;
import com.turling.service.QuestionSectionGroupService;
import com.turling.service.dto.QuestionSectionGroupDTO;
import com.turling.service.mapper.QuestionSectionGroupMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionSectionGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionSectionGroupResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BASE_GROUP_IDS = "AAAAAAAAAA";
    private static final String UPDATED_BASE_GROUP_IDS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-section-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionSectionGroupRepository questionSectionGroupRepository;

    @Mock
    private QuestionSectionGroupRepository questionSectionGroupRepositoryMock;

    @Autowired
    private QuestionSectionGroupMapper questionSectionGroupMapper;

    @Mock
    private QuestionSectionGroupService questionSectionGroupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionSectionGroupMockMvc;

    private QuestionSectionGroup questionSectionGroup;

    private QuestionSectionGroup insertedQuestionSectionGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionSectionGroup createEntity() {
        return new QuestionSectionGroup().title(DEFAULT_TITLE).baseGroupIds(DEFAULT_BASE_GROUP_IDS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionSectionGroup createUpdatedEntity() {
        return new QuestionSectionGroup().title(UPDATED_TITLE).baseGroupIds(UPDATED_BASE_GROUP_IDS);
    }

    @BeforeEach
    void initTest() {
        questionSectionGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestionSectionGroup != null) {
            questionSectionGroupRepository.delete(insertedQuestionSectionGroup);
            insertedQuestionSectionGroup = null;
        }
    }

    @Test
    @Transactional
    void createQuestionSectionGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuestionSectionGroup
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);
        var returnedQuestionSectionGroupDTO = om.readValue(
            restQuestionSectionGroupMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionSectionGroupDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionSectionGroupDTO.class
        );

        // Validate the QuestionSectionGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestionSectionGroup = questionSectionGroupMapper.toEntity(returnedQuestionSectionGroupDTO);
        assertQuestionSectionGroupUpdatableFieldsEquals(
            returnedQuestionSectionGroup,
            getPersistedQuestionSectionGroup(returnedQuestionSectionGroup)
        );

        insertedQuestionSectionGroup = returnedQuestionSectionGroup;
    }

    @Test
    @Transactional
    void createQuestionSectionGroupWithExistingId() throws Exception {
        // Create the QuestionSectionGroup with an existing ID
        questionSectionGroup.setId(1L);
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionSectionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionSectionGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroups() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList
        restQuestionSectionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionSectionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].baseGroupIds").value(hasItem(DEFAULT_BASE_GROUP_IDS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionSectionGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionSectionGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionSectionGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionSectionGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionSectionGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionSectionGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionSectionGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(questionSectionGroupRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuestionSectionGroup() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get the questionSectionGroup
        restQuestionSectionGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, questionSectionGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionSectionGroup.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.baseGroupIds").value(DEFAULT_BASE_GROUP_IDS));
    }

    @Test
    @Transactional
    void getQuestionSectionGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        Long id = questionSectionGroup.getId();

        defaultQuestionSectionGroupFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuestionSectionGroupFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuestionSectionGroupFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where title equals to
        defaultQuestionSectionGroupFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where title in
        defaultQuestionSectionGroupFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where title is not null
        defaultQuestionSectionGroupFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where title contains
        defaultQuestionSectionGroupFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where title does not contain
        defaultQuestionSectionGroupFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByBaseGroupIdsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where baseGroupIds equals to
        defaultQuestionSectionGroupFiltering(
            "baseGroupIds.equals=" + DEFAULT_BASE_GROUP_IDS,
            "baseGroupIds.equals=" + UPDATED_BASE_GROUP_IDS
        );
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByBaseGroupIdsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where baseGroupIds in
        defaultQuestionSectionGroupFiltering(
            "baseGroupIds.in=" + DEFAULT_BASE_GROUP_IDS + "," + UPDATED_BASE_GROUP_IDS,
            "baseGroupIds.in=" + UPDATED_BASE_GROUP_IDS
        );
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByBaseGroupIdsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where baseGroupIds is not null
        defaultQuestionSectionGroupFiltering("baseGroupIds.specified=true", "baseGroupIds.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByBaseGroupIdsContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where baseGroupIds contains
        defaultQuestionSectionGroupFiltering(
            "baseGroupIds.contains=" + DEFAULT_BASE_GROUP_IDS,
            "baseGroupIds.contains=" + UPDATED_BASE_GROUP_IDS
        );
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByBaseGroupIdsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        // Get all the questionSectionGroupList where baseGroupIds does not contain
        defaultQuestionSectionGroupFiltering(
            "baseGroupIds.doesNotContain=" + UPDATED_BASE_GROUP_IDS,
            "baseGroupIds.doesNotContain=" + DEFAULT_BASE_GROUP_IDS
        );
    }

    @Test
    @Transactional
    void getAllQuestionSectionGroupsByGradeIsEqualToSomething() throws Exception {
        StudentGrade grade;
        if (TestUtil.findAll(em, StudentGrade.class).isEmpty()) {
            questionSectionGroupRepository.saveAndFlush(questionSectionGroup);
            grade = StudentGradeResourceIT.createEntity();
        } else {
            grade = TestUtil.findAll(em, StudentGrade.class).get(0);
        }
        em.persist(grade);
        em.flush();
        questionSectionGroup.setGrade(grade);
        questionSectionGroupRepository.saveAndFlush(questionSectionGroup);
        Long gradeId = grade.getId();
        // Get all the questionSectionGroupList where grade equals to gradeId
        defaultQuestionSectionGroupShouldBeFound("gradeId.equals=" + gradeId);

        // Get all the questionSectionGroupList where grade equals to (gradeId + 1)
        defaultQuestionSectionGroupShouldNotBeFound("gradeId.equals=" + (gradeId + 1));
    }

    private void defaultQuestionSectionGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuestionSectionGroupShouldBeFound(shouldBeFound);
        defaultQuestionSectionGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionSectionGroupShouldBeFound(String filter) throws Exception {
        restQuestionSectionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionSectionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].baseGroupIds").value(hasItem(DEFAULT_BASE_GROUP_IDS)));

        // Check, that the count call also returns 1
        restQuestionSectionGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionSectionGroupShouldNotBeFound(String filter) throws Exception {
        restQuestionSectionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionSectionGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionSectionGroup() throws Exception {
        // Get the questionSectionGroup
        restQuestionSectionGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionSectionGroup() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionSectionGroup
        QuestionSectionGroup updatedQuestionSectionGroup = questionSectionGroupRepository
            .findById(questionSectionGroup.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedQuestionSectionGroup are not directly saved in db
        em.detach(updatedQuestionSectionGroup);
        updatedQuestionSectionGroup.title(UPDATED_TITLE).baseGroupIds(UPDATED_BASE_GROUP_IDS);
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(updatedQuestionSectionGroup);

        restQuestionSectionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionSectionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionSectionGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionSectionGroupToMatchAllProperties(updatedQuestionSectionGroup);
    }

    @Test
    @Transactional
    void putNonExistingQuestionSectionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionSectionGroup.setId(longCount.incrementAndGet());

        // Create the QuestionSectionGroup
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionSectionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionSectionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionSectionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionSectionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionSectionGroup.setId(longCount.incrementAndGet());

        // Create the QuestionSectionGroup
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionSectionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionSectionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionSectionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionSectionGroup.setId(longCount.incrementAndGet());

        // Create the QuestionSectionGroup
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionSectionGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionSectionGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionSectionGroupWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionSectionGroup using partial update
        QuestionSectionGroup partialUpdatedQuestionSectionGroup = new QuestionSectionGroup();
        partialUpdatedQuestionSectionGroup.setId(questionSectionGroup.getId());

        partialUpdatedQuestionSectionGroup.title(UPDATED_TITLE);

        restQuestionSectionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionSectionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionSectionGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionSectionGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionSectionGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuestionSectionGroup, questionSectionGroup),
            getPersistedQuestionSectionGroup(questionSectionGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuestionSectionGroupWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionSectionGroup using partial update
        QuestionSectionGroup partialUpdatedQuestionSectionGroup = new QuestionSectionGroup();
        partialUpdatedQuestionSectionGroup.setId(questionSectionGroup.getId());

        partialUpdatedQuestionSectionGroup.title(UPDATED_TITLE).baseGroupIds(UPDATED_BASE_GROUP_IDS);

        restQuestionSectionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionSectionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionSectionGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionSectionGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionSectionGroupUpdatableFieldsEquals(
            partialUpdatedQuestionSectionGroup,
            getPersistedQuestionSectionGroup(partialUpdatedQuestionSectionGroup)
        );
    }

    @Test
    @Transactional
    void patchNonExistingQuestionSectionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionSectionGroup.setId(longCount.incrementAndGet());

        // Create the QuestionSectionGroup
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionSectionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionSectionGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionSectionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionSectionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionSectionGroup.setId(longCount.incrementAndGet());

        // Create the QuestionSectionGroup
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionSectionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionSectionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionSectionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionSectionGroup.setId(longCount.incrementAndGet());

        // Create the QuestionSectionGroup
        QuestionSectionGroupDTO questionSectionGroupDTO = questionSectionGroupMapper.toDto(questionSectionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionSectionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionSectionGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionSectionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionSectionGroup() throws Exception {
        // Initialize the database
        insertedQuestionSectionGroup = questionSectionGroupRepository.saveAndFlush(questionSectionGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the questionSectionGroup
        restQuestionSectionGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionSectionGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionSectionGroupRepository.count();
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

    protected QuestionSectionGroup getPersistedQuestionSectionGroup(QuestionSectionGroup questionSectionGroup) {
        return questionSectionGroupRepository.findById(questionSectionGroup.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionSectionGroupToMatchAllProperties(QuestionSectionGroup expectedQuestionSectionGroup) {
        assertQuestionSectionGroupAllPropertiesEquals(
            expectedQuestionSectionGroup,
            getPersistedQuestionSectionGroup(expectedQuestionSectionGroup)
        );
    }

    protected void assertPersistedQuestionSectionGroupToMatchUpdatableProperties(QuestionSectionGroup expectedQuestionSectionGroup) {
        assertQuestionSectionGroupAllUpdatablePropertiesEquals(
            expectedQuestionSectionGroup,
            getPersistedQuestionSectionGroup(expectedQuestionSectionGroup)
        );
    }
}
