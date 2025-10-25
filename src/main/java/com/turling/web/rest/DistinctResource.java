package com.turling.web.rest;

import com.turling.repository.DistinctRepository;
import com.turling.service.DistinctQueryService;
import com.turling.service.DistinctService;
import com.turling.service.criteria.DistinctCriteria;
import com.turling.service.dto.DistinctDTO;
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
 * REST controller for managing {@link com.turling.domain.Distinct}.
 */
@RestController
@RequestMapping("/api/distincts")
public class DistinctResource {

    private static final Logger LOG = LoggerFactory.getLogger(DistinctResource.class);

    private static final String ENTITY_NAME = "distinct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistinctService distinctService;

    private final DistinctRepository distinctRepository;

    private final DistinctQueryService distinctQueryService;

    public DistinctResource(
        DistinctService distinctService,
        DistinctRepository distinctRepository,
        DistinctQueryService distinctQueryService
    ) {
        this.distinctService = distinctService;
        this.distinctRepository = distinctRepository;
        this.distinctQueryService = distinctQueryService;
    }

    /**
     * {@code POST  /distincts} : Create a new distinct.
     *
     * @param distinctDTO the distinctDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new distinctDTO, or with status {@code 400 (Bad Request)} if the distinct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DistinctDTO> createDistinct(@RequestBody DistinctDTO distinctDTO) throws URISyntaxException {
        LOG.debug("REST request to save Distinct : {}", distinctDTO);
        if (distinctDTO.getId() != null) {
            throw new BadRequestAlertException("A new distinct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        distinctDTO = distinctService.save(distinctDTO);
        return ResponseEntity.created(new URI("/api/distincts/" + distinctDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, distinctDTO.getId().toString()))
            .body(distinctDTO);
    }

    /**
     * {@code PUT  /distincts/:id} : Updates an existing distinct.
     *
     * @param id the id of the distinctDTO to save.
     * @param distinctDTO the distinctDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distinctDTO,
     * or with status {@code 400 (Bad Request)} if the distinctDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the distinctDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DistinctDTO> updateDistinct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DistinctDTO distinctDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Distinct : {}, {}", id, distinctDTO);
        if (distinctDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distinctDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distinctRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        distinctDTO = distinctService.update(distinctDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, distinctDTO.getId().toString()))
            .body(distinctDTO);
    }

    /**
     * {@code PATCH  /distincts/:id} : Partial updates given fields of an existing distinct, field will ignore if it is null
     *
     * @param id the id of the distinctDTO to save.
     * @param distinctDTO the distinctDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distinctDTO,
     * or with status {@code 400 (Bad Request)} if the distinctDTO is not valid,
     * or with status {@code 404 (Not Found)} if the distinctDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the distinctDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DistinctDTO> partialUpdateDistinct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DistinctDTO distinctDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Distinct partially : {}, {}", id, distinctDTO);
        if (distinctDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distinctDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distinctRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DistinctDTO> result = distinctService.partialUpdate(distinctDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, distinctDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /distincts} : get all the distincts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of distincts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DistinctDTO>> getAllDistincts(
        DistinctCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Distincts by criteria: {}", criteria);

        Page<DistinctDTO> page = distinctQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /distincts/count} : count all the distincts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDistincts(DistinctCriteria criteria) {
        LOG.debug("REST request to count Distincts by criteria: {}", criteria);
        return ResponseEntity.ok().body(distinctQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /distincts/:id} : get the "id" distinct.
     *
     * @param id the id of the distinctDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the distinctDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DistinctDTO> getDistinct(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Distinct : {}", id);
        Optional<DistinctDTO> distinctDTO = distinctService.findOne(id);
        return ResponseUtil.wrapOrNotFound(distinctDTO);
    }

    /**
     * {@code DELETE  /distincts/:id} : delete the "id" distinct.
     *
     * @param id the id of the distinctDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistinct(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Distinct : {}", id);
        distinctService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
