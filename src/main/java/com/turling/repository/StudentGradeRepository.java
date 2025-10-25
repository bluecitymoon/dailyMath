package com.turling.repository;

import com.turling.domain.StudentGrade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentGrade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentGradeRepository extends JpaRepository<StudentGrade, Long> {}
