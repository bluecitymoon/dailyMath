package com.turling.web.rest;

import static com.turling.domain.StudentSectionLogAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.StudentSectionLog;
import com.turling.repository.StudentSectionLogRepository;
import com.turling.service.dto.StudentSectionLogDTO;
import com.turling.service.mapper.StudentSectionLogMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link StudentSectionLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentSectionLogResourceIT {

    private static final Long DEFAULT_STUDENT_ID = 1L;
    private static final Long UPDATED_STUDENT_ID = 2L;
    private static final Long SMALLER_STUDENT_ID = 1L - 1L;

    private static final Long DEFAULT_SECTION_ID = 1L;
    private static final Long UPDATED_SECTION_ID = 2L;
    private static final Long SMALLER_SECTION_ID = 1L - 1L;

    private static final Integer DEFAULT_TOTAL_COUNT = 1;
    private static final Integer UPDATED_TOTAL_COUNT = 2;
    private static final Integer SMALLER_TOTAL_COUNT = 1 - 1;

    private static final Integer DEFAULT_FINISHED_COUNT = 1;
    private static final Integer UPDATED_FINISHED_COUNT = 2;
    private static final Integer SMALLER_FINISHED_COUNT = 1 - 1;

    private static final Double DEFAULT_CORRECT_RATE = 1D;
    private static final Double UPDATED_CORRECT_RATE = 2D;
    private static final Double SMALLER_CORRECT_RATE = 1D - 1D;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/student-section-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudentSectionLogRepository studentSectionLogRepository;

    @Autowired
    private StudentSectionLogMapper studentSectionLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentSectionLogMockMvc;

    private StudentSectionLog studentSectionLog;

    private StudentSectionLog insertedStudentSectionLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentSectionLog createEntity() {
        return new StudentSectionLog()
            .studentId(DEFAULT_STUDENT_ID)
            .sectionId(DEFAULT_SECTION_ID)
            .totalCount(DEFAULT_TOTAL_COUNT)
            .finishedCount(DEFAULT_FINISHED_COUNT)
            .correctRate(DEFAULT_CORRECT_RATE)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentSectionLog createUpdatedEntity() {
        return new StudentSectionLog()
            .studentId(UPDATED_STUDENT_ID)
            .sectionId(UPDATED_SECTION_ID)
            .totalCount(UPDATED_TOTAL_COUNT)
            .finishedCount(UPDATED_FINISHED_COUNT)
            .correctRate(UPDATED_CORRECT_RATE)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);
    }

    @BeforeEach
    void initTest() {
        studentSectionLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStudentSectionLog != null) {
            studentSectionLogRepository.delete(insertedStudentSectionLog);
            insertedStudentSectionLog = null;
        }
    }

    @Test
    @Transactional
    void createStudentSectionLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StudentSectionLog
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);
        var returnedStudentSectionLogDTO = om.readValue(
            restStudentSectionLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentSectionLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudentSectionLogDTO.class
        );

        // Validate the StudentSectionLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStudentSectionLog = studentSectionLogMapper.toEntity(returnedStudentSectionLogDTO);
        assertStudentSectionLogUpdatableFieldsEquals(returnedStudentSectionLog, getPersistedStudentSectionLog(returnedStudentSectionLog));

        insertedStudentSectionLog = returnedStudentSectionLog;
    }

    @Test
    @Transactional
    void createStudentSectionLogWithExistingId() throws Exception {
        // Create the StudentSectionLog with an existing ID
        studentSectionLog.setId(1L);
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentSectionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentSectionLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogs() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList
        restStudentSectionLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentSectionLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].totalCount").value(hasItem(DEFAULT_TOTAL_COUNT)))
            .andExpect(jsonPath("$.[*].finishedCount").value(hasItem(DEFAULT_FINISHED_COUNT)))
            .andExpect(jsonPath("$.[*].correctRate").value(hasItem(DEFAULT_CORRECT_RATE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentSectionLog() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get the studentSectionLog
        restStudentSectionLogMockMvc
            .perform(get(ENTITY_API_URL_ID, studentSectionLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentSectionLog.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID.intValue()))
            .andExpect(jsonPath("$.sectionId").value(DEFAULT_SECTION_ID.intValue()))
            .andExpect(jsonPath("$.totalCount").value(DEFAULT_TOTAL_COUNT))
            .andExpect(jsonPath("$.finishedCount").value(DEFAULT_FINISHED_COUNT))
            .andExpect(jsonPath("$.correctRate").value(DEFAULT_CORRECT_RATE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentSectionLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        Long id = studentSectionLog.getId();

        defaultStudentSectionLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStudentSectionLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStudentSectionLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByStudentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where studentId equals to
        defaultStudentSectionLogFiltering("studentId.equals=" + DEFAULT_STUDENT_ID, "studentId.equals=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByStudentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where studentId in
        defaultStudentSectionLogFiltering(
            "studentId.in=" + DEFAULT_STUDENT_ID + "," + UPDATED_STUDENT_ID,
            "studentId.in=" + UPDATED_STUDENT_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByStudentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where studentId is not null
        defaultStudentSectionLogFiltering("studentId.specified=true", "studentId.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByStudentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where studentId is greater than or equal to
        defaultStudentSectionLogFiltering(
            "studentId.greaterThanOrEqual=" + DEFAULT_STUDENT_ID,
            "studentId.greaterThanOrEqual=" + UPDATED_STUDENT_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByStudentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where studentId is less than or equal to
        defaultStudentSectionLogFiltering(
            "studentId.lessThanOrEqual=" + DEFAULT_STUDENT_ID,
            "studentId.lessThanOrEqual=" + SMALLER_STUDENT_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByStudentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where studentId is less than
        defaultStudentSectionLogFiltering("studentId.lessThan=" + UPDATED_STUDENT_ID, "studentId.lessThan=" + DEFAULT_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByStudentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where studentId is greater than
        defaultStudentSectionLogFiltering("studentId.greaterThan=" + SMALLER_STUDENT_ID, "studentId.greaterThan=" + DEFAULT_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsBySectionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where sectionId equals to
        defaultStudentSectionLogFiltering("sectionId.equals=" + DEFAULT_SECTION_ID, "sectionId.equals=" + UPDATED_SECTION_ID);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsBySectionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where sectionId in
        defaultStudentSectionLogFiltering(
            "sectionId.in=" + DEFAULT_SECTION_ID + "," + UPDATED_SECTION_ID,
            "sectionId.in=" + UPDATED_SECTION_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsBySectionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where sectionId is not null
        defaultStudentSectionLogFiltering("sectionId.specified=true", "sectionId.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsBySectionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where sectionId is greater than or equal to
        defaultStudentSectionLogFiltering(
            "sectionId.greaterThanOrEqual=" + DEFAULT_SECTION_ID,
            "sectionId.greaterThanOrEqual=" + UPDATED_SECTION_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsBySectionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where sectionId is less than or equal to
        defaultStudentSectionLogFiltering(
            "sectionId.lessThanOrEqual=" + DEFAULT_SECTION_ID,
            "sectionId.lessThanOrEqual=" + SMALLER_SECTION_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsBySectionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where sectionId is less than
        defaultStudentSectionLogFiltering("sectionId.lessThan=" + UPDATED_SECTION_ID, "sectionId.lessThan=" + DEFAULT_SECTION_ID);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsBySectionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where sectionId is greater than
        defaultStudentSectionLogFiltering("sectionId.greaterThan=" + SMALLER_SECTION_ID, "sectionId.greaterThan=" + DEFAULT_SECTION_ID);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByTotalCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where totalCount equals to
        defaultStudentSectionLogFiltering("totalCount.equals=" + DEFAULT_TOTAL_COUNT, "totalCount.equals=" + UPDATED_TOTAL_COUNT);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByTotalCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where totalCount in
        defaultStudentSectionLogFiltering(
            "totalCount.in=" + DEFAULT_TOTAL_COUNT + "," + UPDATED_TOTAL_COUNT,
            "totalCount.in=" + UPDATED_TOTAL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByTotalCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where totalCount is not null
        defaultStudentSectionLogFiltering("totalCount.specified=true", "totalCount.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByTotalCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where totalCount is greater than or equal to
        defaultStudentSectionLogFiltering(
            "totalCount.greaterThanOrEqual=" + DEFAULT_TOTAL_COUNT,
            "totalCount.greaterThanOrEqual=" + UPDATED_TOTAL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByTotalCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where totalCount is less than or equal to
        defaultStudentSectionLogFiltering(
            "totalCount.lessThanOrEqual=" + DEFAULT_TOTAL_COUNT,
            "totalCount.lessThanOrEqual=" + SMALLER_TOTAL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByTotalCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where totalCount is less than
        defaultStudentSectionLogFiltering("totalCount.lessThan=" + UPDATED_TOTAL_COUNT, "totalCount.lessThan=" + DEFAULT_TOTAL_COUNT);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByTotalCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where totalCount is greater than
        defaultStudentSectionLogFiltering("totalCount.greaterThan=" + SMALLER_TOTAL_COUNT, "totalCount.greaterThan=" + DEFAULT_TOTAL_COUNT);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByFinishedCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where finishedCount equals to
        defaultStudentSectionLogFiltering(
            "finishedCount.equals=" + DEFAULT_FINISHED_COUNT,
            "finishedCount.equals=" + UPDATED_FINISHED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByFinishedCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where finishedCount in
        defaultStudentSectionLogFiltering(
            "finishedCount.in=" + DEFAULT_FINISHED_COUNT + "," + UPDATED_FINISHED_COUNT,
            "finishedCount.in=" + UPDATED_FINISHED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByFinishedCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where finishedCount is not null
        defaultStudentSectionLogFiltering("finishedCount.specified=true", "finishedCount.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByFinishedCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where finishedCount is greater than or equal to
        defaultStudentSectionLogFiltering(
            "finishedCount.greaterThanOrEqual=" + DEFAULT_FINISHED_COUNT,
            "finishedCount.greaterThanOrEqual=" + UPDATED_FINISHED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByFinishedCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where finishedCount is less than or equal to
        defaultStudentSectionLogFiltering(
            "finishedCount.lessThanOrEqual=" + DEFAULT_FINISHED_COUNT,
            "finishedCount.lessThanOrEqual=" + SMALLER_FINISHED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByFinishedCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where finishedCount is less than
        defaultStudentSectionLogFiltering(
            "finishedCount.lessThan=" + UPDATED_FINISHED_COUNT,
            "finishedCount.lessThan=" + DEFAULT_FINISHED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByFinishedCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where finishedCount is greater than
        defaultStudentSectionLogFiltering(
            "finishedCount.greaterThan=" + SMALLER_FINISHED_COUNT,
            "finishedCount.greaterThan=" + DEFAULT_FINISHED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCorrectRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where correctRate equals to
        defaultStudentSectionLogFiltering("correctRate.equals=" + DEFAULT_CORRECT_RATE, "correctRate.equals=" + UPDATED_CORRECT_RATE);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCorrectRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where correctRate in
        defaultStudentSectionLogFiltering(
            "correctRate.in=" + DEFAULT_CORRECT_RATE + "," + UPDATED_CORRECT_RATE,
            "correctRate.in=" + UPDATED_CORRECT_RATE
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCorrectRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where correctRate is not null
        defaultStudentSectionLogFiltering("correctRate.specified=true", "correctRate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCorrectRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where correctRate is greater than or equal to
        defaultStudentSectionLogFiltering(
            "correctRate.greaterThanOrEqual=" + DEFAULT_CORRECT_RATE,
            "correctRate.greaterThanOrEqual=" + UPDATED_CORRECT_RATE
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCorrectRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where correctRate is less than or equal to
        defaultStudentSectionLogFiltering(
            "correctRate.lessThanOrEqual=" + DEFAULT_CORRECT_RATE,
            "correctRate.lessThanOrEqual=" + SMALLER_CORRECT_RATE
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCorrectRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where correctRate is less than
        defaultStudentSectionLogFiltering("correctRate.lessThan=" + UPDATED_CORRECT_RATE, "correctRate.lessThan=" + DEFAULT_CORRECT_RATE);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCorrectRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where correctRate is greater than
        defaultStudentSectionLogFiltering(
            "correctRate.greaterThan=" + SMALLER_CORRECT_RATE,
            "correctRate.greaterThan=" + DEFAULT_CORRECT_RATE
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where createDate equals to
        defaultStudentSectionLogFiltering("createDate.equals=" + DEFAULT_CREATE_DATE, "createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where createDate in
        defaultStudentSectionLogFiltering(
            "createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE,
            "createDate.in=" + UPDATED_CREATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where createDate is not null
        defaultStudentSectionLogFiltering("createDate.specified=true", "createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByUpdateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where updateDate equals to
        defaultStudentSectionLogFiltering("updateDate.equals=" + DEFAULT_UPDATE_DATE, "updateDate.equals=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByUpdateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where updateDate in
        defaultStudentSectionLogFiltering(
            "updateDate.in=" + DEFAULT_UPDATE_DATE + "," + UPDATED_UPDATE_DATE,
            "updateDate.in=" + UPDATED_UPDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllStudentSectionLogsByUpdateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        // Get all the studentSectionLogList where updateDate is not null
        defaultStudentSectionLogFiltering("updateDate.specified=true", "updateDate.specified=false");
    }

    private void defaultStudentSectionLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStudentSectionLogShouldBeFound(shouldBeFound);
        defaultStudentSectionLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentSectionLogShouldBeFound(String filter) throws Exception {
        restStudentSectionLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentSectionLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].totalCount").value(hasItem(DEFAULT_TOTAL_COUNT)))
            .andExpect(jsonPath("$.[*].finishedCount").value(hasItem(DEFAULT_FINISHED_COUNT)))
            .andExpect(jsonPath("$.[*].correctRate").value(hasItem(DEFAULT_CORRECT_RATE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentSectionLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentSectionLogShouldNotBeFound(String filter) throws Exception {
        restStudentSectionLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentSectionLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentSectionLog() throws Exception {
        // Get the studentSectionLog
        restStudentSectionLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentSectionLog() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentSectionLog
        StudentSectionLog updatedStudentSectionLog = studentSectionLogRepository.findById(studentSectionLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudentSectionLog are not directly saved in db
        em.detach(updatedStudentSectionLog);
        updatedStudentSectionLog
            .studentId(UPDATED_STUDENT_ID)
            .sectionId(UPDATED_SECTION_ID)
            .totalCount(UPDATED_TOTAL_COUNT)
            .finishedCount(UPDATED_FINISHED_COUNT)
            .correctRate(UPDATED_CORRECT_RATE)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(updatedStudentSectionLog);

        restStudentSectionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentSectionLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentSectionLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudentSectionLogToMatchAllProperties(updatedStudentSectionLog);
    }

    @Test
    @Transactional
    void putNonExistingStudentSectionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentSectionLog.setId(longCount.incrementAndGet());

        // Create the StudentSectionLog
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentSectionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentSectionLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentSectionLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentSectionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentSectionLog.setId(longCount.incrementAndGet());

        // Create the StudentSectionLog
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSectionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentSectionLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentSectionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentSectionLog.setId(longCount.incrementAndGet());

        // Create the StudentSectionLog
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSectionLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentSectionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentSectionLogWithPatch() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentSectionLog using partial update
        StudentSectionLog partialUpdatedStudentSectionLog = new StudentSectionLog();
        partialUpdatedStudentSectionLog.setId(studentSectionLog.getId());

        partialUpdatedStudentSectionLog.sectionId(UPDATED_SECTION_ID);

        restStudentSectionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentSectionLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentSectionLog))
            )
            .andExpect(status().isOk());

        // Validate the StudentSectionLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentSectionLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStudentSectionLog, studentSectionLog),
            getPersistedStudentSectionLog(studentSectionLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateStudentSectionLogWithPatch() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentSectionLog using partial update
        StudentSectionLog partialUpdatedStudentSectionLog = new StudentSectionLog();
        partialUpdatedStudentSectionLog.setId(studentSectionLog.getId());

        partialUpdatedStudentSectionLog
            .studentId(UPDATED_STUDENT_ID)
            .sectionId(UPDATED_SECTION_ID)
            .totalCount(UPDATED_TOTAL_COUNT)
            .finishedCount(UPDATED_FINISHED_COUNT)
            .correctRate(UPDATED_CORRECT_RATE)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restStudentSectionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentSectionLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentSectionLog))
            )
            .andExpect(status().isOk());

        // Validate the StudentSectionLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentSectionLogUpdatableFieldsEquals(
            partialUpdatedStudentSectionLog,
            getPersistedStudentSectionLog(partialUpdatedStudentSectionLog)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStudentSectionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentSectionLog.setId(longCount.incrementAndGet());

        // Create the StudentSectionLog
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentSectionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentSectionLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentSectionLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentSectionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentSectionLog.setId(longCount.incrementAndGet());

        // Create the StudentSectionLog
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSectionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentSectionLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentSectionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentSectionLog.setId(longCount.incrementAndGet());

        // Create the StudentSectionLog
        StudentSectionLogDTO studentSectionLogDTO = studentSectionLogMapper.toDto(studentSectionLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSectionLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studentSectionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentSectionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentSectionLog() throws Exception {
        // Initialize the database
        insertedStudentSectionLog = studentSectionLogRepository.saveAndFlush(studentSectionLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studentSectionLog
        restStudentSectionLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentSectionLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studentSectionLogRepository.count();
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

    protected StudentSectionLog getPersistedStudentSectionLog(StudentSectionLog studentSectionLog) {
        return studentSectionLogRepository.findById(studentSectionLog.getId()).orElseThrow();
    }

    protected void assertPersistedStudentSectionLogToMatchAllProperties(StudentSectionLog expectedStudentSectionLog) {
        assertStudentSectionLogAllPropertiesEquals(expectedStudentSectionLog, getPersistedStudentSectionLog(expectedStudentSectionLog));
    }

    protected void assertPersistedStudentSectionLogToMatchUpdatableProperties(StudentSectionLog expectedStudentSectionLog) {
        assertStudentSectionLogAllUpdatablePropertiesEquals(
            expectedStudentSectionLog,
            getPersistedStudentSectionLog(expectedStudentSectionLog)
        );
    }
}
