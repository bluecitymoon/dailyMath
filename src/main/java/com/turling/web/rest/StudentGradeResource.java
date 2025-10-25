package com.turling.web.rest;

import com.turling.repository.StudentGradeRepository;
import com.turling.service.StudentGradeService;
import com.turling.service.dto.StudentGradeDTO;
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
 * REST controller for managing {@link com.turling.domain.StudentGrade}.
 */
@RestController
@RequestMapping("/api/student-grades")
public class StudentGradeResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentGradeResource.class);

    private static final String ENTITY_NAME = "studentGrade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentGradeService studentGradeService;

    private final StudentGradeRepository studentGradeRepository;

    public StudentGradeResource(StudentGradeService studentGradeService, StudentGradeRepository studentGradeRepository) {
        this.studentGradeService = studentGradeService;
        this.studentGradeRepository = studentGradeRepository;
    }

    /**
     * {@code POST  /student-grades} : Create a new studentGrade.
     *
     * @param studentGradeDTO the studentGradeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentGradeDTO, or with status {@code 400 (Bad Request)} if the studentGrade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentGradeDTO> createStudentGrade(@RequestBody StudentGradeDTO studentGradeDTO) throws URISyntaxException {
        LOG.debug("REST request to save StudentGrade : {}", studentGradeDTO);
        if (studentGradeDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentGrade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentGradeDTO = studentGradeService.save(studentGradeDTO);
        return ResponseEntity.created(new URI("/api/student-grades/" + studentGradeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, studentGradeDTO.getId().toString()))
            .body(studentGradeDTO);
    }

    /**
     * {@code PUT  /student-grades/:id} : Updates an existing studentGrade.
     *
     * @param id the id of the studentGradeDTO to save.
     * @param studentGradeDTO the studentGradeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentGradeDTO,
     * or with status {@code 400 (Bad Request)} if the studentGradeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentGradeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentGradeDTO> updateStudentGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentGradeDTO studentGradeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudentGrade : {}, {}", id, studentGradeDTO);
        if (studentGradeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentGradeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentGradeDTO = studentGradeService.update(studentGradeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentGradeDTO.getId().toString()))
            .body(studentGradeDTO);
    }

    /**
     * {@code PATCH  /student-grades/:id} : Partial updates given fields of an existing studentGrade, field will ignore if it is null
     *
     * @param id the id of the studentGradeDTO to save.
     * @param studentGradeDTO the studentGradeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentGradeDTO,
     * or with status {@code 400 (Bad Request)} if the studentGradeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentGradeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentGradeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentGradeDTO> partialUpdateStudentGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentGradeDTO studentGradeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudentGrade partially : {}, {}", id, studentGradeDTO);
        if (studentGradeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentGradeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentGradeDTO> result = studentGradeService.partialUpdate(studentGradeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentGradeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-grades} : get all the studentGrades.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentGrades in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudentGradeDTO>> getAllStudentGrades(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of StudentGrades");
        Page<StudentGradeDTO> page = studentGradeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-grades/:id} : get the "id" studentGrade.
     *
     * @param id the id of the studentGradeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentGradeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentGradeDTO> getStudentGrade(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudentGrade : {}", id);
        Optional<StudentGradeDTO> studentGradeDTO = studentGradeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentGradeDTO);
    }

    /**
     * {@code DELETE  /student-grades/:id} : delete the "id" studentGrade.
     *
     * @param id the id of the studentGradeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentGrade(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudentGrade : {}", id);
        studentGradeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
