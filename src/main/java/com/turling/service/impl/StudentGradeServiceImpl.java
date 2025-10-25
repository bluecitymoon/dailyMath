package com.turling.service.impl;

import com.turling.domain.StudentGrade;
import com.turling.repository.StudentGradeRepository;
import com.turling.service.StudentGradeService;
import com.turling.service.dto.StudentGradeDTO;
import com.turling.service.mapper.StudentGradeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.turling.domain.StudentGrade}.
 */
@Service
@Transactional
public class StudentGradeServiceImpl implements StudentGradeService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentGradeServiceImpl.class);

    private final StudentGradeRepository studentGradeRepository;

    private final StudentGradeMapper studentGradeMapper;

    public StudentGradeServiceImpl(StudentGradeRepository studentGradeRepository, StudentGradeMapper studentGradeMapper) {
        this.studentGradeRepository = studentGradeRepository;
        this.studentGradeMapper = studentGradeMapper;
    }

    @Override
    public StudentGradeDTO save(StudentGradeDTO studentGradeDTO) {
        LOG.debug("Request to save StudentGrade : {}", studentGradeDTO);
        StudentGrade studentGrade = studentGradeMapper.toEntity(studentGradeDTO);
        studentGrade = studentGradeRepository.save(studentGrade);
        return studentGradeMapper.toDto(studentGrade);
    }

    @Override
    public StudentGradeDTO update(StudentGradeDTO studentGradeDTO) {
        LOG.debug("Request to update StudentGrade : {}", studentGradeDTO);
        StudentGrade studentGrade = studentGradeMapper.toEntity(studentGradeDTO);
        studentGrade = studentGradeRepository.save(studentGrade);
        return studentGradeMapper.toDto(studentGrade);
    }

    @Override
    public Optional<StudentGradeDTO> partialUpdate(StudentGradeDTO studentGradeDTO) {
        LOG.debug("Request to partially update StudentGrade : {}", studentGradeDTO);

        return studentGradeRepository
            .findById(studentGradeDTO.getId())
            .map(existingStudentGrade -> {
                studentGradeMapper.partialUpdate(existingStudentGrade, studentGradeDTO);

                return existingStudentGrade;
            })
            .map(studentGradeRepository::save)
            .map(studentGradeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentGradeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StudentGrades");
        return studentGradeRepository.findAll(pageable).map(studentGradeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentGradeDTO> findOne(Long id) {
        LOG.debug("Request to get StudentGrade : {}", id);
        return studentGradeRepository.findById(id).map(studentGradeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StudentGrade : {}", id);
        studentGradeRepository.deleteById(id);
    }
}
