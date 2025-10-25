package com.turling.web.rest;

import static com.turling.domain.CommunityAsserts.*;
import static com.turling.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.IntegrationTest;
import com.turling.domain.Community;
import com.turling.domain.Distinct;
import com.turling.repository.CommunityRepository;
import com.turling.service.CommunityService;
import com.turling.service.dto.CommunityDTO;
import com.turling.service.mapper.CommunityMapper;
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
 * Integration tests for the {@link CommunityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommunityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;
    private static final Double SMALLER_LON = 1D - 1D;

    private static final Integer DEFAULT_STUDENTS_COUNT = 1;
    private static final Integer UPDATED_STUDENTS_COUNT = 2;
    private static final Integer SMALLER_STUDENTS_COUNT = 1 - 1;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/communities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommunityRepository communityRepository;

    @Mock
    private CommunityRepository communityRepositoryMock;

    @Autowired
    private CommunityMapper communityMapper;

    @Mock
    private CommunityService communityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityMockMvc;

    private Community community;

    private Community insertedCommunity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Community createEntity() {
        return new Community()
            .name(DEFAULT_NAME)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .studentsCount(DEFAULT_STUDENTS_COUNT)
            .createDate(DEFAULT_CREATE_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Community createUpdatedEntity() {
        return new Community()
            .name(UPDATED_NAME)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .studentsCount(UPDATED_STUDENTS_COUNT)
            .createDate(UPDATED_CREATE_DATE);
    }

    @BeforeEach
    void initTest() {
        community = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCommunity != null) {
            communityRepository.delete(insertedCommunity);
            insertedCommunity = null;
        }
    }

    @Test
    @Transactional
    void createCommunity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);
        var returnedCommunityDTO = om.readValue(
            restCommunityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommunityDTO.class
        );

        // Validate the Community in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommunity = communityMapper.toEntity(returnedCommunityDTO);
        assertCommunityUpdatableFieldsEquals(returnedCommunity, getPersistedCommunity(returnedCommunity));

        insertedCommunity = returnedCommunity;
    }

    @Test
    @Transactional
    void createCommunityWithExistingId() throws Exception {
        // Create the Community with an existing ID
        community.setId(1L);
        CommunityDTO communityDTO = communityMapper.toDto(community);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommunities() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT)))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON)))
            .andExpect(jsonPath("$.[*].studentsCount").value(hasItem(DEFAULT_STUDENTS_COUNT)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommunitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(communityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommunityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(communityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommunitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(communityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommunityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(communityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCommunity() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get the community
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL_ID, community.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(community.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON))
            .andExpect(jsonPath("$.studentsCount").value(DEFAULT_STUDENTS_COUNT))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    void getCommunitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        Long id = community.getId();

        defaultCommunityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCommunityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCommunityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where name equals to
        defaultCommunityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where name in
        defaultCommunityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where name is not null
        defaultCommunityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where name contains
        defaultCommunityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where name does not contain
        defaultCommunityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lat equals to
        defaultCommunityFiltering("lat.equals=" + DEFAULT_LAT, "lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lat in
        defaultCommunityFiltering("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT, "lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lat is not null
        defaultCommunityFiltering("lat.specified=true", "lat.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lat is greater than or equal to
        defaultCommunityFiltering("lat.greaterThanOrEqual=" + DEFAULT_LAT, "lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lat is less than or equal to
        defaultCommunityFiltering("lat.lessThanOrEqual=" + DEFAULT_LAT, "lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lat is less than
        defaultCommunityFiltering("lat.lessThan=" + UPDATED_LAT, "lat.lessThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lat is greater than
        defaultCommunityFiltering("lat.greaterThan=" + SMALLER_LAT, "lat.greaterThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lon equals to
        defaultCommunityFiltering("lon.equals=" + DEFAULT_LON, "lon.equals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lon in
        defaultCommunityFiltering("lon.in=" + DEFAULT_LON + "," + UPDATED_LON, "lon.in=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lon is not null
        defaultCommunityFiltering("lon.specified=true", "lon.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByLonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lon is greater than or equal to
        defaultCommunityFiltering("lon.greaterThanOrEqual=" + DEFAULT_LON, "lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lon is less than or equal to
        defaultCommunityFiltering("lon.lessThanOrEqual=" + DEFAULT_LON, "lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLonIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lon is less than
        defaultCommunityFiltering("lon.lessThan=" + UPDATED_LON, "lon.lessThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where lon is greater than
        defaultCommunityFiltering("lon.greaterThan=" + SMALLER_LON, "lon.greaterThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllCommunitiesByStudentsCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where studentsCount equals to
        defaultCommunityFiltering("studentsCount.equals=" + DEFAULT_STUDENTS_COUNT, "studentsCount.equals=" + UPDATED_STUDENTS_COUNT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByStudentsCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where studentsCount in
        defaultCommunityFiltering(
            "studentsCount.in=" + DEFAULT_STUDENTS_COUNT + "," + UPDATED_STUDENTS_COUNT,
            "studentsCount.in=" + UPDATED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCommunitiesByStudentsCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where studentsCount is not null
        defaultCommunityFiltering("studentsCount.specified=true", "studentsCount.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByStudentsCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where studentsCount is greater than or equal to
        defaultCommunityFiltering(
            "studentsCount.greaterThanOrEqual=" + DEFAULT_STUDENTS_COUNT,
            "studentsCount.greaterThanOrEqual=" + UPDATED_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCommunitiesByStudentsCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where studentsCount is less than or equal to
        defaultCommunityFiltering(
            "studentsCount.lessThanOrEqual=" + DEFAULT_STUDENTS_COUNT,
            "studentsCount.lessThanOrEqual=" + SMALLER_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCommunitiesByStudentsCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where studentsCount is less than
        defaultCommunityFiltering("studentsCount.lessThan=" + UPDATED_STUDENTS_COUNT, "studentsCount.lessThan=" + DEFAULT_STUDENTS_COUNT);
    }

    @Test
    @Transactional
    void getAllCommunitiesByStudentsCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where studentsCount is greater than
        defaultCommunityFiltering(
            "studentsCount.greaterThan=" + SMALLER_STUDENTS_COUNT,
            "studentsCount.greaterThan=" + DEFAULT_STUDENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where createDate equals to
        defaultCommunityFiltering("createDate.equals=" + DEFAULT_CREATE_DATE, "createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where createDate in
        defaultCommunityFiltering(
            "createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE,
            "createDate.in=" + UPDATED_CREATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCommunitiesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        // Get all the communityList where createDate is not null
        defaultCommunityFiltering("createDate.specified=true", "createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByDistinctIsEqualToSomething() throws Exception {
        Distinct distinct;
        if (TestUtil.findAll(em, Distinct.class).isEmpty()) {
            communityRepository.saveAndFlush(community);
            distinct = DistinctResourceIT.createEntity();
        } else {
            distinct = TestUtil.findAll(em, Distinct.class).get(0);
        }
        em.persist(distinct);
        em.flush();
        community.setDistinct(distinct);
        communityRepository.saveAndFlush(community);
        Long distinctId = distinct.getId();
        // Get all the communityList where distinct equals to distinctId
        defaultCommunityShouldBeFound("distinctId.equals=" + distinctId);

        // Get all the communityList where distinct equals to (distinctId + 1)
        defaultCommunityShouldNotBeFound("distinctId.equals=" + (distinctId + 1));
    }

    private void defaultCommunityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCommunityShouldBeFound(shouldBeFound);
        defaultCommunityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommunityShouldBeFound(String filter) throws Exception {
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT)))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON)))
            .andExpect(jsonPath("$.[*].studentsCount").value(hasItem(DEFAULT_STUDENTS_COUNT)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));

        // Check, that the count call also returns 1
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommunityShouldNotBeFound(String filter) throws Exception {
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommunity() throws Exception {
        // Get the community
        restCommunityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommunity() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the community
        Community updatedCommunity = communityRepository.findById(community.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommunity are not directly saved in db
        em.detach(updatedCommunity);
        updatedCommunity
            .name(UPDATED_NAME)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .studentsCount(UPDATED_STUDENTS_COUNT)
            .createDate(UPDATED_CREATE_DATE);
        CommunityDTO communityDTO = communityMapper.toDto(updatedCommunity);

        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommunityToMatchAllProperties(updatedCommunity);
    }

    @Test
    @Transactional
    void putNonExistingCommunity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        community.setId(longCount.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        community.setId(longCount.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        community.setId(longCount.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommunityWithPatch() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the community using partial update
        Community partialUpdatedCommunity = new Community();
        partialUpdatedCommunity.setId(community.getId());

        partialUpdatedCommunity.name(UPDATED_NAME).lat(UPDATED_LAT).lon(UPDATED_LON).createDate(UPDATED_CREATE_DATE);

        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunity))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommunity, community),
            getPersistedCommunity(community)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommunityWithPatch() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the community using partial update
        Community partialUpdatedCommunity = new Community();
        partialUpdatedCommunity.setId(community.getId());

        partialUpdatedCommunity
            .name(UPDATED_NAME)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .studentsCount(UPDATED_STUDENTS_COUNT)
            .createDate(UPDATED_CREATE_DATE);

        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunity))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityUpdatableFieldsEquals(partialUpdatedCommunity, getPersistedCommunity(partialUpdatedCommunity));
    }

    @Test
    @Transactional
    void patchNonExistingCommunity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        community.setId(longCount.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        community.setId(longCount.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        community.setId(longCount.incrementAndGet());

        // Create the Community
        CommunityDTO communityDTO = communityMapper.toDto(community);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(communityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Community in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommunity() throws Exception {
        // Initialize the database
        insertedCommunity = communityRepository.saveAndFlush(community);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the community
        restCommunityMockMvc
            .perform(delete(ENTITY_API_URL_ID, community.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return communityRepository.count();
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

    protected Community getPersistedCommunity(Community community) {
        return communityRepository.findById(community.getId()).orElseThrow();
    }

    protected void assertPersistedCommunityToMatchAllProperties(Community expectedCommunity) {
        assertCommunityAllPropertiesEquals(expectedCommunity, getPersistedCommunity(expectedCommunity));
    }

    protected void assertPersistedCommunityToMatchUpdatableProperties(Community expectedCommunity) {
        assertCommunityAllUpdatablePropertiesEquals(expectedCommunity, getPersistedCommunity(expectedCommunity));
    }
}
