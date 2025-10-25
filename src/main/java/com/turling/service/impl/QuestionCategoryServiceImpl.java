package com.turling.service.impl;

import com.turling.domain.QuestionCategory;
import com.turling.repository.QuestionCategoryRepository;
import com.turling.service.QuestionCategoryService;
import com.turling.service.dto.QuestionCategoryDTO;
import com.turling.service.mapper.QuestionCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.QuestionCategory}.
 */
@Service
@Transactional
public class QuestionCategoryServiceImpl implements QuestionCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionCategoryServiceImpl.class);

    private final QuestionCategoryRepository questionCategoryRepository;

    private final QuestionCategoryMapper questionCategoryMapper;

    public QuestionCategoryServiceImpl(
        QuestionCategoryRepository questionCategoryRepository,
        QuestionCategoryMapper questionCategoryMapper
    ) {
        this.questionCategoryRepository = questionCategoryRepository;
        this.questionCategoryMapper = questionCategoryMapper;
    }

    @Override
    public QuestionCategoryDTO save(QuestionCategoryDTO questionCategoryDTO) {
        LOG.debug("Request to save QuestionCategory : {}", questionCategoryDTO);
        QuestionCategory questionCategory = questionCategoryMapper.toEntity(questionCategoryDTO);
        questionCategory = questionCategoryRepository.save(questionCategory);
        return questionCategoryMapper.toDto(questionCategory);
    }

    @Override
    public QuestionCategoryDTO update(QuestionCategoryDTO questionCategoryDTO) {
        LOG.debug("Request to update QuestionCategory : {}", questionCategoryDTO);
        QuestionCategory questionCategory = questionCategoryMapper.toEntity(questionCategoryDTO);
        questionCategory = questionCategoryRepository.save(questionCategory);
        return questionCategoryMapper.toDto(questionCategory);
    }

    @Override
    public Optional<QuestionCategoryDTO> partialUpdate(QuestionCategoryDTO questionCategoryDTO) {
        LOG.debug("Request to partially update QuestionCategory : {}", questionCategoryDTO);

        return questionCategoryRepository
            .findById(questionCategoryDTO.getId())
            .map(existingQuestionCategory -> {
                questionCategoryMapper.partialUpdate(existingQuestionCategory, questionCategoryDTO);

                return existingQuestionCategory;
            })
            .map(questionCategoryRepository::save)
            .map(questionCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get QuestionCategory : {}", id);
        return questionCategoryRepository.findById(id).map(questionCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete QuestionCategory : {}", id);
        questionCategoryRepository.deleteById(id);
    }
}
