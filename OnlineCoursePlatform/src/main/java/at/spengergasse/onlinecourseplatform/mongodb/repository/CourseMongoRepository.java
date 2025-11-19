package at.spengergasse.onlinecourseplatform.mongodb.repository;

import at.spengergasse.onlinecourseplatform.mongodb.document.CourseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMongoRepository extends MongoRepository<CourseDocument, String> {

    List<CourseDocument> findByNameContainingIgnoreCase(String name);

    List<CourseDocument> findByInstructorId(String instructorId);
}
