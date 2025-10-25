package com.turling.web.rest;

import static com.turling.domain.SchoolAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.Distinct;
import com.turling.domain.School;
import com.turling.repository.SchoolRepository;
import com.turling.service.SchoolService;
import com.turling.service.dto.SchoolDTO;
import com.turling.service.mapper.SchoolMapper;
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
 * Integration tests for the {@link SchoolResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_REGISTERED_STUDENTS_COUNT = 1;
    private static final Integer UPDATED_REGISTERED_STUDENTS_COUNT = 2;
    private static final Integer SMALLER_REGISTERED_STUDENTS_COUNT = 1 - 1;

    private static final String DEFAULT_PINYIN = "AAAAAAAAAA";
    private static final String UPDATED_PINYIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/schools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolRepository schoolRepositoryMock;

    @Autowired
    private SchoolMapper schoolMapper;

    @Mock
    private SchoolService schoolServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolMockMvc;

    private School school;

    private School insertedSchool;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static School createEntity() {
        return new School().name(DEFAULT_NAME).registeredStudentsCount(DEFAULT_REGISTERED_STUDENTS_COUNT).pinyin(DEFAULT_PINYIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static School createUpdatedEntity() {
        return new School().name(UPDATED_NAME).registeredStudentsCount(UPDATED_REGISTERED_STUDENTS_COUNT).pinyin(UPDATED_PINYIN);
    }

    @BeforeEach
    void initTest() {
        school = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSchool != null) {
            schoolRepository.delete(insertedSchool);
            insertedSchool = null;
        }
    }

    @Test
    @Transactional
    void createSchool() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);
        var returnedSchoolDTO = om.readValue(
            restSchoolMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SchoolDTO.class
        );

        // Validate the School in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSchool = schoolMapper.toEntity(returnedSchoolDTO);
        assertSchoolUpdatableFieldsEquals(returnedSchool, getPersistedSchool(returnedSchool));

        insertedSchool = returnedSchool;
    }

    @Test
    @Transactional
    void createSchoolWithExistingId() throws Exception {
        // Create the School with an existing ID
        school.setId(1L);
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSchools() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(school.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].registeredStudentsCount").value(hasItem(DEFAULT_REGISTERED_STUDENTS_COUNT)))
            .andExpect(jsonPath("$.[*].pinyin").value(hasItem(DEFAULT_PINYIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolsWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(schoolRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSchool() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get the school
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL_ID, school.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(school.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.registeredStudentsCount").value(DEFAULT_REGISTERED_STUDENTS_COUNT))
            .andExpect(jsonPath("$.pinyin").value(DEFAULT_PINYIN));
    }

    @Test
    @Transactional
    void getSchoolsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        Long id = school.getId();

        defaultSchoolFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSchoolFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSchoolFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name equals to
        defaultSchoolFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name in
        defaultSchoolFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name is not null
        defaultSchoolFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name contains
        defaultSchoolFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where name does not contain
        defaultSchoolFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByRegisteredStudentsCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where registeredStudentsCount equals to
        defaultSchoolFiltering(
            "registeredStudentsCount.equals=" + DEFAULT_REGISTERED_STUDENTS_COUNT,
            "registeredStudentsCount.equals=" + UPDATED_REGISTERED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSchoolsByRegisteredStudentsCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where registeredStudentsCount in
        defaultSchoolFiltering(
            "registeredStudentsCount.in=" + DEFAULT_REGISTERED_STUDENTS_COUNT + "," + UPDATED_REGISTERED_STUDENTS_COUNT,
            "registeredStudentsCount.in=" + UPDATED_REGISTERED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSchoolsByRegisteredStudentsCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where registeredStudentsCount is not null
        defaultSchoolFiltering("registeredStudentsCount.specified=true", "registeredStudentsCount.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByRegisteredStudentsCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where registeredStudentsCount is greater than or equal to
        defaultSchoolFiltering(
            "registeredStudentsCount.greaterThanOrEqual=" + DEFAULT_REGISTERED_STUDENTS_COUNT,
            "registeredStudentsCount.greaterThanOrEqual=" + UPDATED_REGISTERED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSchoolsByRegisteredStudentsCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where registeredStudentsCount is less than or equal to
        defaultSchoolFiltering(
            "registeredStudentsCount.lessThanOrEqual=" + DEFAULT_REGISTERED_STUDENTS_COUNT,
            "registeredStudentsCount.lessThanOrEqual=" + SMALLER_REGISTERED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSchoolsByRegisteredStudentsCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where registeredStudentsCount is less than
        defaultSchoolFiltering(
            "registeredStudentsCount.lessThan=" + UPDATED_REGISTERED_STUDENTS_COUNT,
            "registeredStudentsCount.lessThan=" + DEFAULT_REGISTERED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSchoolsByRegisteredStudentsCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where registeredStudentsCount is greater than
        defaultSchoolFiltering(
            "registeredStudentsCount.greaterThan=" + SMALLER_REGISTERED_STUDENTS_COUNT,
            "registeredStudentsCount.greaterThan=" + DEFAULT_REGISTERED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSchoolsByPinyinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where pinyin equals to
        defaultSchoolFiltering("pinyin.equals=" + DEFAULT_PINYIN, "pinyin.equals=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    void getAllSchoolsByPinyinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where pinyin in
        defaultSchoolFiltering("pinyin.in=" + DEFAULT_PINYIN + "," + UPDATED_PINYIN, "pinyin.in=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    void getAllSchoolsByPinyinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where pinyin is not null
        defaultSchoolFiltering("pinyin.specified=true", "pinyin.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByPinyinContainsSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where pinyin contains
        defaultSchoolFiltering("pinyin.contains=" + DEFAULT_PINYIN, "pinyin.contains=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    void getAllSchoolsByPinyinNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        // Get all the schoolList where pinyin does not contain
        defaultSchoolFiltering("pinyin.doesNotContain=" + UPDATED_PINYIN, "pinyin.doesNotContain=" + DEFAULT_PINYIN);
    }

    @Test
    @Transactional
    void getAllSchoolsByDistinctIsEqualToSomething() throws Exception {
        Distinct distinct;
        if (TestUtil.findAll(em, Distinct.class).isEmpty()) {
            schoolRepository.saveAndFlush(school);
            distinct = DistinctResourceIT.createEntity();
        } else {
            distinct = TestUtil.findAll(em, Distinct.class).get(0);
        }
        em.persist(distinct);
        em.flush();
        school.setDistinct(distinct);
        schoolRepository.saveAndFlush(school);
        Long distinctId = distinct.getId();
        // Get all the schoolList where distinct equals to distinctId
        defaultSchoolShouldBeFound("distinctId.equals=" + distinctId);

        // Get all the schoolList where distinct equals to (distinctId + 1)
        defaultSchoolShouldNotBeFound("distinctId.equals=" + (distinctId + 1));
    }

    private void defaultSchoolFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSchoolShouldBeFound(shouldBeFound);
        defaultSchoolShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolShouldBeFound(String filter) throws Exception {
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(school.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].registeredStudentsCount").value(hasItem(DEFAULT_REGISTERED_STUDENTS_COUNT)))
            .andExpect(jsonPath("$.[*].pinyin").value(hasItem(DEFAULT_PINYIN)));

        // Check, that the count call also returns 1
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolShouldNotBeFound(String filter) throws Exception {
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchool() throws Exception {
        // Get the school
        restSchoolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchool() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the school
        School updatedSchool = schoolRepository.findById(school.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchool are not directly saved in db
        em.detach(updatedSchool);
        updatedSchool.name(UPDATED_NAME).registeredStudentsCount(UPDATED_REGISTERED_STUDENTS_COUNT).pinyin(UPDATED_PINYIN);
        SchoolDTO schoolDTO = schoolMapper.toDto(updatedSchool);

        restSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDTO))
            )
            .andExpect(status().isOk());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSchoolToMatchAllProperties(updatedSchool);
    }

    @Test
    @Transactional
    void putNonExistingSchool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        school.setId(longCount.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        school.setId(longCount.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        school.setId(longCount.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolWithPatch() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the school using partial update
        School partialUpdatedSchool = new School();
        partialUpdatedSchool.setId(school.getId());

        partialUpdatedSchool.name(UPDATED_NAME);

        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchool.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchool))
            )
            .andExpect(status().isOk());

        // Validate the School in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSchoolUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSchool, school), getPersistedSchool(school));
    }

    @Test
    @Transactional
    void fullUpdateSchoolWithPatch() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the school using partial update
        School partialUpdatedSchool = new School();
        partialUpdatedSchool.setId(school.getId());

        partialUpdatedSchool.name(UPDATED_NAME).registeredStudentsCount(UPDATED_REGISTERED_STUDENTS_COUNT).pinyin(UPDATED_PINYIN);

        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchool.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchool))
            )
            .andExpect(status().isOk());

        // Validate the School in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSchoolUpdatableFieldsEquals(partialUpdatedSchool, getPersistedSchool(partialUpdatedSchool));
    }

    @Test
    @Transactional
    void patchNonExistingSchool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        school.setId(longCount.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        school.setId(longCount.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchool() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        school.setId(longCount.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(schoolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the School in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchool() throws Exception {
        // Initialize the database
        insertedSchool = schoolRepository.saveAndFlush(school);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the school
        restSchoolMockMvc
            .perform(delete(ENTITY_API_URL_ID, school.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return schoolRepository.count();
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

    protected School getPersistedSchool(School school) {
        return schoolRepository.findById(school.getId()).orElseThrow();
    }

    protected void assertPersistedSchoolToMatchAllProperties(School expectedSchool) {
        assertSchoolAllPropertiesEquals(expectedSchool, getPersistedSchool(expectedSchool));
    }

    protected void assertPersistedSchoolToMatchUpdatableProperties(School expectedSchool) {
        assertSchoolAllUpdatablePropertiesEquals(expectedSchool, getPersistedSchool(expectedSchool));
    }
}
