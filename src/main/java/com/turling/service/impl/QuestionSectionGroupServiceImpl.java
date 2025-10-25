package com.turling.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.domain.Question;
import com.turling.domain.QuestionBaseGroup;
import com.turling.domain.QuestionSectionGroup;
import com.turling.repository.QuestionBaseGroupRepository;
import com.turling.repository.QuestionRepository;
import com.turling.repository.QuestionSectionGroupRepository;
import com.turling.service.QuestionSectionGroupService;
import com.turling.service.dto.QuestionSectionGroupDTO;
import com.turling.service.dto.QuestionWithBaseGroupDTO;
import com.turling.service.dto.SectionGroupQuestionsResponse;
import com.turling.service.mapper.QuestionMapper;
import com.turling.service.mapper.QuestionSectionGroupMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.QuestionSectionGroup}.
 */
@Service
@Transactional
public class QuestionSectionGroupServiceImpl implements QuestionSectionGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionSectionGroupServiceImpl.class);

    private final QuestionSectionGroupRepository questionSectionGroupRepository;
    private final QuestionRepository questionRepository;
    private final QuestionBaseGroupRepository questionBaseGroupRepository;
    private final QuestionSectionGroupMapper questionSectionGroupMapper;
    private final QuestionMapper questionMapper;
    private final ObjectMapper objectMapper;

    public QuestionSectionGroupServiceImpl(
        QuestionSectionGroupRepository questionSectionGroupRepository,
        QuestionRepository questionRepository,
        QuestionBaseGroupRepository questionBaseGroupRepository,
        QuestionSectionGroupMapper questionSectionGroupMapper,
        QuestionMapper questionMapper,
        ObjectMapper objectMapper
    ) {
        this.questionSectionGroupRepository = questionSectionGroupRepository;
        this.questionRepository = questionRepository;
        this.questionBaseGroupRepository = questionBaseGroupRepository;
        this.questionSectionGroupMapper = questionSectionGroupMapper;
        this.questionMapper = questionMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public QuestionSectionGroupDTO save(QuestionSectionGroupDTO questionSectionGroupDTO) {
        LOG.debug("Request to save QuestionSectionGroup : {}", questionSectionGroupDTO);
        QuestionSectionGroup questionSectionGroup = questionSectionGroupMapper.toEntity(questionSectionGroupDTO);

        // 如果是新创建的记录（ID为null），自动设置displayOrder为最大值+1
        if (questionSectionGroup.getId() == null) {
            Integer maxOrder = questionSectionGroupRepository.findMaxDisplayOrder().orElse(0);
            questionSectionGroup.setDisplayOrder(maxOrder + 1);
        }

        questionSectionGroup = questionSectionGroupRepository.save(questionSectionGroup);
        return questionSectionGroupMapper.toDto(questionSectionGroup);
    }

    @Override
    public QuestionSectionGroupDTO update(QuestionSectionGroupDTO questionSectionGroupDTO) {
        LOG.debug("Request to update QuestionSectionGroup : {}", questionSectionGroupDTO);
        QuestionSectionGroup questionSectionGroup = questionSectionGroupMapper.toEntity(questionSectionGroupDTO);
        questionSectionGroup = questionSectionGroupRepository.save(questionSectionGroup);
        return questionSectionGroupMapper.toDto(questionSectionGroup);
    }

    @Override
    public Optional<QuestionSectionGroupDTO> partialUpdate(QuestionSectionGroupDTO questionSectionGroupDTO) {
        LOG.debug("Request to partially update QuestionSectionGroup : {}", questionSectionGroupDTO);

        return questionSectionGroupRepository
            .findById(questionSectionGroupDTO.getId())
            .map(existingQuestionSectionGroup -> {
                questionSectionGroupMapper.partialUpdate(existingQuestionSectionGroup, questionSectionGroupDTO);

                return existingQuestionSectionGroup;
            })
            .map(questionSectionGroupRepository::save)
            .map(questionSectionGroupMapper::toDto);
    }

    public Page<QuestionSectionGroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return questionSectionGroupRepository.findAllWithEagerRelationships(pageable).map(questionSectionGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionSectionGroupDTO> findOne(Long id) {
        LOG.debug("Request to get QuestionSectionGroup : {}", id);
        return questionSectionGroupRepository.findOneWithEagerRelationships(id).map(questionSectionGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete QuestionSectionGroup : {}", id);
        questionSectionGroupRepository.deleteById(id);
    }

    @Override
    public void updateDisplayOrderForPage(List<Long> orderedIds, int page, int size) {
        LOG.debug("Request to update display order for QuestionSectionGroups page {} with size {} : {}", page, size, orderedIds);

        // 计算当前页面在全局排序中的起始位置
        int globalStartOrder = page * size + 1;

        // 更新当前页面的排序
        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            Integer newOrder = globalStartOrder + i; // 基于全局位置计算新排序

            questionSectionGroupRepository
                .findById(id)
                .ifPresent(group -> {
                    group.setDisplayOrder(newOrder);
                    questionSectionGroupRepository.save(group);
                });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionSectionGroupDTO> findByGradeId(Long gradeId) {
        LOG.debug("Request to get QuestionSectionGroups by grade ID : {}", gradeId);
        return questionSectionGroupRepository
            .findByGradeIdOrderByDisplayOrder(gradeId)
            .stream()
            .map(questionSectionGroupMapper::toDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionSectionGroupDTO> findByGradeId(Long gradeId, Pageable pageable) {
        LOG.debug("Request to get QuestionSectionGroups by grade ID with pagination : {}, {}", gradeId, pageable);
        return questionSectionGroupRepository.findByGradeIdOrderByDisplayOrder(gradeId, pageable).map(questionSectionGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public SectionGroupQuestionsResponse getQuestionsBySectionGroupId(Long sectionGroupId) {
        LOG.debug("Request to get questions by section group ID : {}", sectionGroupId);

        Optional<QuestionSectionGroup> sectionGroupOpt = questionSectionGroupRepository.findById(sectionGroupId);
        if (sectionGroupOpt.isEmpty()) {
            return new SectionGroupQuestionsResponse();
        }

        QuestionSectionGroup sectionGroup = sectionGroupOpt.get();
        List<QuestionWithBaseGroupDTO> questionsWithBaseGroup = getQuestionsWithBaseGroupInfo(sectionGroup);

        SectionGroupQuestionsResponse response = new SectionGroupQuestionsResponse();
        response.setSectionGroupId(sectionGroup.getId());
        response.setSectionGroupTitle(sectionGroup.getTitle());
        response.setSectionGroupBaseGroupIds(sectionGroup.getBaseGroupIds());
        response.setSectionGroupGrade(questionSectionGroupMapper.toDto(sectionGroup).getGrade());
        response.setSectionGroupDisplayOrder(sectionGroup.getDisplayOrder());
        response.setQuestions(questionsWithBaseGroup);
        response.setTotalQuestionCount(questionsWithBaseGroup.size());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public SectionGroupQuestionsResponse getQuestionsBySectionGroupId(Long sectionGroupId, Pageable pageable) {
        LOG.debug("Request to get questions by section group ID with pagination : {}, {}", sectionGroupId, pageable);

        Optional<QuestionSectionGroup> sectionGroupOpt = questionSectionGroupRepository.findById(sectionGroupId);
        if (sectionGroupOpt.isEmpty()) {
            return new SectionGroupQuestionsResponse();
        }

        QuestionSectionGroup sectionGroup = sectionGroupOpt.get();
        List<QuestionWithBaseGroupDTO> questionsWithBaseGroup = getQuestionsWithBaseGroupInfo(sectionGroup, pageable);

        SectionGroupQuestionsResponse response = new SectionGroupQuestionsResponse();
        response.setSectionGroupId(sectionGroup.getId());
        response.setSectionGroupTitle(sectionGroup.getTitle());
        response.setSectionGroupBaseGroupIds(sectionGroup.getBaseGroupIds());
        response.setSectionGroupGrade(questionSectionGroupMapper.toDto(sectionGroup).getGrade());
        response.setSectionGroupDisplayOrder(sectionGroup.getDisplayOrder());
        response.setQuestions(questionsWithBaseGroup);
        response.setTotalQuestionCount(questionsWithBaseGroup.size());

        return response;
    }

    private List<QuestionWithBaseGroupDTO> getQuestionsWithBaseGroupInfo(QuestionSectionGroup sectionGroup) {
        return getQuestionsWithBaseGroupInfo(sectionGroup, null);
    }

    private List<QuestionWithBaseGroupDTO> getQuestionsWithBaseGroupInfo(QuestionSectionGroup sectionGroup, Pageable pageable) {
        List<QuestionWithBaseGroupDTO> result = new ArrayList<>();

        if (sectionGroup.getBaseGroupIds() == null || sectionGroup.getBaseGroupIds().trim().isEmpty()) {
            return result;
        }

        // Parse base group IDs from JSON format or comma-separated string
        List<Long> baseGroupIds = parseBaseGroupIds(sectionGroup.getBaseGroupIds());

        if (baseGroupIds.isEmpty()) {
            return result;
        }

        // Get all base groups
        List<QuestionBaseGroup> baseGroups = questionBaseGroupRepository.findAllById(baseGroupIds);

        // Collect all question IDs from all base groups
        List<Long> allQuestionIds = new ArrayList<>();
        for (QuestionBaseGroup baseGroup : baseGroups) {
            if (baseGroup.getQuestionIds() != null && !baseGroup.getQuestionIds().trim().isEmpty()) {
                List<Long> questionIds = parseQuestionIds(baseGroup.getQuestionIds());
                allQuestionIds.addAll(questionIds);
            }
        }

        if (allQuestionIds.isEmpty()) {
            return result;
        }

        // Get questions with eager relationships
        List<Question> questions;
        if (pageable != null) {
            Page<Question> questionPage = questionRepository.findByIdsWithEagerRelationships(allQuestionIds, pageable);
            questions = questionPage.getContent();
        } else {
            questions = questionRepository.findByIdsWithEagerRelationships(allQuestionIds);
        }

        // Create a map of base group ID to base group for quick lookup
        var baseGroupMap = baseGroups.stream().collect(Collectors.toMap(QuestionBaseGroup::getId, bg -> bg));

        // Convert questions to DTOs with base group information
        for (Question question : questions) {
            QuestionWithBaseGroupDTO dto = convertToQuestionWithBaseGroupDTO(question, baseGroupMap);
            result.add(dto);
        }

        return result;
    }

    private QuestionWithBaseGroupDTO convertToQuestionWithBaseGroupDTO(
        Question question,
        java.util.Map<Long, QuestionBaseGroup> baseGroupMap
    ) {
        QuestionWithBaseGroupDTO dto = new QuestionWithBaseGroupDTO();

        // Set question fields
        dto.setId(question.getId());
        dto.setPoints(question.getPoints());
        dto.setDescription(question.getDescription());
        dto.setSolution(question.getSolution());
        dto.setSolutionExternalLink(question.getSolutionExternalLink());
        dto.setCreateDate(question.getCreateDate());
        dto.setUpdateDate(question.getUpdateDate());
        dto.setCreateBy(question.getCreateBy());
        dto.setCreateByUserId(question.getCreateByUserId());
        dto.setAnswer(question.getAnswer());
        dto.setLevel(question.getLevel());

        // Convert related entities to DTOs
        if (question.getQuestionCategory() != null) {
            dto.setQuestionCategory(questionMapper.toDto(question).getQuestionCategory());
        }
        if (question.getType() != null) {
            dto.setType(questionMapper.toDto(question).getType());
        }
        if (question.getGrade() != null) {
            dto.setGrade(questionMapper.toDto(question).getGrade());
        }
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            dto.setOptions(questionMapper.toDto(question).getOptions());
        }

        // Find the base group that contains this question
        for (var entry : baseGroupMap.entrySet()) {
            QuestionBaseGroup baseGroup = entry.getValue();
            if (baseGroup.getQuestionIds() != null && !baseGroup.getQuestionIds().trim().isEmpty()) {
                List<Long> questionIds = parseQuestionIds(baseGroup.getQuestionIds());
                if (questionIds.contains(question.getId())) {
                    dto.setBaseGroupId(baseGroup.getId());
                    dto.setBaseGroupTitle(baseGroup.getTitle());
                    dto.setBaseGroupQuestionIds(baseGroup.getQuestionIds());
                    break;
                }
            }
        }

        return dto;
    }

    /**
     * Parse base group IDs from JSON format or comma-separated string.
     * Supports both formats:
     * - JSON: [{"id":1502}, {"id":1503}]
     * - Comma-separated: "1502,1503"
     */
    private List<Long> parseBaseGroupIds(String baseGroupIdsStr) {
        if (baseGroupIdsStr == null || baseGroupIdsStr.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Try to parse as JSON first
            if (baseGroupIdsStr.trim().startsWith("[")) {
                List<Map<String, Object>> jsonList = objectMapper.readValue(
                    baseGroupIdsStr,
                    new TypeReference<List<Map<String, Object>>>() {}
                );
                return jsonList
                    .stream()
                    .map(item -> {
                        Object idObj = item.get("id");
                        if (idObj instanceof Number) {
                            return ((Number) idObj).longValue();
                        } else if (idObj instanceof String) {
                            return Long.valueOf((String) idObj);
                        }
                        return null;
                    })
                    .filter(id -> id != null)
                    .collect(Collectors.toList());
            } else {
                // Parse as comma-separated string
                return Arrays.stream(baseGroupIdsStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            LOG.warn("Failed to parse baseGroupIds: {}, trying comma-separated format", baseGroupIdsStr, e);
            // Fallback to comma-separated parsing
            try {
                return Arrays.stream(baseGroupIdsStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            } catch (Exception ex) {
                LOG.error("Failed to parse baseGroupIds as comma-separated: {}", baseGroupIdsStr, ex);
                return new ArrayList<>();
            }
        }
    }

    /**
     * Parse question IDs from JSON format or comma-separated string.
     * Supports both formats:
     * - JSON: [{"id":1502}, {"id":1503}]
     * - Comma-separated: "1502,1503"
     */
    private List<Long> parseQuestionIds(String questionIdsStr) {
        if (questionIdsStr == null || questionIdsStr.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Try to parse as JSON first
            if (questionIdsStr.trim().startsWith("[")) {
                List<Map<String, Object>> jsonList = objectMapper.readValue(
                    questionIdsStr,
                    new TypeReference<List<Map<String, Object>>>() {}
                );
                return jsonList
                    .stream()
                    .map(item -> {
                        Object idObj = item.get("id");
                        if (idObj instanceof Number) {
                            return ((Number) idObj).longValue();
                        } else if (idObj instanceof String) {
                            return Long.valueOf((String) idObj);
                        }
                        return null;
                    })
                    .filter(id -> id != null)
                    .collect(Collectors.toList());
            } else {
                // Parse as comma-separated string
                return Arrays.stream(questionIdsStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            LOG.warn("Failed to parse questionIds: {}, trying comma-separated format", questionIdsStr, e);
            // Fallback to comma-separated parsing
            try {
                return Arrays.stream(questionIdsStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            } catch (Exception ex) {
                LOG.error("Failed to parse questionIds as comma-separated: {}", questionIdsStr, ex);
                return new ArrayList<>();
            }
        }
    }
}
