package at.spengergasse.onlinecourseplatform.mongodb.repository;

import at.spengergasse.onlinecourseplatform.mongodb.document.InstructorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorMongoRepository extends MongoRepository<InstructorDocument, String> {

    Optional<InstructorDocument> findByEmail(String email);

    List<InstructorDocument> findByDepartment(String department);
}
