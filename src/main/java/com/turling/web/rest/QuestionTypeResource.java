package com.turling.web.rest;

import com.turling.repository.QuestionTypeRepository;
import com.turling.service.QuestionTypeService;
import com.turling.service.dto.QuestionTypeDTO;
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
 * REST controller for managing {@link com.turling.domain.QuestionType}.
 */
@RestController
@RequestMapping("/api/question-types")
public class QuestionTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionTypeResource.class);

    private static final String ENTITY_NAME = "questionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionTypeService questionTypeService;

    private final QuestionTypeRepository questionTypeRepository;

    public QuestionTypeResource(QuestionTypeService questionTypeService, QuestionTypeRepository questionTypeRepository) {
        this.questionTypeService = questionTypeService;
        this.questionTypeRepository = questionTypeRepository;
    }

    /**
     * {@code POST  /question-types} : Create a new questionType.
     *
     * @param questionTypeDTO the questionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionTypeDTO, or with status {@code 400 (Bad Request)} if the questionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionTypeDTO> createQuestionType(@RequestBody QuestionTypeDTO questionTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save QuestionType : {}", questionTypeDTO);
        if (questionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionTypeDTO = questionTypeService.save(questionTypeDTO);
        return ResponseEntity.created(new URI("/api/question-types/" + questionTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, questionTypeDTO.getId().toString()))
            .body(questionTypeDTO);
    }

    /**
     * {@code PUT  /question-types/:id} : Updates an existing questionType.
     *
     * @param id the id of the questionTypeDTO to save.
     * @param questionTypeDTO the questionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the questionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionTypeDTO> updateQuestionType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionTypeDTO questionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuestionType : {}, {}", id, questionTypeDTO);
        if (questionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionTypeDTO = questionTypeService.update(questionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionTypeDTO.getId().toString()))
            .body(questionTypeDTO);
    }

    /**
     * {@code PATCH  /question-types/:id} : Partial updates given fields of an existing questionType, field will ignore if it is null
     *
     * @param id the id of the questionTypeDTO to save.
     * @param questionTypeDTO the questionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the questionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionTypeDTO> partialUpdateQuestionType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionTypeDTO questionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuestionType partially : {}, {}", id, questionTypeDTO);
        if (questionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionTypeDTO> result = questionTypeService.partialUpdate(questionTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-types} : get all the questionTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuestionTypeDTO>> getAllQuestionTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of QuestionTypes");
        Page<QuestionTypeDTO> page = questionTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-types/:id} : get the "id" questionType.
     *
     * @param id the id of the questionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionTypeDTO> getQuestionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuestionType : {}", id);
        Optional<QuestionTypeDTO> questionTypeDTO = questionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionTypeDTO);
    }

    /**
     * {@code DELETE  /question-types/:id} : delete the "id" questionType.
     *
     * @param id the id of the questionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuestionType : {}", id);
        questionTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
