package com.turling.web.rest;

import static com.turling.domain.StudentGradeAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.StudentGrade;
import com.turling.repository.StudentGradeRepository;
import com.turling.service.dto.StudentGradeDTO;
import com.turling.service.mapper.StudentGradeMapper;
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
 * Integration tests for the {@link StudentGradeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentGradeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final Integer DEFAULT_TERM = 1;
    private static final Integer UPDATED_TERM = 2;

    private static final String ENTITY_API_URL = "/api/student-grades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudentGradeRepository studentGradeRepository;

    @Autowired
    private StudentGradeMapper studentGradeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentGradeMockMvc;

    private StudentGrade studentGrade;

    private StudentGrade insertedStudentGrade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentGrade createEntity() {
        return new StudentGrade().name(DEFAULT_NAME).index(DEFAULT_INDEX).term(DEFAULT_TERM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentGrade createUpdatedEntity() {
        return new StudentGrade().name(UPDATED_NAME).index(UPDATED_INDEX).term(UPDATED_TERM);
    }

    @BeforeEach
    void initTest() {
        studentGrade = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStudentGrade != null) {
            studentGradeRepository.delete(insertedStudentGrade);
            insertedStudentGrade = null;
        }
    }

    @Test
    @Transactional
    void createStudentGrade() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StudentGrade
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);
        var returnedStudentGradeDTO = om.readValue(
            restStudentGradeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentGradeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudentGradeDTO.class
        );

        // Validate the StudentGrade in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStudentGrade = studentGradeMapper.toEntity(returnedStudentGradeDTO);
        assertStudentGradeUpdatableFieldsEquals(returnedStudentGrade, getPersistedStudentGrade(returnedStudentGrade));

        insertedStudentGrade = returnedStudentGrade;
    }

    @Test
    @Transactional
    void createStudentGradeWithExistingId() throws Exception {
        // Create the StudentGrade with an existing ID
        studentGrade.setId(1L);
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentGradeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentGrades() throws Exception {
        // Initialize the database
        insertedStudentGrade = studentGradeRepository.saveAndFlush(studentGrade);

        // Get all the studentGradeList
        restStudentGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentGrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].term").value(hasItem(DEFAULT_TERM)));
    }

    @Test
    @Transactional
    void getStudentGrade() throws Exception {
        // Initialize the database
        insertedStudentGrade = studentGradeRepository.saveAndFlush(studentGrade);

        // Get the studentGrade
        restStudentGradeMockMvc
            .perform(get(ENTITY_API_URL_ID, studentGrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentGrade.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.term").value(DEFAULT_TERM));
    }

    @Test
    @Transactional
    void getNonExistingStudentGrade() throws Exception {
        // Get the studentGrade
        restStudentGradeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentGrade() throws Exception {
        // Initialize the database
        insertedStudentGrade = studentGradeRepository.saveAndFlush(studentGrade);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentGrade
        StudentGrade updatedStudentGrade = studentGradeRepository.findById(studentGrade.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudentGrade are not directly saved in db
        em.detach(updatedStudentGrade);
        updatedStudentGrade.name(UPDATED_NAME).index(UPDATED_INDEX).term(UPDATED_TERM);
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(updatedStudentGrade);

        restStudentGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentGradeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentGradeDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudentGradeToMatchAllProperties(updatedStudentGrade);
    }

    @Test
    @Transactional
    void putNonExistingStudentGrade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentGrade.setId(longCount.incrementAndGet());

        // Create the StudentGrade
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentGradeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentGradeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentGrade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentGrade.setId(longCount.incrementAndGet());

        // Create the StudentGrade
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentGradeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentGrade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentGrade.setId(longCount.incrementAndGet());

        // Create the StudentGrade
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentGradeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentGradeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentGradeWithPatch() throws Exception {
        // Initialize the database
        insertedStudentGrade = studentGradeRepository.saveAndFlush(studentGrade);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentGrade using partial update
        StudentGrade partialUpdatedStudentGrade = new StudentGrade();
        partialUpdatedStudentGrade.setId(studentGrade.getId());

        partialUpdatedStudentGrade.name(UPDATED_NAME).index(UPDATED_INDEX);

        restStudentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentGrade))
            )
            .andExpect(status().isOk());

        // Validate the StudentGrade in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentGradeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStudentGrade, studentGrade),
            getPersistedStudentGrade(studentGrade)
        );
    }

    @Test
    @Transactional
    void fullUpdateStudentGradeWithPatch() throws Exception {
        // Initialize the database
        insertedStudentGrade = studentGradeRepository.saveAndFlush(studentGrade);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentGrade using partial update
        StudentGrade partialUpdatedStudentGrade = new StudentGrade();
        partialUpdatedStudentGrade.setId(studentGrade.getId());

        partialUpdatedStudentGrade.name(UPDATED_NAME).index(UPDATED_INDEX).term(UPDATED_TERM);

        restStudentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentGrade))
            )
            .andExpect(status().isOk());

        // Validate the StudentGrade in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentGradeUpdatableFieldsEquals(partialUpdatedStudentGrade, getPersistedStudentGrade(partialUpdatedStudentGrade));
    }

    @Test
    @Transactional
    void patchNonExistingStudentGrade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentGrade.setId(longCount.incrementAndGet());

        // Create the StudentGrade
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentGradeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentGradeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentGrade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentGrade.setId(longCount.incrementAndGet());

        // Create the StudentGrade
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentGradeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentGrade() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentGrade.setId(longCount.incrementAndGet());

        // Create the StudentGrade
        StudentGradeDTO studentGradeDTO = studentGradeMapper.toDto(studentGrade);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentGradeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studentGradeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentGrade in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentGrade() throws Exception {
        // Initialize the database
        insertedStudentGrade = studentGradeRepository.saveAndFlush(studentGrade);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studentGrade
        restStudentGradeMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentGrade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studentGradeRepository.count();
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

    protected StudentGrade getPersistedStudentGrade(StudentGrade studentGrade) {
        return studentGradeRepository.findById(studentGrade.getId()).orElseThrow();
    }

    protected void assertPersistedStudentGradeToMatchAllProperties(StudentGrade expectedStudentGrade) {
        assertStudentGradeAllPropertiesEquals(expectedStudentGrade, getPersistedStudentGrade(expectedStudentGrade));
    }

    protected void assertPersistedStudentGradeToMatchUpdatableProperties(StudentGrade expectedStudentGrade) {
        assertStudentGradeAllUpdatablePropertiesEquals(expectedStudentGrade, getPersistedStudentGrade(expectedStudentGrade));
    }
}
