package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByFirstNameContainingIgnoreCase(String firstName);
    List<Student> findByLastNameContainingIgnoreCase(String lastName);
    Optional<Student> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    List<Student> findByRegistrationDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Student> searchStudents(@Param("term") String searchTerm);

    long countByRegistrationDateAfter(LocalDate date);
}