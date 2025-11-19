package at.spengergasse.onlinecourseplatform.controller;

import at.spengergasse.onlinecourseplatform.mongodb.document.CourseDocument;
import at.spengergasse.onlinecourseplatform.mongodb.repository.CourseMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Course CRUD operations (MongoDB)
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseMongoRepository courseMongoRepository;

    @GetMapping
    public ResponseEntity<List<CourseDocument>> getAllCourses() {
        log.info("GET /api/courses - fetching all courses");
        List<CourseDocument> courses = courseMongoRepository.findAll();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDocument> getCourseById(@PathVariable String id) {
        log.info("GET /api/courses/{} - fetching course", id);
        return courseMongoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseDocument> createCourse(@RequestBody CourseDocument course) {
        log.info("POST /api/courses - creating course: {}", course.getName());
        CourseDocument saved = courseMongoRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDocument> updateCourse(
            @PathVariable String id,
            @RequestBody CourseDocument course) {
        log.info("PUT /api/courses/{} - updating course", id);

        return courseMongoRepository.findById(id)
                .map(existing -> {
                    course.setId(id);
                    CourseDocument updated = courseMongoRepository.save(course);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        log.info("DELETE /api/courses/{} - deleting course", id);

        if (courseMongoRepository.existsById(id)) {
            courseMongoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDocument>> searchByName(@RequestParam String name) {
        log.info("GET /api/courses/search?name={}", name);
        List<CourseDocument> courses = courseMongoRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(courses);
    }
}
