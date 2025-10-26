package com.turling.web.rest;

import com.turling.repository.StudentSectionLogRepository;
import com.turling.service.StudentSectionLogQueryService;
import com.turling.service.StudentSectionLogService;
import com.turling.service.criteria.StudentSectionLogCriteria;
import com.turling.service.dto.StudentSectionLogDTO;
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
 * REST controller for managing {@link com.turling.domain.StudentSectionLog}.
 */
@RestController
@RequestMapping("/api/student-section-logs")
public class StudentSectionLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentSectionLogResource.class);

    private static final String ENTITY_NAME = "studentSectionLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentSectionLogService studentSectionLogService;

    private final StudentSectionLogRepository studentSectionLogRepository;

    private final StudentSectionLogQueryService studentSectionLogQueryService;

    public StudentSectionLogResource(
        StudentSectionLogService studentSectionLogService,
        StudentSectionLogRepository studentSectionLogRepository,
        StudentSectionLogQueryService studentSectionLogQueryService
    ) {
        this.studentSectionLogService = studentSectionLogService;
        this.studentSectionLogRepository = studentSectionLogRepository;
        this.studentSectionLogQueryService = studentSectionLogQueryService;
    }

    /**
     * {@code POST  /student-section-logs} : Create a new studentSectionLog.
     *
     * @param studentSectionLogDTO the studentSectionLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentSectionLogDTO, or with status {@code 400 (Bad Request)} if the studentSectionLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentSectionLogDTO> createStudentSectionLog(@RequestBody StudentSectionLogDTO studentSectionLogDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StudentSectionLog : {}", studentSectionLogDTO);
        if (studentSectionLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentSectionLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentSectionLogDTO = studentSectionLogService.save(studentSectionLogDTO);
        return ResponseEntity.created(new URI("/api/student-section-logs/" + studentSectionLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, studentSectionLogDTO.getId().toString()))
            .body(studentSectionLogDTO);
    }

    /**
     * {@code PUT  /student-section-logs/:id} : Updates an existing studentSectionLog.
     *
     * @param id the id of the studentSectionLogDTO to save.
     * @param studentSectionLogDTO the studentSectionLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentSectionLogDTO,
     * or with status {@code 400 (Bad Request)} if the studentSectionLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentSectionLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentSectionLogDTO> updateStudentSectionLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentSectionLogDTO studentSectionLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudentSectionLog : {}, {}", id, studentSectionLogDTO);
        if (studentSectionLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentSectionLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentSectionLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentSectionLogDTO = studentSectionLogService.update(studentSectionLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentSectionLogDTO.getId().toString()))
            .body(studentSectionLogDTO);
    }

    /**
     * {@code PATCH  /student-section-logs/:id} : Partial updates given fields of an existing studentSectionLog, field will ignore if it is null
     *
     * @param id the id of the studentSectionLogDTO to save.
     * @param studentSectionLogDTO the studentSectionLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentSectionLogDTO,
     * or with status {@code 400 (Bad Request)} if the studentSectionLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentSectionLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentSectionLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentSectionLogDTO> partialUpdateStudentSectionLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentSectionLogDTO studentSectionLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudentSectionLog partially : {}, {}", id, studentSectionLogDTO);
        if (studentSectionLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentSectionLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentSectionLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentSectionLogDTO> result = studentSectionLogService.partialUpdate(studentSectionLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentSectionLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-section-logs} : get all the studentSectionLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentSectionLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudentSectionLogDTO>> getAllStudentSectionLogs(
        StudentSectionLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get StudentSectionLogs by criteria: {}", criteria);

        Page<StudentSectionLogDTO> page = studentSectionLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-section-logs/count} : count all the studentSectionLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStudentSectionLogs(StudentSectionLogCriteria criteria) {
        LOG.debug("REST request to count StudentSectionLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(studentSectionLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /student-section-logs/:id} : get the "id" studentSectionLog.
     *
     * @param id the id of the studentSectionLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentSectionLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentSectionLogDTO> getStudentSectionLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudentSectionLog : {}", id);
        Optional<StudentSectionLogDTO> studentSectionLogDTO = studentSectionLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentSectionLogDTO);
    }

    /**
     * {@code DELETE  /student-section-logs/:id} : delete the "id" studentSectionLog.
     *
     * @param id the id of the studentSectionLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentSectionLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudentSectionLog : {}", id);
        studentSectionLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
