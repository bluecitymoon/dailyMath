package com.turling.service.impl;

import com.turling.domain.StudentAnswerLog;
import com.turling.repository.StudentAnswerLogRepository;
import com.turling.service.StudentAnswerLogService;
import com.turling.service.dto.StudentAnswerLogDTO;
import com.turling.service.dto.StudentAnswerLogResponseDTO;
import com.turling.service.mapper.StudentAnswerLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.StudentAnswerLog}.
 */
@Service
@Transactional
public class StudentAnswerLogServiceImpl implements StudentAnswerLogService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentAnswerLogServiceImpl.class);

    private final StudentAnswerLogRepository studentAnswerLogRepository;

    private final StudentAnswerLogMapper studentAnswerLogMapper;

    public StudentAnswerLogServiceImpl(
        StudentAnswerLogRepository studentAnswerLogRepository,
        StudentAnswerLogMapper studentAnswerLogMapper
    ) {
        this.studentAnswerLogRepository = studentAnswerLogRepository;
        this.studentAnswerLogMapper = studentAnswerLogMapper;
    }

    @Override
    public StudentAnswerLogDTO save(StudentAnswerLogDTO studentAnswerLogDTO) {
        LOG.debug("Request to save StudentAnswerLog : {}", studentAnswerLogDTO);
        StudentAnswerLog studentAnswerLog = studentAnswerLogMapper.toEntity(studentAnswerLogDTO);
        studentAnswerLog = studentAnswerLogRepository.save(studentAnswerLog);
        return studentAnswerLogMapper.toDto(studentAnswerLog);
    }

    @Override
    public StudentAnswerLogDTO update(StudentAnswerLogDTO studentAnswerLogDTO) {
        LOG.debug("Request to update StudentAnswerLog : {}", studentAnswerLogDTO);
        StudentAnswerLog studentAnswerLog = studentAnswerLogMapper.toEntity(studentAnswerLogDTO);
        studentAnswerLog = studentAnswerLogRepository.save(studentAnswerLog);
        return studentAnswerLogMapper.toDto(studentAnswerLog);
    }

    @Override
    public Optional<StudentAnswerLogDTO> partialUpdate(StudentAnswerLogDTO studentAnswerLogDTO) {
        LOG.debug("Request to partially update StudentAnswerLog : {}", studentAnswerLogDTO);

        return studentAnswerLogRepository
            .findById(studentAnswerLogDTO.getId())
            .map(existingStudentAnswerLog -> {
                studentAnswerLogMapper.partialUpdate(existingStudentAnswerLog, studentAnswerLogDTO);

                return existingStudentAnswerLog;
            })
            .map(studentAnswerLogRepository::save)
            .map(studentAnswerLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentAnswerLogDTO> findOne(Long id) {
        LOG.debug("Request to get StudentAnswerLog : {}", id);
        return studentAnswerLogRepository.findById(id).map(studentAnswerLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StudentAnswerLog : {}", id);
        studentAnswerLogRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentAnswerLogResponseDTO findByStudentIdAndQuestionId(Long studentId, Long questionId) {
        LOG.debug("Request to get StudentAnswerLog by studentId: {} and questionId: {}", studentId, questionId);
        
        Optional<StudentAnswerLog> answerLog = studentAnswerLogRepository.findByStudentIdAndQuestionId(studentId, questionId);
        
        if (answerLog.isPresent()) {
            StudentAnswerLog log = answerLog.get();
            return new StudentAnswerLogResponseDTO(
                log.getId(),
                log.getStudentId(),
                log.getQuestionId(),
                log.getAnswer(),
                log.getCorrect(),
                log.getCreateDate(),
                log.getWinPoints(),
                true
            );
        } else {
            return new StudentAnswerLogResponseDTO(
                null,
                studentId,
                questionId,
                null,
                null,
                null,
                null,
                false
            );
        }
    }
}
