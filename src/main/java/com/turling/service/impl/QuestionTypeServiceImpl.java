package com.turling.service.impl;

import com.turling.domain.QuestionType;
import com.turling.repository.QuestionTypeRepository;
import com.turling.service.QuestionTypeService;
import com.turling.service.dto.QuestionTypeDTO;
import com.turling.service.mapper.QuestionTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.QuestionType}.
 */
@Service
@Transactional
public class QuestionTypeServiceImpl implements QuestionTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionTypeServiceImpl.class);

    private final QuestionTypeRepository questionTypeRepository;

    private final QuestionTypeMapper questionTypeMapper;

    public QuestionTypeServiceImpl(QuestionTypeRepository questionTypeRepository, QuestionTypeMapper questionTypeMapper) {
        this.questionTypeRepository = questionTypeRepository;
        this.questionTypeMapper = questionTypeMapper;
    }

    @Override
    public QuestionTypeDTO save(QuestionTypeDTO questionTypeDTO) {
        LOG.debug("Request to save QuestionType : {}", questionTypeDTO);
        QuestionType questionType = questionTypeMapper.toEntity(questionTypeDTO);
        questionType = questionTypeRepository.save(questionType);
        return questionTypeMapper.toDto(questionType);
    }

    @Override
    public QuestionTypeDTO update(QuestionTypeDTO questionTypeDTO) {
        LOG.debug("Request to update QuestionType : {}", questionTypeDTO);
        QuestionType questionType = questionTypeMapper.toEntity(questionTypeDTO);
        questionType = questionTypeRepository.save(questionType);
        return questionTypeMapper.toDto(questionType);
    }

    @Override
    public Optional<QuestionTypeDTO> partialUpdate(QuestionTypeDTO questionTypeDTO) {
        LOG.debug("Request to partially update QuestionType : {}", questionTypeDTO);

        return questionTypeRepository
            .findById(questionTypeDTO.getId())
            .map(existingQuestionType -> {
                questionTypeMapper.partialUpdate(existingQuestionType, questionTypeDTO);

                return existingQuestionType;
            })
            .map(questionTypeRepository::save)
            .map(questionTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all QuestionTypes");
        return questionTypeRepository.findAll(pageable).map(questionTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionTypeDTO> findOne(Long id) {
        LOG.debug("Request to get QuestionType : {}", id);
        return questionTypeRepository.findById(id).map(questionTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete QuestionType : {}", id);
        questionTypeRepository.deleteById(id);
    }
}
