package com.turling.service.impl;

import com.turling.domain.Community;
import com.turling.repository.CommunityRepository;
import com.turling.service.CommunityService;
import com.turling.service.dto.CommunityDTO;
import com.turling.service.mapper.CommunityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.Community}.
 */
@Service
@Transactional
public class CommunityServiceImpl implements CommunityService {

    private static final Logger LOG = LoggerFactory.getLogger(CommunityServiceImpl.class);

    private final CommunityRepository communityRepository;

    private final CommunityMapper communityMapper;

    public CommunityServiceImpl(CommunityRepository communityRepository, CommunityMapper communityMapper) {
        this.communityRepository = communityRepository;
        this.communityMapper = communityMapper;
    }

    @Override
    public CommunityDTO save(CommunityDTO communityDTO) {
        LOG.debug("Request to save Community : {}", communityDTO);
        Community community = communityMapper.toEntity(communityDTO);
        community = communityRepository.save(community);
        return communityMapper.toDto(community);
    }

    @Override
    public CommunityDTO update(CommunityDTO communityDTO) {
        LOG.debug("Request to update Community : {}", communityDTO);
        Community community = communityMapper.toEntity(communityDTO);
        community = communityRepository.save(community);
        return communityMapper.toDto(community);
    }

    @Override
    public Optional<CommunityDTO> partialUpdate(CommunityDTO communityDTO) {
        LOG.debug("Request to partially update Community : {}", communityDTO);

        return communityRepository
            .findById(communityDTO.getId())
            .map(existingCommunity -> {
                communityMapper.partialUpdate(existingCommunity, communityDTO);

                return existingCommunity;
            })
            .map(communityRepository::save)
            .map(communityMapper::toDto);
    }

    public Page<CommunityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return communityRepository.findAllWithEagerRelationships(pageable).map(communityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommunityDTO> findOne(Long id) {
        LOG.debug("Request to get Community : {}", id);
        return communityRepository.findOneWithEagerRelationships(id).map(communityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Community : {}", id);
        communityRepository.deleteById(id);
    }
}
