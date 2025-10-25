package com.turling.web.rest;

import com.turling.service.StudentAnswerLogQueryService;
import com.turling.service.StudentAnswerLogService;
import com.turling.service.criteria.StudentAnswerLogCriteria;
import com.turling.service.dto.StudentAnswerLogDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.turling.domain.StudentAnswerLog}.
 */
@RestController
@RequestMapping("/api/student-answer-logs")
public class StudentAnswerLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentAnswerLogResource.class);

    private final StudentAnswerLogService studentAnswerLogService;

    private final StudentAnswerLogQueryService studentAnswerLogQueryService;

    public StudentAnswerLogResource(
        StudentAnswerLogService studentAnswerLogService,
        StudentAnswerLogQueryService studentAnswerLogQueryService
    ) {
        this.studentAnswerLogService = studentAnswerLogService;
        this.studentAnswerLogQueryService = studentAnswerLogQueryService;
    }

    /**
     * {@code GET  /student-answer-logs} : get all the studentAnswerLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentAnswerLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudentAnswerLogDTO>> getAllStudentAnswerLogs(
        StudentAnswerLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get StudentAnswerLogs by criteria: {}", criteria);

        Page<StudentAnswerLogDTO> page = studentAnswerLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-answer-logs/count} : count all the studentAnswerLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStudentAnswerLogs(StudentAnswerLogCriteria criteria) {
        LOG.debug("REST request to count StudentAnswerLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(studentAnswerLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /student-answer-logs/:id} : get the "id" studentAnswerLog.
     *
     * @param id the id of the studentAnswerLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentAnswerLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentAnswerLogDTO> getStudentAnswerLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudentAnswerLog : {}", id);
        Optional<StudentAnswerLogDTO> studentAnswerLogDTO = studentAnswerLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentAnswerLogDTO);
    }
}
