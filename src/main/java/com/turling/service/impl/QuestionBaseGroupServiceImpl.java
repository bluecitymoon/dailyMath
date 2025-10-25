package com.turling.service.impl;

import com.turling.domain.QuestionBaseGroup;
import com.turling.repository.QuestionBaseGroupRepository;
import com.turling.service.QuestionBaseGroupService;
import com.turling.service.dto.QuestionBaseGroupDTO;
import com.turling.service.mapper.QuestionBaseGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.QuestionBaseGroup}.
 */
@Service
@Transactional
public class QuestionBaseGroupServiceImpl implements QuestionBaseGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionBaseGroupServiceImpl.class);

    private final QuestionBaseGroupRepository questionBaseGroupRepository;

    private final QuestionBaseGroupMapper questionBaseGroupMapper;

    public QuestionBaseGroupServiceImpl(
        QuestionBaseGroupRepository questionBaseGroupRepository,
        QuestionBaseGroupMapper questionBaseGroupMapper
    ) {
        this.questionBaseGroupRepository = questionBaseGroupRepository;
        this.questionBaseGroupMapper = questionBaseGroupMapper;
    }

    @Override
    public QuestionBaseGroupDTO save(QuestionBaseGroupDTO questionBaseGroupDTO) {
        LOG.debug("Request to save QuestionBaseGroup : {}", questionBaseGroupDTO);
        QuestionBaseGroup questionBaseGroup = questionBaseGroupMapper.toEntity(questionBaseGroupDTO);
        questionBaseGroup = questionBaseGroupRepository.save(questionBaseGroup);
        return questionBaseGroupMapper.toDto(questionBaseGroup);
    }

    @Override
    public QuestionBaseGroupDTO update(QuestionBaseGroupDTO questionBaseGroupDTO) {
        LOG.debug("Request to update QuestionBaseGroup : {}", questionBaseGroupDTO);
        QuestionBaseGroup questionBaseGroup = questionBaseGroupMapper.toEntity(questionBaseGroupDTO);
        questionBaseGroup = questionBaseGroupRepository.save(questionBaseGroup);
        return questionBaseGroupMapper.toDto(questionBaseGroup);
    }

    @Override
    public Optional<QuestionBaseGroupDTO> partialUpdate(QuestionBaseGroupDTO questionBaseGroupDTO) {
        LOG.debug("Request to partially update QuestionBaseGroup : {}", questionBaseGroupDTO);

        return questionBaseGroupRepository
            .findById(questionBaseGroupDTO.getId())
            .map(existingQuestionBaseGroup -> {
                questionBaseGroupMapper.partialUpdate(existingQuestionBaseGroup, questionBaseGroupDTO);

                return existingQuestionBaseGroup;
            })
            .map(questionBaseGroupRepository::save)
            .map(questionBaseGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionBaseGroupDTO> findOne(Long id) {
        LOG.debug("Request to get QuestionBaseGroup : {}", id);
        return questionBaseGroupRepository.findById(id).map(questionBaseGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete QuestionBaseGroup : {}", id);
        questionBaseGroupRepository.deleteById(id);
    }
}
