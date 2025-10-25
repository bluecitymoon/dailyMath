package com.turling.web.rest;

import static com.turling.domain.QuestionAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.Question;
import com.turling.domain.QuestionCategory;
import com.turling.domain.QuestionType;
import com.turling.domain.StudentGrade;
import com.turling.repository.QuestionRepository;
import com.turling.service.QuestionService;
import com.turling.service.dto.QuestionDTO;
import com.turling.service.mapper.QuestionMapper;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    private static final Double DEFAULT_POINTS = 1D;
    private static final Double UPDATED_POINTS = 2D;
    private static final Double SMALLER_POINTS = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SOLUTION = "AAAAAAAAAA";
    private static final String UPDATED_SOLUTION = "BBBBBBBBBB";

    private static final String DEFAULT_SOLUTION_EXTERNAL_LINK = "AAAAAAAAAA";
    private static final String UPDATED_SOLUTION_EXTERNAL_LINK = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_BY_USER_ID = 1L;
    private static final Long UPDATED_CREATE_BY_USER_ID = 2L;
    private static final Long SMALLER_CREATE_BY_USER_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionRepository questionRepository;

    @Mock
    private QuestionRepository questionRepositoryMock;

    @Autowired
    private QuestionMapper questionMapper;

    @Mock
    private QuestionService questionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    private Question insertedQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity() {
        return new Question()
            .points(DEFAULT_POINTS)
            .description(DEFAULT_DESCRIPTION)
            .solution(DEFAULT_SOLUTION)
            .solutionExternalLink(DEFAULT_SOLUTION_EXTERNAL_LINK)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .createBy(DEFAULT_CREATE_BY)
            .createByUserId(DEFAULT_CREATE_BY_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity() {
        return new Question()
            .points(UPDATED_POINTS)
            .description(UPDATED_DESCRIPTION)
            .solution(UPDATED_SOLUTION)
            .solutionExternalLink(UPDATED_SOLUTION_EXTERNAL_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .createByUserId(UPDATED_CREATE_BY_USER_ID);
    }

    @BeforeEach
    void initTest() {
        question = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestion != null) {
            questionRepository.delete(insertedQuestion);
            insertedQuestion = null;
        }
    }

    @Test
    @Transactional
    void createQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        var returnedQuestionDTO = om.readValue(
            restQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionDTO.class
        );

        // Validate the Question in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestion = questionMapper.toEntity(returnedQuestionDTO);
        assertQuestionUpdatableFieldsEquals(returnedQuestion, getPersistedQuestion(returnedQuestion));

        insertedQuestion = returnedQuestion;
    }

    @Test
    @Transactional
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].solution").value(hasItem(DEFAULT_SOLUTION)))
            .andExpect(jsonPath("$.[*].solutionExternalLink").value(hasItem(DEFAULT_SOLUTION_EXTERNAL_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createByUserId").value(hasItem(DEFAULT_CREATE_BY_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(questionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.solution").value(DEFAULT_SOLUTION))
            .andExpect(jsonPath("$.solutionExternalLink").value(DEFAULT_SOLUTION_EXTERNAL_LINK))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createByUserId").value(DEFAULT_CREATE_BY_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getQuestionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        Long id = question.getId();

        defaultQuestionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuestionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuestionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points equals to
        defaultQuestionFiltering("points.equals=" + DEFAULT_POINTS, "points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points in
        defaultQuestionFiltering("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS, "points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is not null
        defaultQuestionFiltering("points.specified=true", "points.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is greater than or equal to
        defaultQuestionFiltering("points.greaterThanOrEqual=" + DEFAULT_POINTS, "points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is less than or equal to
        defaultQuestionFiltering("points.lessThanOrEqual=" + DEFAULT_POINTS, "points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is less than
        defaultQuestionFiltering("points.lessThan=" + UPDATED_POINTS, "points.lessThan=" + DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is greater than
        defaultQuestionFiltering("points.greaterThan=" + SMALLER_POINTS, "points.greaterThan=" + DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where description equals to
        defaultQuestionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where description in
        defaultQuestionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where description is not null
        defaultQuestionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where description contains
        defaultQuestionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where description does not contain
        defaultQuestionFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solution equals to
        defaultQuestionFiltering("solution.equals=" + DEFAULT_SOLUTION, "solution.equals=" + UPDATED_SOLUTION);
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solution in
        defaultQuestionFiltering("solution.in=" + DEFAULT_SOLUTION + "," + UPDATED_SOLUTION, "solution.in=" + UPDATED_SOLUTION);
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solution is not null
        defaultQuestionFiltering("solution.specified=true", "solution.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solution contains
        defaultQuestionFiltering("solution.contains=" + DEFAULT_SOLUTION, "solution.contains=" + UPDATED_SOLUTION);
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solution does not contain
        defaultQuestionFiltering("solution.doesNotContain=" + UPDATED_SOLUTION, "solution.doesNotContain=" + DEFAULT_SOLUTION);
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionExternalLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solutionExternalLink equals to
        defaultQuestionFiltering(
            "solutionExternalLink.equals=" + DEFAULT_SOLUTION_EXTERNAL_LINK,
            "solutionExternalLink.equals=" + UPDATED_SOLUTION_EXTERNAL_LINK
        );
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionExternalLinkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solutionExternalLink in
        defaultQuestionFiltering(
            "solutionExternalLink.in=" + DEFAULT_SOLUTION_EXTERNAL_LINK + "," + UPDATED_SOLUTION_EXTERNAL_LINK,
            "solutionExternalLink.in=" + UPDATED_SOLUTION_EXTERNAL_LINK
        );
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionExternalLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solutionExternalLink is not null
        defaultQuestionFiltering("solutionExternalLink.specified=true", "solutionExternalLink.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionExternalLinkContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solutionExternalLink contains
        defaultQuestionFiltering(
            "solutionExternalLink.contains=" + DEFAULT_SOLUTION_EXTERNAL_LINK,
            "solutionExternalLink.contains=" + UPDATED_SOLUTION_EXTERNAL_LINK
        );
    }

    @Test
    @Transactional
    void getAllQuestionsBySolutionExternalLinkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where solutionExternalLink does not contain
        defaultQuestionFiltering(
            "solutionExternalLink.doesNotContain=" + UPDATED_SOLUTION_EXTERNAL_LINK,
            "solutionExternalLink.doesNotContain=" + DEFAULT_SOLUTION_EXTERNAL_LINK
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate equals to
        defaultQuestionFiltering("createDate.equals=" + DEFAULT_CREATE_DATE, "createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate in
        defaultQuestionFiltering(
            "createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE,
            "createDate.in=" + UPDATED_CREATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate is not null
        defaultQuestionFiltering("createDate.specified=true", "createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByUpdateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where updateDate equals to
        defaultQuestionFiltering("updateDate.equals=" + DEFAULT_UPDATE_DATE, "updateDate.equals=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByUpdateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where updateDate in
        defaultQuestionFiltering(
            "updateDate.in=" + DEFAULT_UPDATE_DATE + "," + UPDATED_UPDATE_DATE,
            "updateDate.in=" + UPDATED_UPDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByUpdateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where updateDate is not null
        defaultQuestionFiltering("updateDate.specified=true", "updateDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createBy equals to
        defaultQuestionFiltering("createBy.equals=" + DEFAULT_CREATE_BY, "createBy.equals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createBy in
        defaultQuestionFiltering("createBy.in=" + DEFAULT_CREATE_BY + "," + UPDATED_CREATE_BY, "createBy.in=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createBy is not null
        defaultQuestionFiltering("createBy.specified=true", "createBy.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createBy contains
        defaultQuestionFiltering("createBy.contains=" + DEFAULT_CREATE_BY, "createBy.contains=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createBy does not contain
        defaultQuestionFiltering("createBy.doesNotContain=" + UPDATED_CREATE_BY, "createBy.doesNotContain=" + DEFAULT_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createByUserId equals to
        defaultQuestionFiltering(
            "createByUserId.equals=" + DEFAULT_CREATE_BY_USER_ID,
            "createByUserId.equals=" + UPDATED_CREATE_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createByUserId in
        defaultQuestionFiltering(
            "createByUserId.in=" + DEFAULT_CREATE_BY_USER_ID + "," + UPDATED_CREATE_BY_USER_ID,
            "createByUserId.in=" + UPDATED_CREATE_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createByUserId is not null
        defaultQuestionFiltering("createByUserId.specified=true", "createByUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createByUserId is greater than or equal to
        defaultQuestionFiltering(
            "createByUserId.greaterThanOrEqual=" + DEFAULT_CREATE_BY_USER_ID,
            "createByUserId.greaterThanOrEqual=" + UPDATED_CREATE_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createByUserId is less than or equal to
        defaultQuestionFiltering(
            "createByUserId.lessThanOrEqual=" + DEFAULT_CREATE_BY_USER_ID,
            "createByUserId.lessThanOrEqual=" + SMALLER_CREATE_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createByUserId is less than
        defaultQuestionFiltering(
            "createByUserId.lessThan=" + UPDATED_CREATE_BY_USER_ID,
            "createByUserId.lessThan=" + DEFAULT_CREATE_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createByUserId is greater than
        defaultQuestionFiltering(
            "createByUserId.greaterThan=" + SMALLER_CREATE_BY_USER_ID,
            "createByUserId.greaterThan=" + DEFAULT_CREATE_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionCategoryIsEqualToSomething() throws Exception {
        QuestionCategory questionCategory;
        if (TestUtil.findAll(em, QuestionCategory.class).isEmpty()) {
            questionRepository.saveAndFlush(question);
            questionCategory = QuestionCategoryResourceIT.createEntity();
        } else {
            questionCategory = TestUtil.findAll(em, QuestionCategory.class).get(0);
        }
        em.persist(questionCategory);
        em.flush();
        question.setQuestionCategory(questionCategory);
        questionRepository.saveAndFlush(question);
        Long questionCategoryId = questionCategory.getId();
        // Get all the questionList where questionCategory equals to questionCategoryId
        defaultQuestionShouldBeFound("questionCategoryId.equals=" + questionCategoryId);

        // Get all the questionList where questionCategory equals to (questionCategoryId + 1)
        defaultQuestionShouldNotBeFound("questionCategoryId.equals=" + (questionCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByTypeIsEqualToSomething() throws Exception {
        QuestionType type;
        if (TestUtil.findAll(em, QuestionType.class).isEmpty()) {
            questionRepository.saveAndFlush(question);
            type = QuestionTypeResourceIT.createEntity();
        } else {
            type = TestUtil.findAll(em, QuestionType.class).get(0);
        }
        em.persist(type);
        em.flush();
        question.setType(type);
        questionRepository.saveAndFlush(question);
        Long typeId = type.getId();
        // Get all the questionList where type equals to typeId
        defaultQuestionShouldBeFound("typeId.equals=" + typeId);

        // Get all the questionList where type equals to (typeId + 1)
        defaultQuestionShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByGradeIsEqualToSomething() throws Exception {
        StudentGrade grade;
        if (TestUtil.findAll(em, StudentGrade.class).isEmpty()) {
            questionRepository.saveAndFlush(question);
            grade = StudentGradeResourceIT.createEntity();
        } else {
            grade = TestUtil.findAll(em, StudentGrade.class).get(0);
        }
        em.persist(grade);
        em.flush();
        question.setGrade(grade);
        questionRepository.saveAndFlush(question);
        Long gradeId = grade.getId();
        // Get all the questionList where grade equals to gradeId
        defaultQuestionShouldBeFound("gradeId.equals=" + gradeId);

        // Get all the questionList where grade equals to (gradeId + 1)
        defaultQuestionShouldNotBeFound("gradeId.equals=" + (gradeId + 1));
    }

    private void defaultQuestionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuestionShouldBeFound(shouldBeFound);
        defaultQuestionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].solution").value(hasItem(DEFAULT_SOLUTION)))
            .andExpect(jsonPath("$.[*].solutionExternalLink").value(hasItem(DEFAULT_SOLUTION_EXTERNAL_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createByUserId").value(hasItem(DEFAULT_CREATE_BY_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .points(UPDATED_POINTS)
            .description(UPDATED_DESCRIPTION)
            .solution(UPDATED_SOLUTION)
            .solutionExternalLink(UPDATED_SOLUTION_EXTERNAL_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .createByUserId(UPDATED_CREATE_BY_USER_ID);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionToMatchAllProperties(updatedQuestion);
    }

    @Test
    @Transactional
    void putNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.points(UPDATED_POINTS).solution(UPDATED_SOLUTION).updateDate(UPDATED_UPDATE_DATE);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuestion, question), getPersistedQuestion(question));
    }

    @Test
    @Transactional
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .points(UPDATED_POINTS)
            .description(UPDATED_DESCRIPTION)
            .solution(UPDATED_SOLUTION)
            .solutionExternalLink(UPDATED_SOLUTION_EXTERNAL_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .createByUserId(UPDATED_CREATE_BY_USER_ID);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(partialUpdatedQuestion, getPersistedQuestion(partialUpdatedQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the question
        restQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, question.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionRepository.count();
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

    protected Question getPersistedQuestion(Question question) {
        return questionRepository.findById(question.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionToMatchAllProperties(Question expectedQuestion) {
        assertQuestionAllPropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }

    protected void assertPersistedQuestionToMatchUpdatableProperties(Question expectedQuestion) {
        assertQuestionAllUpdatablePropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }
}
