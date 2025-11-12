package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Find all enrollments for a specific student
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * Find all enrollments for a specific course
     */
    List<Enrollment> findByCourseId(Long courseId);

    /**
     * Find a specific enrollment by student and course
     */
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Check if an enrollment exists for a student in a course
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Find all enrollments with grades above a certain threshold
     */
    List<Enrollment> findByGradeGreaterThanEqual(Double minGrade);

    /**
     * Find all enrollments with grades below a certain threshold
     */
    List<Enrollment> findByGradeLessThan(Double maxGrade);

    /**
     * Find all enrollments with grades between two values
     */
    List<Enrollment> findByGradeBetween(Double minGrade, Double maxGrade);

    /**
     * Find all enrollments without a grade assigned
     */
    List<Enrollment> findByGradeIsNull();

    /**
     * Find all enrollments with a grade assigned
     */
    List<Enrollment> findByGradeIsNotNull();

    /**
     * Count enrollments for a specific student
     */
    long countByStudentId(Long studentId);

    /**
     * Count enrollments for a specific course
     */
    long countByCourseId(Long courseId);

    /**
     * Calculate average grade for a student
     */
    @Query("SELECT AVG(e.grade) FROM Enrollment e WHERE e.studentId = :studentId AND e.grade IS NOT NULL")
    Double calculateAverageGradeByStudentId(@Param("studentId") Long studentId);

    /**
     * Calculate average grade for a course
     */
    @Query("SELECT AVG(e.grade) FROM Enrollment e WHERE e.courseId = :courseId AND e.grade IS NOT NULL")
    Double calculateAverageGradeByCourseId(@Param("courseId") Long courseId);

    /**
     * Find all enrollments for a student with grades
     */
    List<Enrollment> findByStudentIdAndGradeIsNotNull(Long studentId);

    /**
     * Find all enrollments for a course with grades
     */
    List<Enrollment> findByCourseIdAndGradeIsNotNull(Long courseId);

    /**
     * Delete all enrollments for a specific student
     */
    void deleteByStudentId(Long studentId);

    /**
     * Delete all enrollments for a specific course
     */
    void deleteByCourseId(Long courseId);
}
