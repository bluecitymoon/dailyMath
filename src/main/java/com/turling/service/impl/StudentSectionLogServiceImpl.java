package com.turling.service.impl;

import com.turling.domain.StudentSectionLog;
import com.turling.repository.StudentSectionLogRepository;
import com.turling.service.StudentSectionLogService;
import com.turling.service.dto.StudentSectionLogDTO;
import com.turling.service.mapper.StudentSectionLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.StudentSectionLog}.
 */
@Service
@Transactional
public class StudentSectionLogServiceImpl implements StudentSectionLogService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentSectionLogServiceImpl.class);

    private final StudentSectionLogRepository studentSectionLogRepository;

    private final StudentSectionLogMapper studentSectionLogMapper;

    public StudentSectionLogServiceImpl(
        StudentSectionLogRepository studentSectionLogRepository,
        StudentSectionLogMapper studentSectionLogMapper
    ) {
        this.studentSectionLogRepository = studentSectionLogRepository;
        this.studentSectionLogMapper = studentSectionLogMapper;
    }

    @Override
    public StudentSectionLogDTO save(StudentSectionLogDTO studentSectionLogDTO) {
        LOG.debug("Request to save StudentSectionLog : {}", studentSectionLogDTO);
        StudentSectionLog studentSectionLog = studentSectionLogMapper.toEntity(studentSectionLogDTO);
        studentSectionLog = studentSectionLogRepository.save(studentSectionLog);
        return studentSectionLogMapper.toDto(studentSectionLog);
    }

    @Override
    public StudentSectionLogDTO update(StudentSectionLogDTO studentSectionLogDTO) {
        LOG.debug("Request to update StudentSectionLog : {}", studentSectionLogDTO);
        StudentSectionLog studentSectionLog = studentSectionLogMapper.toEntity(studentSectionLogDTO);
        studentSectionLog = studentSectionLogRepository.save(studentSectionLog);
        return studentSectionLogMapper.toDto(studentSectionLog);
    }

    @Override
    public Optional<StudentSectionLogDTO> partialUpdate(StudentSectionLogDTO studentSectionLogDTO) {
        LOG.debug("Request to partially update StudentSectionLog : {}", studentSectionLogDTO);

        return studentSectionLogRepository
            .findById(studentSectionLogDTO.getId())
            .map(existingStudentSectionLog -> {
                studentSectionLogMapper.partialUpdate(existingStudentSectionLog, studentSectionLogDTO);

                return existingStudentSectionLog;
            })
            .map(studentSectionLogRepository::save)
            .map(studentSectionLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentSectionLogDTO> findOne(Long id) {
        LOG.debug("Request to get StudentSectionLog : {}", id);
        return studentSectionLogRepository.findById(id).map(studentSectionLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StudentSectionLog : {}", id);
        studentSectionLogRepository.deleteById(id);
    }
}
