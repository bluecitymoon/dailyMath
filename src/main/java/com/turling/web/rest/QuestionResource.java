package com.turling.web.rest;

import com.turling.repository.QuestionRepository;
import com.turling.service.QuestionQueryService;
import com.turling.service.QuestionService;
import com.turling.service.criteria.QuestionCriteria;
import com.turling.service.dto.QuestionDTO;
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
 * REST controller for managing {@link com.turling.domain.Question}.
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionService questionService;

    private final QuestionRepository questionRepository;

    private final QuestionQueryService questionQueryService;

    public QuestionResource(
        QuestionService questionService,
        QuestionRepository questionRepository,
        QuestionQueryService questionQueryService
    ) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.questionQueryService = questionQueryService;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param questionDTO the questionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionDTO, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionDTO = questionService.save(questionDTO);
        return ResponseEntity.created(new URI("/api/questions/" + questionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, questionDTO.getId().toString()))
            .body(questionDTO);
    }

    /**
     * {@code PUT  /questions/:id} : Updates an existing question.
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Question : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionDTO = questionService.update(questionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionDTO.getId().toString()))
            .body(questionDTO);
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing question, field will ignore if it is null
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionDTO> partialUpdateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Question partially : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionDTO> result = questionService.partialUpdate(questionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(
        QuestionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Questions by criteria: {}", criteria);

        Page<QuestionDTO> page = questionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /questions/count} : count all the questions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuestions(QuestionCriteria criteria) {
        LOG.debug("REST request to count Questions by criteria: {}", criteria);
        return ResponseEntity.ok().body(questionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Question : {}", id);
        Optional<QuestionDTO> questionDTO = questionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionDTO);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Question : {}", id);
        questionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
