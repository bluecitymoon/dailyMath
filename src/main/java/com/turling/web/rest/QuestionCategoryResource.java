package com.turling.web.rest;

import com.turling.repository.QuestionCategoryRepository;
import com.turling.service.QuestionCategoryQueryService;
import com.turling.service.QuestionCategoryService;
import com.turling.service.criteria.QuestionCategoryCriteria;
import com.turling.service.dto.QuestionCategoryDTO;
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
 * REST controller for managing {@link com.turling.domain.QuestionCategory}.
 */
@RestController
@RequestMapping("/api/question-categories")
public class QuestionCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionCategoryResource.class);

    private static final String ENTITY_NAME = "questionCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionCategoryService questionCategoryService;

    private final QuestionCategoryRepository questionCategoryRepository;

    private final QuestionCategoryQueryService questionCategoryQueryService;

    public QuestionCategoryResource(
        QuestionCategoryService questionCategoryService,
        QuestionCategoryRepository questionCategoryRepository,
        QuestionCategoryQueryService questionCategoryQueryService
    ) {
        this.questionCategoryService = questionCategoryService;
        this.questionCategoryRepository = questionCategoryRepository;
        this.questionCategoryQueryService = questionCategoryQueryService;
    }

    /**
     * {@code POST  /question-categories} : Create a new questionCategory.
     *
     * @param questionCategoryDTO the questionCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionCategoryDTO, or with status {@code 400 (Bad Request)} if the questionCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionCategoryDTO> createQuestionCategory(@RequestBody QuestionCategoryDTO questionCategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuestionCategory : {}", questionCategoryDTO);
        if (questionCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionCategoryDTO = questionCategoryService.save(questionCategoryDTO);
        return ResponseEntity.created(new URI("/api/question-categories/" + questionCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, questionCategoryDTO.getId().toString()))
            .body(questionCategoryDTO);
    }

    /**
     * {@code PUT  /question-categories/:id} : Updates an existing questionCategory.
     *
     * @param id the id of the questionCategoryDTO to save.
     * @param questionCategoryDTO the questionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the questionCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionCategoryDTO> updateQuestionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionCategoryDTO questionCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuestionCategory : {}, {}", id, questionCategoryDTO);
        if (questionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionCategoryDTO = questionCategoryService.update(questionCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionCategoryDTO.getId().toString()))
            .body(questionCategoryDTO);
    }

    /**
     * {@code PATCH  /question-categories/:id} : Partial updates given fields of an existing questionCategory, field will ignore if it is null
     *
     * @param id the id of the questionCategoryDTO to save.
     * @param questionCategoryDTO the questionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the questionCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionCategoryDTO> partialUpdateQuestionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionCategoryDTO questionCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuestionCategory partially : {}, {}", id, questionCategoryDTO);
        if (questionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionCategoryDTO> result = questionCategoryService.partialUpdate(questionCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-categories} : get all the questionCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuestionCategoryDTO>> getAllQuestionCategories(
        QuestionCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuestionCategories by criteria: {}", criteria);

        Page<QuestionCategoryDTO> page = questionCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-categories/count} : count all the questionCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuestionCategories(QuestionCategoryCriteria criteria) {
        LOG.debug("REST request to count QuestionCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(questionCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /question-categories/:id} : get the "id" questionCategory.
     *
     * @param id the id of the questionCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionCategoryDTO> getQuestionCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuestionCategory : {}", id);
        Optional<QuestionCategoryDTO> questionCategoryDTO = questionCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionCategoryDTO);
    }

    /**
     * {@code DELETE  /question-categories/:id} : delete the "id" questionCategory.
     *
     * @param id the id of the questionCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuestionCategory : {}", id);
        questionCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
