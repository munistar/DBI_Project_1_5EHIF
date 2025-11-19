package at.spengergasse.onlinecourseplatform.controller;

import at.spengergasse.onlinecourseplatform.mongodb.document.StudentDocument;
import at.spengergasse.onlinecourseplatform.mongodb.repository.StudentMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Student CRUD operations (MongoDB)
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentMongoRepository studentMongoRepository;

    @GetMapping
    public ResponseEntity<List<StudentDocument>> getAllStudents() {
        log.info("GET /api/students - fetching all students");
        List<StudentDocument> students = studentMongoRepository.findAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDocument> getStudentById(@PathVariable String id) {
        log.info("GET /api/students/{} - fetching student", id);
        return studentMongoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDocument> createStudent(@RequestBody StudentDocument student) {
        log.info("POST /api/students - creating student: {}", student.getEmail());
        StudentDocument saved = studentMongoRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDocument> updateStudent(
            @PathVariable String id,
            @RequestBody StudentDocument student) {
        log.info("PUT /api/students/{} - updating student", id);

        return studentMongoRepository.findById(id)
                .map(existing -> {
                    student.setId(id);
                    StudentDocument updated = studentMongoRepository.save(student);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        log.info("DELETE /api/students/{} - deleting student", id);

        if (studentMongoRepository.existsById(id)) {
            studentMongoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<StudentDocument> searchByEmail(@RequestParam String email) {
        log.info("GET /api/students/search?email={}", email);
        return studentMongoRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
