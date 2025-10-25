package com.turling.service.impl;

import com.turling.domain.Distinct;
import com.turling.repository.DistinctRepository;
import com.turling.service.DistinctService;
import com.turling.service.dto.DistinctDTO;
import com.turling.service.mapper.DistinctMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.Distinct}.
 */
@Service
@Transactional
public class DistinctServiceImpl implements DistinctService {

    private static final Logger LOG = LoggerFactory.getLogger(DistinctServiceImpl.class);

    private final DistinctRepository distinctRepository;

    private final DistinctMapper distinctMapper;

    public DistinctServiceImpl(DistinctRepository distinctRepository, DistinctMapper distinctMapper) {
        this.distinctRepository = distinctRepository;
        this.distinctMapper = distinctMapper;
    }

    @Override
    public DistinctDTO save(DistinctDTO distinctDTO) {
        LOG.debug("Request to save Distinct : {}", distinctDTO);
        Distinct distinct = distinctMapper.toEntity(distinctDTO);
        distinct = distinctRepository.save(distinct);
        return distinctMapper.toDto(distinct);
    }

    @Override
    public DistinctDTO update(DistinctDTO distinctDTO) {
        LOG.debug("Request to update Distinct : {}", distinctDTO);
        Distinct distinct = distinctMapper.toEntity(distinctDTO);
        distinct = distinctRepository.save(distinct);
        return distinctMapper.toDto(distinct);
    }

    @Override
    public Optional<DistinctDTO> partialUpdate(DistinctDTO distinctDTO) {
        LOG.debug("Request to partially update Distinct : {}", distinctDTO);

        return distinctRepository
            .findById(distinctDTO.getId())
            .map(existingDistinct -> {
                distinctMapper.partialUpdate(existingDistinct, distinctDTO);

                return existingDistinct;
            })
            .map(distinctRepository::save)
            .map(distinctMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DistinctDTO> findOne(Long id) {
        LOG.debug("Request to get Distinct : {}", id);
        return distinctRepository.findById(id).map(distinctMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Distinct : {}", id);
        distinctRepository.deleteById(id);
    }
}
