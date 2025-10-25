package com.turling.web.rest;

import com.turling.repository.QuestionBaseGroupRepository;
import com.turling.service.QuestionBaseGroupQueryService;
import com.turling.service.QuestionBaseGroupService;
import com.turling.service.criteria.QuestionBaseGroupCriteria;
import com.turling.service.dto.QuestionBaseGroupDTO;
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
 * REST controller for managing {@link com.turling.domain.QuestionBaseGroup}.
 */
@RestController
@RequestMapping("/api/question-base-groups")
public class QuestionBaseGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionBaseGroupResource.class);

    private static final String ENTITY_NAME = "questionBaseGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionBaseGroupService questionBaseGroupService;

    private final QuestionBaseGroupRepository questionBaseGroupRepository;

    private final QuestionBaseGroupQueryService questionBaseGroupQueryService;

    public QuestionBaseGroupResource(
        QuestionBaseGroupService questionBaseGroupService,
        QuestionBaseGroupRepository questionBaseGroupRepository,
        QuestionBaseGroupQueryService questionBaseGroupQueryService
    ) {
        this.questionBaseGroupService = questionBaseGroupService;
        this.questionBaseGroupRepository = questionBaseGroupRepository;
        this.questionBaseGroupQueryService = questionBaseGroupQueryService;
    }

    /**
     * {@code POST  /question-base-groups} : Create a new questionBaseGroup.
     *
     * @param questionBaseGroupDTO the questionBaseGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionBaseGroupDTO, or with status {@code 400 (Bad Request)} if the questionBaseGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionBaseGroupDTO> createQuestionBaseGroup(@RequestBody QuestionBaseGroupDTO questionBaseGroupDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuestionBaseGroup : {}", questionBaseGroupDTO);
        if (questionBaseGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionBaseGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionBaseGroupDTO = questionBaseGroupService.save(questionBaseGroupDTO);
        return ResponseEntity.created(new URI("/api/question-base-groups/" + questionBaseGroupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, questionBaseGroupDTO.getId().toString()))
            .body(questionBaseGroupDTO);
    }

    /**
     * {@code PUT  /question-base-groups/:id} : Updates an existing questionBaseGroup.
     *
     * @param id the id of the questionBaseGroupDTO to save.
     * @param questionBaseGroupDTO the questionBaseGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionBaseGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionBaseGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionBaseGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionBaseGroupDTO> updateQuestionBaseGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionBaseGroupDTO questionBaseGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuestionBaseGroup : {}, {}", id, questionBaseGroupDTO);
        if (questionBaseGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionBaseGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionBaseGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionBaseGroupDTO = questionBaseGroupService.update(questionBaseGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionBaseGroupDTO.getId().toString()))
            .body(questionBaseGroupDTO);
    }

    /**
     * {@code PATCH  /question-base-groups/:id} : Partial updates given fields of an existing questionBaseGroup, field will ignore if it is null
     *
     * @param id the id of the questionBaseGroupDTO to save.
     * @param questionBaseGroupDTO the questionBaseGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionBaseGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionBaseGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionBaseGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionBaseGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionBaseGroupDTO> partialUpdateQuestionBaseGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionBaseGroupDTO questionBaseGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuestionBaseGroup partially : {}, {}", id, questionBaseGroupDTO);
        if (questionBaseGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionBaseGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionBaseGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionBaseGroupDTO> result = questionBaseGroupService.partialUpdate(questionBaseGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionBaseGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-base-groups} : get all the questionBaseGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionBaseGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuestionBaseGroupDTO>> getAllQuestionBaseGroups(
        QuestionBaseGroupCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuestionBaseGroups by criteria: {}", criteria);

        Page<QuestionBaseGroupDTO> page = questionBaseGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-base-groups/count} : count all the questionBaseGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuestionBaseGroups(QuestionBaseGroupCriteria criteria) {
        LOG.debug("REST request to count QuestionBaseGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(questionBaseGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /question-base-groups/:id} : get the "id" questionBaseGroup.
     *
     * @param id the id of the questionBaseGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionBaseGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionBaseGroupDTO> getQuestionBaseGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuestionBaseGroup : {}", id);
        Optional<QuestionBaseGroupDTO> questionBaseGroupDTO = questionBaseGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionBaseGroupDTO);
    }

    /**
     * {@code DELETE  /question-base-groups/:id} : delete the "id" questionBaseGroup.
     *
     * @param id the id of the questionBaseGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionBaseGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuestionBaseGroup : {}", id);
        questionBaseGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
