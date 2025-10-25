package com.turling.repository;

import com.turling.domain.StudentAnswerLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentAnswerLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentAnswerLogRepository extends JpaRepository<StudentAnswerLog, Long>, JpaSpecificationExecutor<StudentAnswerLog> {
    /**
     * Find student answer log by student id and question id
     *
     * @param studentId the student id
     * @param questionId the question id
     * @return Optional of StudentAnswerLog
     */
    Optional<StudentAnswerLog> findByStudentIdAndQuestionId(Long studentId, Long questionId);
}
