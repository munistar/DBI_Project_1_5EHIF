package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends CrudRepository<Instructor, Long> {
    List<Instructor> findByFirstNameIgnoreCase(String firstname);
    List<Instructor> findByLastNameIgnoreCase (String lastName);

}
