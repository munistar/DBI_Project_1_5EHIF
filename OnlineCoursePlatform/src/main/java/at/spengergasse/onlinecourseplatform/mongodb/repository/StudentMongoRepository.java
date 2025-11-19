package at.spengergasse.onlinecourseplatform.mongodb.repository;

import at.spengergasse.onlinecourseplatform.mongodb.document.StudentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentMongoRepository extends MongoRepository<StudentDocument, String> {

    Optional<StudentDocument> findByEmail(String email);

    long countByLastName(String lastName);
}
