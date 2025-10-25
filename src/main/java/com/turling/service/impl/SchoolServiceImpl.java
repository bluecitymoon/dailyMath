package com.turling.service.impl;

import com.turling.domain.School;
import com.turling.repository.SchoolRepository;
import com.turling.service.SchoolService;
import com.turling.service.dto.SchoolDTO;
import com.turling.service.mapper.SchoolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.School}.
 */
@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private static final Logger LOG = LoggerFactory.getLogger(SchoolServiceImpl.class);

    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    public SchoolServiceImpl(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
    }

    @Override
    public SchoolDTO save(SchoolDTO schoolDTO) {
        LOG.debug("Request to save School : {}", schoolDTO);
        School school = schoolMapper.toEntity(schoolDTO);
        school = schoolRepository.save(school);
        return schoolMapper.toDto(school);
    }

    @Override
    public SchoolDTO update(SchoolDTO schoolDTO) {
        LOG.debug("Request to update School : {}", schoolDTO);
        School school = schoolMapper.toEntity(schoolDTO);
        school = schoolRepository.save(school);
        return schoolMapper.toDto(school);
    }

    @Override
    public Optional<SchoolDTO> partialUpdate(SchoolDTO schoolDTO) {
        LOG.debug("Request to partially update School : {}", schoolDTO);

        return schoolRepository
            .findById(schoolDTO.getId())
            .map(existingSchool -> {
                schoolMapper.partialUpdate(existingSchool, schoolDTO);

                return existingSchool;
            })
            .map(schoolRepository::save)
            .map(schoolMapper::toDto);
    }

    public Page<SchoolDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schoolRepository.findAllWithEagerRelationships(pageable).map(schoolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchoolDTO> findOne(Long id) {
        LOG.debug("Request to get School : {}", id);
        return schoolRepository.findOneWithEagerRelationships(id).map(schoolMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete School : {}", id);
        schoolRepository.deleteById(id);
    }
}
