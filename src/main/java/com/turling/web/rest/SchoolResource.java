package com.turling.web.rest;

import com.turling.repository.SchoolRepository;
import com.turling.service.SchoolQueryService;
import com.turling.service.SchoolService;
import com.turling.service.criteria.SchoolCriteria;
import com.turling.service.dto.SchoolDTO;
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
 * REST controller for managing {@link com.turling.domain.School}.
 */
@RestController
@RequestMapping("/api/schools")
public class SchoolResource {

    private static final Logger LOG = LoggerFactory.getLogger(SchoolResource.class);

    private static final String ENTITY_NAME = "school";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolService schoolService;

    private final SchoolRepository schoolRepository;

    private final SchoolQueryService schoolQueryService;

    public SchoolResource(SchoolService schoolService, SchoolRepository schoolRepository, SchoolQueryService schoolQueryService) {
        this.schoolService = schoolService;
        this.schoolRepository = schoolRepository;
        this.schoolQueryService = schoolQueryService;
    }

    /**
     * {@code POST  /schools} : Create a new school.
     *
     * @param schoolDTO the schoolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schoolDTO, or with status {@code 400 (Bad Request)} if the school has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolDTO schoolDTO) throws URISyntaxException {
        LOG.debug("REST request to save School : {}", schoolDTO);
        if (schoolDTO.getId() != null) {
            throw new BadRequestAlertException("A new school cannot already have an ID", ENTITY_NAME, "idexists");
        }
        schoolDTO = schoolService.save(schoolDTO);
        return ResponseEntity.created(new URI("/api/schools/" + schoolDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, schoolDTO.getId().toString()))
            .body(schoolDTO);
    }

    /**
     * {@code PUT  /schools/:id} : Updates an existing school.
     *
     * @param id the id of the schoolDTO to save.
     * @param schoolDTO the schoolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolDTO,
     * or with status {@code 400 (Bad Request)} if the schoolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schoolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SchoolDTO> updateSchool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchoolDTO schoolDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update School : {}, {}", id, schoolDTO);
        if (schoolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        schoolDTO = schoolService.update(schoolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, schoolDTO.getId().toString()))
            .body(schoolDTO);
    }

    /**
     * {@code PATCH  /schools/:id} : Partial updates given fields of an existing school, field will ignore if it is null
     *
     * @param id the id of the schoolDTO to save.
     * @param schoolDTO the schoolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolDTO,
     * or with status {@code 400 (Bad Request)} if the schoolDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schoolDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schoolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SchoolDTO> partialUpdateSchool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchoolDTO schoolDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update School partially : {}, {}", id, schoolDTO);
        if (schoolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchoolDTO> result = schoolService.partialUpdate(schoolDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, schoolDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /schools} : get all the schools.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schools in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SchoolDTO>> getAllSchools(
        SchoolCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Schools by criteria: {}", criteria);

        Page<SchoolDTO> page = schoolQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /schools/count} : count all the schools.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSchools(SchoolCriteria criteria) {
        LOG.debug("REST request to count Schools by criteria: {}", criteria);
        return ResponseEntity.ok().body(schoolQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /schools/:id} : get the "id" school.
     *
     * @param id the id of the schoolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schoolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchoolDTO> getSchool(@PathVariable("id") Long id) {
        LOG.debug("REST request to get School : {}", id);
        Optional<SchoolDTO> schoolDTO = schoolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schoolDTO);
    }

    /**
     * {@code DELETE  /schools/:id} : delete the "id" school.
     *
     * @param id the id of the schoolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete School : {}", id);
        schoolService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
