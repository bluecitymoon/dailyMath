package com.turling.web.rest;

import static com.turling.domain.DistinctAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.Distinct;
import com.turling.repository.DistinctRepository;
import com.turling.service.dto.DistinctDTO;
import com.turling.service.mapper.DistinctMapper;
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
 * Integration tests for the {@link DistinctResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DistinctResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PINYIN = "AAAAAAAAAA";
    private static final String UPDATED_PINYIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/distincts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DistinctRepository distinctRepository;

    @Autowired
    private DistinctMapper distinctMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDistinctMockMvc;

    private Distinct distinct;

    private Distinct insertedDistinct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distinct createEntity() {
        return new Distinct().name(DEFAULT_NAME).pinyin(DEFAULT_PINYIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distinct createUpdatedEntity() {
        return new Distinct().name(UPDATED_NAME).pinyin(UPDATED_PINYIN);
    }

    @BeforeEach
    void initTest() {
        distinct = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDistinct != null) {
            distinctRepository.delete(insertedDistinct);
            insertedDistinct = null;
        }
    }

    @Test
    @Transactional
    void createDistinct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Distinct
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);
        var returnedDistinctDTO = om.readValue(
            restDistinctMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distinctDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DistinctDTO.class
        );

        // Validate the Distinct in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDistinct = distinctMapper.toEntity(returnedDistinctDTO);
        assertDistinctUpdatableFieldsEquals(returnedDistinct, getPersistedDistinct(returnedDistinct));

        insertedDistinct = returnedDistinct;
    }

    @Test
    @Transactional
    void createDistinctWithExistingId() throws Exception {
        // Create the Distinct with an existing ID
        distinct.setId(1L);
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistinctMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distinctDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDistincts() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList
        restDistinctMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distinct.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pinyin").value(hasItem(DEFAULT_PINYIN)));
    }

    @Test
    @Transactional
    void getDistinct() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get the distinct
        restDistinctMockMvc
            .perform(get(ENTITY_API_URL_ID, distinct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(distinct.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pinyin").value(DEFAULT_PINYIN));
    }

    @Test
    @Transactional
    void getDistinctsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        Long id = distinct.getId();

        defaultDistinctFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDistinctFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDistinctFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDistinctsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where name equals to
        defaultDistinctFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistinctsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where name in
        defaultDistinctFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistinctsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where name is not null
        defaultDistinctFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDistinctsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where name contains
        defaultDistinctFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistinctsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where name does not contain
        defaultDistinctFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDistinctsByPinyinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where pinyin equals to
        defaultDistinctFiltering("pinyin.equals=" + DEFAULT_PINYIN, "pinyin.equals=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    void getAllDistinctsByPinyinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where pinyin in
        defaultDistinctFiltering("pinyin.in=" + DEFAULT_PINYIN + "," + UPDATED_PINYIN, "pinyin.in=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    void getAllDistinctsByPinyinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where pinyin is not null
        defaultDistinctFiltering("pinyin.specified=true", "pinyin.specified=false");
    }

    @Test
    @Transactional
    void getAllDistinctsByPinyinContainsSomething() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where pinyin contains
        defaultDistinctFiltering("pinyin.contains=" + DEFAULT_PINYIN, "pinyin.contains=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    void getAllDistinctsByPinyinNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        // Get all the distinctList where pinyin does not contain
        defaultDistinctFiltering("pinyin.doesNotContain=" + UPDATED_PINYIN, "pinyin.doesNotContain=" + DEFAULT_PINYIN);
    }

    private void defaultDistinctFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDistinctShouldBeFound(shouldBeFound);
        defaultDistinctShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDistinctShouldBeFound(String filter) throws Exception {
        restDistinctMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distinct.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pinyin").value(hasItem(DEFAULT_PINYIN)));

        // Check, that the count call also returns 1
        restDistinctMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDistinctShouldNotBeFound(String filter) throws Exception {
        restDistinctMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDistinctMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDistinct() throws Exception {
        // Get the distinct
        restDistinctMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDistinct() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distinct
        Distinct updatedDistinct = distinctRepository.findById(distinct.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDistinct are not directly saved in db
        em.detach(updatedDistinct);
        updatedDistinct.name(UPDATED_NAME).pinyin(UPDATED_PINYIN);
        DistinctDTO distinctDTO = distinctMapper.toDto(updatedDistinct);

        restDistinctMockMvc
            .perform(
                put(ENTITY_API_URL_ID, distinctDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distinctDTO))
            )
            .andExpect(status().isOk());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDistinctToMatchAllProperties(updatedDistinct);
    }

    @Test
    @Transactional
    void putNonExistingDistinct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distinct.setId(longCount.incrementAndGet());

        // Create the Distinct
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistinctMockMvc
            .perform(
                put(ENTITY_API_URL_ID, distinctDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distinctDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDistinct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distinct.setId(longCount.incrementAndGet());

        // Create the Distinct
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistinctMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distinctDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDistinct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distinct.setId(longCount.incrementAndGet());

        // Create the Distinct
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistinctMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distinctDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDistinctWithPatch() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distinct using partial update
        Distinct partialUpdatedDistinct = new Distinct();
        partialUpdatedDistinct.setId(distinct.getId());

        partialUpdatedDistinct.pinyin(UPDATED_PINYIN);

        restDistinctMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistinct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistinct))
            )
            .andExpect(status().isOk());

        // Validate the Distinct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistinctUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDistinct, distinct), getPersistedDistinct(distinct));
    }

    @Test
    @Transactional
    void fullUpdateDistinctWithPatch() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distinct using partial update
        Distinct partialUpdatedDistinct = new Distinct();
        partialUpdatedDistinct.setId(distinct.getId());

        partialUpdatedDistinct.name(UPDATED_NAME).pinyin(UPDATED_PINYIN);

        restDistinctMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistinct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistinct))
            )
            .andExpect(status().isOk());

        // Validate the Distinct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistinctUpdatableFieldsEquals(partialUpdatedDistinct, getPersistedDistinct(partialUpdatedDistinct));
    }

    @Test
    @Transactional
    void patchNonExistingDistinct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distinct.setId(longCount.incrementAndGet());

        // Create the Distinct
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistinctMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, distinctDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distinctDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDistinct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distinct.setId(longCount.incrementAndGet());

        // Create the Distinct
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistinctMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distinctDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDistinct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distinct.setId(longCount.incrementAndGet());

        // Create the Distinct
        DistinctDTO distinctDTO = distinctMapper.toDto(distinct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistinctMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(distinctDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distinct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDistinct() throws Exception {
        // Initialize the database
        insertedDistinct = distinctRepository.saveAndFlush(distinct);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the distinct
        restDistinctMockMvc
            .perform(delete(ENTITY_API_URL_ID, distinct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return distinctRepository.count();
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

    protected Distinct getPersistedDistinct(Distinct distinct) {
        return distinctRepository.findById(distinct.getId()).orElseThrow();
    }

    protected void assertPersistedDistinctToMatchAllProperties(Distinct expectedDistinct) {
        assertDistinctAllPropertiesEquals(expectedDistinct, getPersistedDistinct(expectedDistinct));
    }

    protected void assertPersistedDistinctToMatchUpdatableProperties(Distinct expectedDistinct) {
        assertDistinctAllUpdatablePropertiesEquals(expectedDistinct, getPersistedDistinct(expectedDistinct));
    }
}
