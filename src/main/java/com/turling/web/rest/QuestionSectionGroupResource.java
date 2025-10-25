package com.turling.web.rest;

import com.turling.repository.QuestionSectionGroupRepository;
import com.turling.service.QuestionSectionGroupQueryService;
import com.turling.service.QuestionSectionGroupService;
import com.turling.service.criteria.QuestionSectionGroupCriteria;
import com.turling.service.dto.GradeSectionGroupResponse;
import com.turling.service.dto.QuestionSectionGroupDTO;
import com.turling.service.dto.SectionGroupQuestionsResponse;
import com.turling.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.turling.domain.QuestionSectionGroup}.
 */
@RestController
@RequestMapping("/api/question-section-groups")
public class QuestionSectionGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionSectionGroupResource.class);

    private static final String ENTITY_NAME = "questionSectionGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionSectionGroupService questionSectionGroupService;

    private final QuestionSectionGroupRepository questionSectionGroupRepository;

    private final QuestionSectionGroupQueryService questionSectionGroupQueryService;

    public QuestionSectionGroupResource(
        QuestionSectionGroupService questionSectionGroupService,
        QuestionSectionGroupRepository questionSectionGroupRepository,
        QuestionSectionGroupQueryService questionSectionGroupQueryService
    ) {
        this.questionSectionGroupService = questionSectionGroupService;
        this.questionSectionGroupRepository = questionSectionGroupRepository;
        this.questionSectionGroupQueryService = questionSectionGroupQueryService;
    }

    /**
     * {@code POST  /question-section-groups} : Create a new questionSectionGroup.
     *
     * @param questionSectionGroupDTO the questionSectionGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionSectionGroupDTO, or with status {@code 400 (Bad Request)} if the questionSectionGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionSectionGroupDTO> createQuestionSectionGroup(@RequestBody QuestionSectionGroupDTO questionSectionGroupDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuestionSectionGroup : {}", questionSectionGroupDTO);
        if (questionSectionGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionSectionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionSectionGroupDTO = questionSectionGroupService.save(questionSectionGroupDTO);
        return ResponseEntity.created(new URI("/api/question-section-groups/" + questionSectionGroupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, questionSectionGroupDTO.getId().toString()))
            .body(questionSectionGroupDTO);
    }

    /**
     * {@code PUT  /question-section-groups/:id} : Updates an existing questionSectionGroup.
     *
     * @param id the id of the questionSectionGroupDTO to save.
     * @param questionSectionGroupDTO the questionSectionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionSectionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionSectionGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionSectionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionSectionGroupDTO> updateQuestionSectionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionSectionGroupDTO questionSectionGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuestionSectionGroup : {}, {}", id, questionSectionGroupDTO);
        if (questionSectionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionSectionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionSectionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionSectionGroupDTO = questionSectionGroupService.update(questionSectionGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionSectionGroupDTO.getId().toString()))
            .body(questionSectionGroupDTO);
    }

    /**
     * {@code PATCH  /question-section-groups/:id} : Partial updates given fields of an existing questionSectionGroup, field will ignore if it is null
     *
     * @param id the id of the questionSectionGroupDTO to save.
     * @param questionSectionGroupDTO the questionSectionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionSectionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionSectionGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionSectionGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionSectionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionSectionGroupDTO> partialUpdateQuestionSectionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionSectionGroupDTO questionSectionGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuestionSectionGroup partially : {}, {}", id, questionSectionGroupDTO);
        if (questionSectionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionSectionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionSectionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionSectionGroupDTO> result = questionSectionGroupService.partialUpdate(questionSectionGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionSectionGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-section-groups} : get all the questionSectionGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionSectionGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuestionSectionGroupDTO>> getAllQuestionSectionGroups(
        QuestionSectionGroupCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuestionSectionGroups by criteria: {}", criteria);

        Page<QuestionSectionGroupDTO> page = questionSectionGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-section-groups/count} : count all the questionSectionGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuestionSectionGroups(QuestionSectionGroupCriteria criteria) {
        LOG.debug("REST request to count QuestionSectionGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(questionSectionGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /question-section-groups/:id} : get the "id" questionSectionGroup.
     *
     * @param id the id of the questionSectionGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionSectionGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionSectionGroupDTO> getQuestionSectionGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuestionSectionGroup : {}", id);
        Optional<QuestionSectionGroupDTO> questionSectionGroupDTO = questionSectionGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionSectionGroupDTO);
    }

    /**
     * {@code DELETE  /question-section-groups/:id} : delete the "id" questionSectionGroup.
     *
     * @param id the id of the questionSectionGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionSectionGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuestionSectionGroup : {}", id);
        questionSectionGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /question-section-groups/update-order} : Update the display order of question section groups.
     *
     * @param request the request containing ordered IDs and pagination info.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if the order was updated successfully.
     */
    @PutMapping("/update-order")
    public ResponseEntity<Void> updateQuestionSectionGroupOrder(@RequestBody UpdateOrderRequest request) {
        LOG.debug("REST request to update QuestionSectionGroup order : {}", request);
        questionSectionGroupService.updateDisplayOrderForPage(request.getOrderedIds(), request.getPage(), request.getSize());
        return ResponseEntity.ok().build();
    }

    /**
     * {@code GET  /question-section-groups/by-grade/:gradeId} : get all question section groups by grade.
     *
     * @param gradeId the grade ID to filter by.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of section groups in body.
     */
    @GetMapping("/by-grade/{gradeId}")
    public ResponseEntity<GradeSectionGroupResponse> getQuestionSectionGroupsByGrade(@PathVariable("gradeId") Long gradeId) {
        LOG.debug("REST request to get QuestionSectionGroups by grade : {}", gradeId);

        List<QuestionSectionGroupDTO> sectionGroups = questionSectionGroupService.findByGradeId(gradeId);

        // Create response with grade information
        GradeSectionGroupResponse response = new GradeSectionGroupResponse();
        response.setSectionGroups(sectionGroups);
        response.setTotalCount(sectionGroups.size());

        return ResponseEntity.ok().body(response);
    }

    /**
     * {@code GET  /question-section-groups/by-grade/:gradeId/paged} : get all question section groups by grade with pagination.
     *
     * @param gradeId the grade ID to filter by.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the page of section groups in body.
     */
    @GetMapping("/by-grade/{gradeId}/paged")
    public ResponseEntity<Page<QuestionSectionGroupDTO>> getQuestionSectionGroupsByGradePaged(
        @PathVariable("gradeId") Long gradeId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuestionSectionGroups by grade with pagination : {}, {}", gradeId, pageable);

        Page<QuestionSectionGroupDTO> page = questionSectionGroupService.findByGradeId(gradeId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    /**
     * {@code GET  /question-section-groups/{sectionGroupId}/questions} : get all questions with base group information for a section group.
     *
     * @param sectionGroupId the section group ID to get questions for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the questions with base group info in body.
     */
    @GetMapping("questions-list/{sectionGroupId}")
    public ResponseEntity<SectionGroupQuestionsResponse> getQuestionsBySectionGroupId(@PathVariable("sectionGroupId") Long sectionGroupId) {
        LOG.debug("REST request to get questions by section group ID : {}", sectionGroupId);

        SectionGroupQuestionsResponse response = questionSectionGroupService.getQuestionsBySectionGroupId(sectionGroupId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * {@code GET  /question-section-groups/{sectionGroupId}/questions/paged} : get all questions with base group information for a section group with pagination.
     *
     * @param sectionGroupId the section group ID to get questions for.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the questions with base group info in body.
     */
    @GetMapping("/{sectionGroupId}/questions/paged")
    public ResponseEntity<SectionGroupQuestionsResponse> getQuestionsBySectionGroupIdPaged(
        @PathVariable("sectionGroupId") Long sectionGroupId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get questions by section group ID with pagination : {}, {}", sectionGroupId, pageable);

        SectionGroupQuestionsResponse response = questionSectionGroupService.getQuestionsBySectionGroupId(sectionGroupId, pageable);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Request DTO for updating display order
     */
    public static class UpdateOrderRequest {

        private List<Long> orderedIds;
        private int page;
        private int size;

        public List<Long> getOrderedIds() {
            return orderedIds;
        }

        public void setOrderedIds(List<Long> orderedIds) {
            this.orderedIds = orderedIds;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
