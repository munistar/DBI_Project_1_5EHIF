package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Find course by name (case-insensitive)
     */
    Optional<Course> findByNameIgnoreCase(String name);

    /**
     * Find all courses by instructor ID
     */
    List<Course> findByInstructorId(Long instructorId);

    /**
     * Search courses by name containing keyword (case-insensitive)
     */
    List<Course> findByNameContainingIgnoreCase(String keyword);

    /**
     * Search courses by description containing keyword (case-insensitive)
     */
    List<Course> findByDescriptionContainingIgnoreCase(String keyword);

    /**
     * Check if a course with given name exists
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Custom query to search courses by name or description
     */
    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Course> searchCourses(@Param("searchTerm") String searchTerm);

    /**
     * Count courses by instructor ID
     */
    long countByInstructorId(Long instructorId);
}
