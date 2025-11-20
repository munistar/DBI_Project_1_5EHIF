package at.spengergasse.onlinecourseplatform.performance;

import at.spengergasse.onlinecourseplatform.domain.*;
import at.spengergasse.onlinecourseplatform.mongodb.document.*;
import at.spengergasse.onlinecourseplatform.mongodb.repository.*;
import at.spengergasse.onlinecourseplatform.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for running performance tests on both SQL and MongoDB databases
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceTestService {

    // SQL Repositories
    private final StudentRepository studentRepository;

    // MongoDB Repositories
    private final StudentMongoRepository studentMongoRepository;

    private final Random random = new Random();

    /**
     * Run all performance tests
     */
    public List<PerformanceResult> runAllTests() {
        List<PerformanceResult> results = new ArrayList<>();

        log.info("=== Starting Performance Tests ===");

        // Write Tests
        results.addAll(testWrites());

        // Read Tests
        results.addAll(testReads());

        // Update Tests
        results.addAll(testUpdates());

        // Delete Tests
        results.addAll(testDeletes());

        log.info("=== Performance Tests Completed ===");
        printSummary(results);

        return results;
    }

    /**
     * Test 1: Write Operations (100, 1000, 100000)
     */
    private List<PerformanceResult> testWrites() {
        List<PerformanceResult> results = new ArrayList<>();
        int[] counts = { 100, 1000, 10000 }; // Reduced max for testing

        for (int count : counts) {
            // SQL Write
            results.add(testSqlWrite(count));

            // MongoDB Write
            results.add(testMongoWrite(count));
        }

        return results;
    }

    @Transactional
    private PerformanceResult testSqlWrite(int count) {
        log.info("Testing SQL write: {} records", count);
        long start = System.currentTimeMillis();

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Student student = Student.builder()
                    .firstName("PerfTest" + i)
                    .lastName("Student")
                    .email("perftest" + i + "@test.com")
                    .dateOfBirth(LocalDate.now().minusYears(20))
                    .registrationDate(LocalDate.now())
                    .build();
            students.add(student);
        }

        studentRepository.saveAll(students);
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("WRITE")
                .database("MySQL")
                .recordCount(count)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Batch insert students")
                .build();
    }

    private PerformanceResult testMongoWrite(int count) {
        log.info("Testing MongoDB write: {} records", count);
        long start = System.currentTimeMillis();

        List<StudentDocument> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StudentDocument student = StudentDocument.builder()
                    .firstName("PerfTest" + i)
                    .lastName("Student")
                    .email("perftest_mongo" + i + "@test.com")
                    .dateOfBirth(LocalDate.now().minusYears(20))
                    .registrationDate(LocalDate.now())
                    .enrollments(new ArrayList<>())
                    .build();
            students.add(student);
        }

        studentMongoRepository.saveAll(students);
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("WRITE")
                .database("MongoDB")
                .recordCount(count)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Batch insert students")
                .build();
    }

    /**
     * Test 2: Read Operations (4 types)
     */
    private List<PerformanceResult> testReads() {
        List<PerformanceResult> results = new ArrayList<>();

        // 1. Find All (no filter)
        results.add(testSqlFindAll());
        results.add(testMongoFindAll());

        // 2. Find with filter
        results.add(testSqlFindByFilter());
        results.add(testMongoFindByFilter());

        // 3. Find with projection (count only)
        results.add(testSqlCount());
        results.add(testMongoCount());

        // 4. Find with sort
        results.add(testSqlFindSorted());
        results.add(testMongoFindSorted());

        return results;
    }

    private PerformanceResult testSqlFindAll() {
        log.info("Testing SQL findAll");
        long start = System.currentTimeMillis();
        List<Student> students = studentRepository.findAll();
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("FIND_ALL")
                .database("MySQL")
                .recordCount(students.size())
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Find all students")
                .build();
    }

    private PerformanceResult testMongoFindAll() {
        log.info("Testing MongoDB findAll");
        long start = System.currentTimeMillis();
        List<StudentDocument> students = studentMongoRepository.findAll();
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("FIND_ALL")
                .database("MongoDB")
                .recordCount(students.size())
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Find all students")
                .build();
    }

    private PerformanceResult testSqlFindByFilter() {
        log.info("Testing SQL find by filter");
        long start = System.currentTimeMillis();
        Optional<Student> student = studentRepository.findByEmail("student0@university.edu");
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("FIND_FILTER")
                .database("MySQL")
                .recordCount(student.isPresent() ? 1 : 0)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Find by email")
                .build();
    }

    private PerformanceResult testMongoFindByFilter() {
        log.info("Testing MongoDB find by filter");
        long start = System.currentTimeMillis();
        Optional<StudentDocument> student = studentMongoRepository.findByEmail("student0@university.edu");
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("FIND_FILTER")
                .database("MongoDB")
                .recordCount(student.isPresent() ? 1 : 0)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Find by email")
                .build();
    }

    private PerformanceResult testSqlCount() {
        log.info("Testing SQL count");
        long start = System.currentTimeMillis();
        long count = studentRepository.count();
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("COUNT")
                .database("MySQL")
                .recordCount((int) count)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Count all students")
                .build();
    }

    private PerformanceResult testMongoCount() {
        log.info("Testing MongoDB count");
        long start = System.currentTimeMillis();
        long count = studentMongoRepository.count();
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("COUNT")
                .database("MongoDB")
                .recordCount((int) count)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Count all students")
                .build();
    }

    private PerformanceResult testSqlFindSorted() {
        log.info("Testing SQL find sorted");
        long start = System.currentTimeMillis();
        List<Student> students = studentRepository.findAll();
        students.sort(Comparator.comparing(Student::getLastName));
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("FIND_SORTED")
                .database("MySQL")
                .recordCount(students.size())
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Find all sorted by lastName")
                .build();
    }

    private PerformanceResult testMongoFindSorted() {
        log.info("Testing MongoDB find sorted");
        long start = System.currentTimeMillis();
        List<StudentDocument> students = studentMongoRepository.findAll();
        students.sort(Comparator.comparing(StudentDocument::getLastName));
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("FIND_SORTED")
                .database("MongoDB")
                .recordCount(students.size())
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Find all sorted by lastName")
                .build();
    }

    /**
     * Test 3: Update Operations
     */
    private List<PerformanceResult> testUpdates() {
        List<PerformanceResult> results = new ArrayList<>();

        results.add(testSqlUpdate());
        results.add(testMongoUpdate());

        return results;
    }

    @Transactional
    private PerformanceResult testSqlUpdate() {
        log.info("Testing SQL update");
        long start = System.currentTimeMillis();

        List<Student> students = studentRepository.findAll();
        int updateCount = Math.min(100, students.size());

        for (int i = 0; i < updateCount; i++) {
            Student student = students.get(i);
            student.setPhoneNumber("+43" + random.nextInt(999999999));
        }

        studentRepository.saveAll(students.subList(0, updateCount));
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("UPDATE")
                .database("MySQL")
                .recordCount(updateCount)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Update phone numbers")
                .build();
    }

    private PerformanceResult testMongoUpdate() {
        log.info("Testing MongoDB update");
        long start = System.currentTimeMillis();

        List<StudentDocument> students = studentMongoRepository.findAll();
        int updateCount = Math.min(100, students.size());

        for (int i = 0; i < updateCount; i++) {
            StudentDocument student = students.get(i);
            student.setPhoneNumber("+43" + random.nextInt(999999999));
        }

        studentMongoRepository.saveAll(students.subList(0, updateCount));
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("UPDATE")
                .database("MongoDB")
                .recordCount(updateCount)
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Update phone numbers")
                .build();
    }

    /**
     * Test 4: Delete Operations
     */
    private List<PerformanceResult> testDeletes() {
        List<PerformanceResult> results = new ArrayList<>();

        results.add(testSqlDelete());
        results.add(testMongoDelete());

        return results;
    }

    @Transactional
    private PerformanceResult testSqlDelete() {
        log.info("Testing SQL delete");

        // Find test records to delete
        List<Student> testStudents = studentRepository.findAll().stream()
                .filter(s -> s.getEmail().startsWith("perftest"))
                .limit(100)
                .toList();

        long start = System.currentTimeMillis();
        studentRepository.deleteAll(testStudents);
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("DELETE")
                .database("MySQL")
                .recordCount(testStudents.size())
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Delete test records")
                .build();
    }

    private PerformanceResult testMongoDelete() {
        log.info("Testing MongoDB delete");

        // Find test records to delete
        List<StudentDocument> testStudents = studentMongoRepository.findAll().stream()
                .filter(s -> s.getEmail().startsWith("perftest"))
                .limit(100)
                .toList();

        long start = System.currentTimeMillis();
        studentMongoRepository.deleteAll(testStudents);
        long duration = System.currentTimeMillis() - start;

        return PerformanceResult.builder()
                .operation("DELETE")
                .database("MongoDB")
                .recordCount(testStudents.size())
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .details("Delete test records")
                .build();
    }

    /**
     * Print summary of results
     */
    private void printSummary(List<PerformanceResult> results) {
        log.info("\n=== PERFORMANCE TEST SUMMARY ===");

        Map<String, List<PerformanceResult>> byOperation = new HashMap<>();
        results.forEach(r -> byOperation.computeIfAbsent(r.getOperation(), k -> new ArrayList<>()).add(r));

        byOperation.forEach((operation, opResults) -> {
            log.info("\n{} Operation:", operation);
            opResults.forEach(r -> log.info("  {} - {} records in {} ms",
                    r.getDatabase(), r.getRecordCount(), r.getDurationMs()));
        });
    }
}
