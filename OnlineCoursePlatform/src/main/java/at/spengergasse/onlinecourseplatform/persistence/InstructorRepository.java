package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    List<Instructor> findByFirstNameContainingIgnoreCase(String firstName);
    List<Instructor> findByLastNameContainingIgnoreCase(String lastName);
    List<Instructor> findByDepartmentIgnoreCase(String department);
    Optional<Instructor> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT i FROM Instructor i WHERE LOWER(i.firstName) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(i.lastName) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(i.department) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Instructor> searchInstructors(@Param("term") String searchTerm);

    List<Instructor> findByHireDateBefore(java.time.LocalDate date);
}