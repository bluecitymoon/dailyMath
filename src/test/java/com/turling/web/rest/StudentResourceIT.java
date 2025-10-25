package com.turling.web.rest;

import static com.turling.domain.StudentAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.Community;
import com.turling.domain.School;
import com.turling.domain.Student;
import com.turling.repository.StudentRepository;
import com.turling.service.StudentService;
import com.turling.service.dto.StudentDTO;
import com.turling.service.mapper.StudentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StudentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REGISTER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LATEST_CONTRACT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LATEST_CONTRACT_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PARENTS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENTS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudentRepository studentRepository;

    @Mock
    private StudentRepository studentRepositoryMock;

    @Autowired
    private StudentMapper studentMapper;

    @Mock
    private StudentService studentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    private Student insertedStudent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity() {
        return new Student()
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .birthday(DEFAULT_BIRTHDAY)
            .registerDate(DEFAULT_REGISTER_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .latestContractEndDate(DEFAULT_LATEST_CONTRACT_END_DATE)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .parentsName(DEFAULT_PARENTS_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity() {
        return new Student()
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .birthday(UPDATED_BIRTHDAY)
            .registerDate(UPDATED_REGISTER_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .latestContractEndDate(UPDATED_LATEST_CONTRACT_END_DATE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .parentsName(UPDATED_PARENTS_NAME);
    }

    @BeforeEach
    void initTest() {
        student = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStudent != null) {
            studentRepository.delete(insertedStudent);
            insertedStudent = null;
        }
    }

    @Test
    @Transactional
    void createStudent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);
        var returnedStudentDTO = om.readValue(
            restStudentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudentDTO.class
        );

        // Validate the Student in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStudent = studentMapper.toEntity(returnedStudentDTO);
        assertStudentUpdatableFieldsEquals(returnedStudent, getPersistedStudent(returnedStudent));

        insertedStudent = returnedStudent;
    }

    @Test
    @Transactional
    void createStudentWithExistingId() throws Exception {
        // Create the Student with an existing ID
        student.setId(1L);
        StudentDTO studentDTO = studentMapper.toDto(student);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudents() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].latestContractEndDate").value(hasItem(DEFAULT_LATEST_CONTRACT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].parentsName").value(hasItem(DEFAULT_PARENTS_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(studentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(studentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(studentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(studentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStudent() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc
            .perform(get(ENTITY_API_URL_ID, student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.registerDate").value(DEFAULT_REGISTER_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.latestContractEndDate").value(DEFAULT_LATEST_CONTRACT_END_DATE.toString()))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.parentsName").value(DEFAULT_PARENTS_NAME));
    }

    @Test
    @Transactional
    void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStudentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStudentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where name equals to
        defaultStudentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where name in
        defaultStudentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where name is not null
        defaultStudentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where name contains
        defaultStudentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where name does not contain
        defaultStudentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where gender equals to
        defaultStudentFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStudentsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where gender in
        defaultStudentFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStudentsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where gender is not null
        defaultStudentFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByGenderContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where gender contains
        defaultStudentFiltering("gender.contains=" + DEFAULT_GENDER, "gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStudentsByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where gender does not contain
        defaultStudentFiltering("gender.doesNotContain=" + UPDATED_GENDER, "gender.doesNotContain=" + DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void getAllStudentsByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday equals to
        defaultStudentFiltering("birthday.equals=" + DEFAULT_BIRTHDAY, "birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday in
        defaultStudentFiltering("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY, "birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday is not null
        defaultStudentFiltering("birthday.specified=true", "birthday.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByRegisterDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where registerDate equals to
        defaultStudentFiltering("registerDate.equals=" + DEFAULT_REGISTER_DATE, "registerDate.equals=" + UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByRegisterDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where registerDate in
        defaultStudentFiltering(
            "registerDate.in=" + DEFAULT_REGISTER_DATE + "," + UPDATED_REGISTER_DATE,
            "registerDate.in=" + UPDATED_REGISTER_DATE
        );
    }

    @Test
    @Transactional
    void getAllStudentsByRegisterDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where registerDate is not null
        defaultStudentFiltering("registerDate.specified=true", "registerDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByUpdateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where updateDate equals to
        defaultStudentFiltering("updateDate.equals=" + DEFAULT_UPDATE_DATE, "updateDate.equals=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByUpdateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where updateDate in
        defaultStudentFiltering("updateDate.in=" + DEFAULT_UPDATE_DATE + "," + UPDATED_UPDATE_DATE, "updateDate.in=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByUpdateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where updateDate is not null
        defaultStudentFiltering("updateDate.specified=true", "updateDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByLatestContractEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where latestContractEndDate equals to
        defaultStudentFiltering(
            "latestContractEndDate.equals=" + DEFAULT_LATEST_CONTRACT_END_DATE,
            "latestContractEndDate.equals=" + UPDATED_LATEST_CONTRACT_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllStudentsByLatestContractEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where latestContractEndDate in
        defaultStudentFiltering(
            "latestContractEndDate.in=" + DEFAULT_LATEST_CONTRACT_END_DATE + "," + UPDATED_LATEST_CONTRACT_END_DATE,
            "latestContractEndDate.in=" + UPDATED_LATEST_CONTRACT_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllStudentsByLatestContractEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where latestContractEndDate is not null
        defaultStudentFiltering("latestContractEndDate.specified=true", "latestContractEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where contactNumber equals to
        defaultStudentFiltering("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER, "contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllStudentsByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where contactNumber in
        defaultStudentFiltering(
            "contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER,
            "contactNumber.in=" + UPDATED_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllStudentsByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where contactNumber is not null
        defaultStudentFiltering("contactNumber.specified=true", "contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where contactNumber contains
        defaultStudentFiltering("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER, "contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllStudentsByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where contactNumber does not contain
        defaultStudentFiltering(
            "contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER,
            "contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllStudentsByParentsNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where parentsName equals to
        defaultStudentFiltering("parentsName.equals=" + DEFAULT_PARENTS_NAME, "parentsName.equals=" + UPDATED_PARENTS_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByParentsNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where parentsName in
        defaultStudentFiltering(
            "parentsName.in=" + DEFAULT_PARENTS_NAME + "," + UPDATED_PARENTS_NAME,
            "parentsName.in=" + UPDATED_PARENTS_NAME
        );
    }

    @Test
    @Transactional
    void getAllStudentsByParentsNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where parentsName is not null
        defaultStudentFiltering("parentsName.specified=true", "parentsName.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByParentsNameContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where parentsName contains
        defaultStudentFiltering("parentsName.contains=" + DEFAULT_PARENTS_NAME, "parentsName.contains=" + UPDATED_PARENTS_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByParentsNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        // Get all the studentList where parentsName does not contain
        defaultStudentFiltering("parentsName.doesNotContain=" + UPDATED_PARENTS_NAME, "parentsName.doesNotContain=" + DEFAULT_PARENTS_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsBySchoolIsEqualToSomething() throws Exception {
        School school;
        if (TestUtil.findAll(em, School.class).isEmpty()) {
            studentRepository.saveAndFlush(student);
            school = SchoolResourceIT.createEntity();
        } else {
            school = TestUtil.findAll(em, School.class).get(0);
        }
        em.persist(school);
        em.flush();
        student.setSchool(school);
        studentRepository.saveAndFlush(student);
        Long schoolId = school.getId();
        // Get all the studentList where school equals to schoolId
        defaultStudentShouldBeFound("schoolId.equals=" + schoolId);

        // Get all the studentList where school equals to (schoolId + 1)
        defaultStudentShouldNotBeFound("schoolId.equals=" + (schoolId + 1));
    }

    @Test
    @Transactional
    void getAllStudentsByCommunityIsEqualToSomething() throws Exception {
        Community community;
        if (TestUtil.findAll(em, Community.class).isEmpty()) {
            studentRepository.saveAndFlush(student);
            community = CommunityResourceIT.createEntity();
        } else {
            community = TestUtil.findAll(em, Community.class).get(0);
        }
        em.persist(community);
        em.flush();
        student.setCommunity(community);
        studentRepository.saveAndFlush(student);
        Long communityId = community.getId();
        // Get all the studentList where community equals to communityId
        defaultStudentShouldBeFound("communityId.equals=" + communityId);

        // Get all the studentList where community equals to (communityId + 1)
        defaultStudentShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    private void defaultStudentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStudentShouldBeFound(shouldBeFound);
        defaultStudentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].latestContractEndDate").value(hasItem(DEFAULT_LATEST_CONTRACT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].parentsName").value(hasItem(DEFAULT_PARENTS_NAME)));

        // Check, that the count call also returns 1
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudent() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .birthday(UPDATED_BIRTHDAY)
            .registerDate(UPDATED_REGISTER_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .latestContractEndDate(UPDATED_LATEST_CONTRACT_END_DATE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .parentsName(UPDATED_PARENTS_NAME);
        StudentDTO studentDTO = studentMapper.toDto(updatedStudent);

        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudentToMatchAllProperties(updatedStudent);
    }

    @Test
    @Transactional
    void putNonExistingStudent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        student.setId(longCount.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        student.setId(longCount.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        student.setId(longCount.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .birthday(UPDATED_BIRTHDAY)
            .updateDate(UPDATED_UPDATE_DATE)
            .latestContractEndDate(UPDATED_LATEST_CONTRACT_END_DATE)
            .parentsName(UPDATED_PARENTS_NAME);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStudent, student), getPersistedStudent(student));
    }

    @Test
    @Transactional
    void fullUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .birthday(UPDATED_BIRTHDAY)
            .registerDate(UPDATED_REGISTER_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .latestContractEndDate(UPDATED_LATEST_CONTRACT_END_DATE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .parentsName(UPDATED_PARENTS_NAME);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentUpdatableFieldsEquals(partialUpdatedStudent, getPersistedStudent(partialUpdatedStudent));
    }

    @Test
    @Transactional
    void patchNonExistingStudent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        student.setId(longCount.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        student.setId(longCount.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        student.setId(longCount.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudent() throws Exception {
        // Initialize the database
        insertedStudent = studentRepository.saveAndFlush(student);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the student
        restStudentMockMvc
            .perform(delete(ENTITY_API_URL_ID, student.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studentRepository.count();
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

    protected Student getPersistedStudent(Student student) {
        return studentRepository.findById(student.getId()).orElseThrow();
    }

    protected void assertPersistedStudentToMatchAllProperties(Student expectedStudent) {
        assertStudentAllPropertiesEquals(expectedStudent, getPersistedStudent(expectedStudent));
    }

    protected void assertPersistedStudentToMatchUpdatableProperties(Student expectedStudent) {
        assertStudentAllUpdatablePropertiesEquals(expectedStudent, getPersistedStudent(expectedStudent));
    }
}
