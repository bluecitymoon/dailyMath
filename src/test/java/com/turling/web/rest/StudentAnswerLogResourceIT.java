package com.turling.web.rest;

import static com.turling.domain.StudentAnswerLogAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.StudentAnswerLog;
import com.turling.repository.StudentAnswerLogRepository;
import com.turling.service.mapper.StudentAnswerLogMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link StudentAnswerLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentAnswerLogResourceIT {

    private static final Long DEFAULT_STUDENT_ID = 1L;
    private static final Long UPDATED_STUDENT_ID = 2L;
    private static final Long SMALLER_STUDENT_ID = 1L - 1L;

    private static final Long DEFAULT_QUESTION_ID = 1L;
    private static final Long UPDATED_QUESTION_ID = 2L;
    private static final Long SMALLER_QUESTION_ID = 1L - 1L;

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final Integer DEFAULT_CORRECT = 1;
    private static final Integer UPDATED_CORRECT = 2;
    private static final Integer SMALLER_CORRECT = 1 - 1;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_WIN_POINTS = 1D;
    private static final Double UPDATED_WIN_POINTS = 2D;
    private static final Double SMALLER_WIN_POINTS = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/student-answer-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudentAnswerLogRepository studentAnswerLogRepository;

    @Autowired
    private StudentAnswerLogMapper studentAnswerLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentAnswerLogMockMvc;

    private StudentAnswerLog studentAnswerLog;

    private StudentAnswerLog insertedStudentAnswerLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentAnswerLog createEntity() {
        return new StudentAnswerLog()
            .studentId(DEFAULT_STUDENT_ID)
            .questionId(DEFAULT_QUESTION_ID)
            .answer(DEFAULT_ANSWER)
            .correct(DEFAULT_CORRECT)
            .createDate(DEFAULT_CREATE_DATE)
            .winPoints(DEFAULT_WIN_POINTS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentAnswerLog createUpdatedEntity() {
        return new StudentAnswerLog()
            .studentId(UPDATED_STUDENT_ID)
            .questionId(UPDATED_QUESTION_ID)
            .answer(UPDATED_ANSWER)
            .correct(UPDATED_CORRECT)
            .createDate(UPDATED_CREATE_DATE)
            .winPoints(UPDATED_WIN_POINTS);
    }

    @BeforeEach
    void initTest() {
        studentAnswerLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStudentAnswerLog != null) {
            studentAnswerLogRepository.delete(insertedStudentAnswerLog);
            insertedStudentAnswerLog = null;
        }
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogs() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList
        restStudentAnswerLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentAnswerLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].winPoints").value(hasItem(DEFAULT_WIN_POINTS)));
    }

    @Test
    @Transactional
    void getStudentAnswerLog() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get the studentAnswerLog
        restStudentAnswerLogMockMvc
            .perform(get(ENTITY_API_URL_ID, studentAnswerLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentAnswerLog.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID.intValue()))
            .andExpect(jsonPath("$.questionId").value(DEFAULT_QUESTION_ID.intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.winPoints").value(DEFAULT_WIN_POINTS));
    }

    @Test
    @Transactional
    void getStudentAnswerLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        Long id = studentAnswerLog.getId();

        defaultStudentAnswerLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStudentAnswerLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStudentAnswerLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByStudentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where studentId equals to
        defaultStudentAnswerLogFiltering("studentId.equals=" + DEFAULT_STUDENT_ID, "studentId.equals=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByStudentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where studentId in
        defaultStudentAnswerLogFiltering(
            "studentId.in=" + DEFAULT_STUDENT_ID + "," + UPDATED_STUDENT_ID,
            "studentId.in=" + UPDATED_STUDENT_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByStudentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where studentId is not null
        defaultStudentAnswerLogFiltering("studentId.specified=true", "studentId.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByStudentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where studentId is greater than or equal to
        defaultStudentAnswerLogFiltering(
            "studentId.greaterThanOrEqual=" + DEFAULT_STUDENT_ID,
            "studentId.greaterThanOrEqual=" + UPDATED_STUDENT_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByStudentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where studentId is less than or equal to
        defaultStudentAnswerLogFiltering(
            "studentId.lessThanOrEqual=" + DEFAULT_STUDENT_ID,
            "studentId.lessThanOrEqual=" + SMALLER_STUDENT_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByStudentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where studentId is less than
        defaultStudentAnswerLogFiltering("studentId.lessThan=" + UPDATED_STUDENT_ID, "studentId.lessThan=" + DEFAULT_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByStudentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where studentId is greater than
        defaultStudentAnswerLogFiltering("studentId.greaterThan=" + SMALLER_STUDENT_ID, "studentId.greaterThan=" + DEFAULT_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByQuestionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where questionId equals to
        defaultStudentAnswerLogFiltering("questionId.equals=" + DEFAULT_QUESTION_ID, "questionId.equals=" + UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByQuestionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where questionId in
        defaultStudentAnswerLogFiltering(
            "questionId.in=" + DEFAULT_QUESTION_ID + "," + UPDATED_QUESTION_ID,
            "questionId.in=" + UPDATED_QUESTION_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByQuestionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where questionId is not null
        defaultStudentAnswerLogFiltering("questionId.specified=true", "questionId.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByQuestionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where questionId is greater than or equal to
        defaultStudentAnswerLogFiltering(
            "questionId.greaterThanOrEqual=" + DEFAULT_QUESTION_ID,
            "questionId.greaterThanOrEqual=" + UPDATED_QUESTION_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByQuestionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where questionId is less than or equal to
        defaultStudentAnswerLogFiltering(
            "questionId.lessThanOrEqual=" + DEFAULT_QUESTION_ID,
            "questionId.lessThanOrEqual=" + SMALLER_QUESTION_ID
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByQuestionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where questionId is less than
        defaultStudentAnswerLogFiltering("questionId.lessThan=" + UPDATED_QUESTION_ID, "questionId.lessThan=" + DEFAULT_QUESTION_ID);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByQuestionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where questionId is greater than
        defaultStudentAnswerLogFiltering("questionId.greaterThan=" + SMALLER_QUESTION_ID, "questionId.greaterThan=" + DEFAULT_QUESTION_ID);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where answer equals to
        defaultStudentAnswerLogFiltering("answer.equals=" + DEFAULT_ANSWER, "answer.equals=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where answer in
        defaultStudentAnswerLogFiltering("answer.in=" + DEFAULT_ANSWER + "," + UPDATED_ANSWER, "answer.in=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where answer is not null
        defaultStudentAnswerLogFiltering("answer.specified=true", "answer.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByAnswerContainsSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where answer contains
        defaultStudentAnswerLogFiltering("answer.contains=" + DEFAULT_ANSWER, "answer.contains=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByAnswerNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where answer does not contain
        defaultStudentAnswerLogFiltering("answer.doesNotContain=" + UPDATED_ANSWER, "answer.doesNotContain=" + DEFAULT_ANSWER);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCorrectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where correct equals to
        defaultStudentAnswerLogFiltering("correct.equals=" + DEFAULT_CORRECT, "correct.equals=" + UPDATED_CORRECT);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCorrectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where correct in
        defaultStudentAnswerLogFiltering("correct.in=" + DEFAULT_CORRECT + "," + UPDATED_CORRECT, "correct.in=" + UPDATED_CORRECT);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCorrectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where correct is not null
        defaultStudentAnswerLogFiltering("correct.specified=true", "correct.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCorrectIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where correct is greater than or equal to
        defaultStudentAnswerLogFiltering("correct.greaterThanOrEqual=" + DEFAULT_CORRECT, "correct.greaterThanOrEqual=" + UPDATED_CORRECT);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCorrectIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where correct is less than or equal to
        defaultStudentAnswerLogFiltering("correct.lessThanOrEqual=" + DEFAULT_CORRECT, "correct.lessThanOrEqual=" + SMALLER_CORRECT);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCorrectIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where correct is less than
        defaultStudentAnswerLogFiltering("correct.lessThan=" + UPDATED_CORRECT, "correct.lessThan=" + DEFAULT_CORRECT);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCorrectIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where correct is greater than
        defaultStudentAnswerLogFiltering("correct.greaterThan=" + SMALLER_CORRECT, "correct.greaterThan=" + DEFAULT_CORRECT);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where createDate equals to
        defaultStudentAnswerLogFiltering("createDate.equals=" + DEFAULT_CREATE_DATE, "createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where createDate in
        defaultStudentAnswerLogFiltering(
            "createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE,
            "createDate.in=" + UPDATED_CREATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where createDate is not null
        defaultStudentAnswerLogFiltering("createDate.specified=true", "createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByWinPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where winPoints equals to
        defaultStudentAnswerLogFiltering("winPoints.equals=" + DEFAULT_WIN_POINTS, "winPoints.equals=" + UPDATED_WIN_POINTS);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByWinPointsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where winPoints in
        defaultStudentAnswerLogFiltering(
            "winPoints.in=" + DEFAULT_WIN_POINTS + "," + UPDATED_WIN_POINTS,
            "winPoints.in=" + UPDATED_WIN_POINTS
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByWinPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where winPoints is not null
        defaultStudentAnswerLogFiltering("winPoints.specified=true", "winPoints.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByWinPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where winPoints is greater than or equal to
        defaultStudentAnswerLogFiltering(
            "winPoints.greaterThanOrEqual=" + DEFAULT_WIN_POINTS,
            "winPoints.greaterThanOrEqual=" + UPDATED_WIN_POINTS
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByWinPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where winPoints is less than or equal to
        defaultStudentAnswerLogFiltering(
            "winPoints.lessThanOrEqual=" + DEFAULT_WIN_POINTS,
            "winPoints.lessThanOrEqual=" + SMALLER_WIN_POINTS
        );
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByWinPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where winPoints is less than
        defaultStudentAnswerLogFiltering("winPoints.lessThan=" + UPDATED_WIN_POINTS, "winPoints.lessThan=" + DEFAULT_WIN_POINTS);
    }

    @Test
    @Transactional
    void getAllStudentAnswerLogsByWinPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentAnswerLog = studentAnswerLogRepository.saveAndFlush(studentAnswerLog);

        // Get all the studentAnswerLogList where winPoints is greater than
        defaultStudentAnswerLogFiltering("winPoints.greaterThan=" + SMALLER_WIN_POINTS, "winPoints.greaterThan=" + DEFAULT_WIN_POINTS);
    }

    private void defaultStudentAnswerLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStudentAnswerLogShouldBeFound(shouldBeFound);
        defaultStudentAnswerLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentAnswerLogShouldBeFound(String filter) throws Exception {
        restStudentAnswerLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentAnswerLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].winPoints").value(hasItem(DEFAULT_WIN_POINTS)));

        // Check, that the count call also returns 1
        restStudentAnswerLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentAnswerLogShouldNotBeFound(String filter) throws Exception {
        restStudentAnswerLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentAnswerLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentAnswerLog() throws Exception {
        // Get the studentAnswerLog
        restStudentAnswerLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    protected long getRepositoryCount() {
        return studentAnswerLogRepository.count();
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

    protected StudentAnswerLog getPersistedStudentAnswerLog(StudentAnswerLog studentAnswerLog) {
        return studentAnswerLogRepository.findById(studentAnswerLog.getId()).orElseThrow();
    }

    protected void assertPersistedStudentAnswerLogToMatchAllProperties(StudentAnswerLog expectedStudentAnswerLog) {
        assertStudentAnswerLogAllPropertiesEquals(expectedStudentAnswerLog, getPersistedStudentAnswerLog(expectedStudentAnswerLog));
    }

    protected void assertPersistedStudentAnswerLogToMatchUpdatableProperties(StudentAnswerLog expectedStudentAnswerLog) {
        assertStudentAnswerLogAllUpdatablePropertiesEquals(
            expectedStudentAnswerLog,
            getPersistedStudentAnswerLog(expectedStudentAnswerLog)
        );
    }
}
