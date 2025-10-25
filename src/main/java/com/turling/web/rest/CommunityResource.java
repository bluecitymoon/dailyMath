package com.turling.web.rest;

import com.turling.repository.CommunityRepository;
import com.turling.service.CommunityQueryService;
import com.turling.service.CommunityService;
import com.turling.service.criteria.CommunityCriteria;
import com.turling.service.dto.CommunityDTO;
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
 * REST controller for managing {@link com.turling.domain.Community}.
 */
@RestController
@RequestMapping("/api/communities")
public class CommunityResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommunityResource.class);

    private static final String ENTITY_NAME = "community";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityService communityService;

    private final CommunityRepository communityRepository;

    private final CommunityQueryService communityQueryService;

    public CommunityResource(
        CommunityService communityService,
        CommunityRepository communityRepository,
        CommunityQueryService communityQueryService
    ) {
        this.communityService = communityService;
        this.communityRepository = communityRepository;
        this.communityQueryService = communityQueryService;
    }

    /**
     * {@code POST  /communities} : Create a new community.
     *
     * @param communityDTO the communityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityDTO, or with status {@code 400 (Bad Request)} if the community has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommunityDTO> createCommunity(@RequestBody CommunityDTO communityDTO) throws URISyntaxException {
        LOG.debug("REST request to save Community : {}", communityDTO);
        if (communityDTO.getId() != null) {
            throw new BadRequestAlertException("A new community cannot already have an ID", ENTITY_NAME, "idexists");
        }
        communityDTO = communityService.save(communityDTO);
        return ResponseEntity.created(new URI("/api/communities/" + communityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, communityDTO.getId().toString()))
            .body(communityDTO);
    }

    /**
     * {@code PUT  /communities/:id} : Updates an existing community.
     *
     * @param id the id of the communityDTO to save.
     * @param communityDTO the communityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityDTO,
     * or with status {@code 400 (Bad Request)} if the communityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommunityDTO> updateCommunity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityDTO communityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Community : {}, {}", id, communityDTO);
        if (communityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        communityDTO = communityService.update(communityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, communityDTO.getId().toString()))
            .body(communityDTO);
    }

    /**
     * {@code PATCH  /communities/:id} : Partial updates given fields of an existing community, field will ignore if it is null
     *
     * @param id the id of the communityDTO to save.
     * @param communityDTO the communityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityDTO,
     * or with status {@code 400 (Bad Request)} if the communityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the communityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunityDTO> partialUpdateCommunity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityDTO communityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Community partially : {}, {}", id, communityDTO);
        if (communityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityDTO> result = communityService.partialUpdate(communityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, communityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /communities} : get all the communities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommunityDTO>> getAllCommunities(
        CommunityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Communities by criteria: {}", criteria);

        Page<CommunityDTO> page = communityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communities/count} : count all the communities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCommunities(CommunityCriteria criteria) {
        LOG.debug("REST request to count Communities by criteria: {}", criteria);
        return ResponseEntity.ok().body(communityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /communities/:id} : get the "id" community.
     *
     * @param id the id of the communityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommunityDTO> getCommunity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Community : {}", id);
        Optional<CommunityDTO> communityDTO = communityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communityDTO);
    }

    /**
     * {@code DELETE  /communities/:id} : delete the "id" community.
     *
     * @param id the id of the communityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Community : {}", id);
        communityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
