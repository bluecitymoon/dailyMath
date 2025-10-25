package com.turling.web.rest;

import com.turling.repository.QuestionOptionRepository;
import com.turling.service.QuestionOptionService;
import com.turling.service.dto.QuestionOptionDTO;
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
 * REST controller for managing {@link com.turling.domain.QuestionOption}.
 */
@RestController
@RequestMapping("/api/question-options")
public class QuestionOptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionOptionResource.class);

    private static final String ENTITY_NAME = "questionOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionOptionService questionOptionService;

    private final QuestionOptionRepository questionOptionRepository;

    public QuestionOptionResource(QuestionOptionService questionOptionService, QuestionOptionRepository questionOptionRepository) {
        this.questionOptionService = questionOptionService;
        this.questionOptionRepository = questionOptionRepository;
    }

    /**
     * {@code POST  /question-options} : Create a new questionOption.
     *
     * @param questionOptionDTO the questionOptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionOptionDTO, or with status {@code 400 (Bad Request)} if the questionOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionOptionDTO> createQuestionOption(@RequestBody QuestionOptionDTO questionOptionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuestionOption : {}", questionOptionDTO);
        if (questionOptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionOptionDTO = questionOptionService.save(questionOptionDTO);
        return ResponseEntity.created(new URI("/api/question-options/" + questionOptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, questionOptionDTO.getId().toString()))
            .body(questionOptionDTO);
    }

    /**
     * {@code PUT  /question-options/:id} : Updates an existing questionOption.
     *
     * @param id the id of the questionOptionDTO to save.
     * @param questionOptionDTO the questionOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionOptionDTO,
     * or with status {@code 400 (Bad Request)} if the questionOptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionOptionDTO> updateQuestionOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionOptionDTO questionOptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuestionOption : {}, {}", id, questionOptionDTO);
        if (questionOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionOptionDTO = questionOptionService.update(questionOptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionOptionDTO.getId().toString()))
            .body(questionOptionDTO);
    }

    /**
     * {@code PATCH  /question-options/:id} : Partial updates given fields of an existing questionOption, field will ignore if it is null
     *
     * @param id the id of the questionOptionDTO to save.
     * @param questionOptionDTO the questionOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionOptionDTO,
     * or with status {@code 400 (Bad Request)} if the questionOptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionOptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionOptionDTO> partialUpdateQuestionOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionOptionDTO questionOptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuestionOption partially : {}, {}", id, questionOptionDTO);
        if (questionOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionOptionDTO> result = questionOptionService.partialUpdate(questionOptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionOptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-options} : get all the questionOptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionOptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuestionOptionDTO>> getAllQuestionOptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of QuestionOptions");
        Page<QuestionOptionDTO> page = questionOptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-options/:id} : get the "id" questionOption.
     *
     * @param id the id of the questionOptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionOptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionOptionDTO> getQuestionOption(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuestionOption : {}", id);
        Optional<QuestionOptionDTO> questionOptionDTO = questionOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionOptionDTO);
    }

    /**
     * {@code DELETE  /question-options/:id} : delete the "id" questionOption.
     *
     * @param id the id of the questionOptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionOption(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuestionOption : {}", id);
        questionOptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
