package at.spengergasse.onlinecourseplatform.mongodb.config;

import at.spengergasse.onlinecourseplatform.domain.*;
import at.spengergasse.onlinecourseplatform.mongodb.document.*;
import at.spengergasse.onlinecourseplatform.mongodb.repository.*;
import at.spengergasse.onlinecourseplatform.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Seeds MongoDB with data from the relational database
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MongoDataSeeder {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final StudentMongoRepository studentMongoRepository;
    private final InstructorMongoRepository instructorMongoRepository;
    private final CourseMongoRepository courseMongoRepository;

    // Temporarily disabled to allow application to start
    // @Bean
    // @Order(2) // Run after DatabaseSeeder
    public CommandLineRunner loadMongoData() {
        return args -> {
            log.info("MongoDB seeding is currently disabled");
            /*
             * // Clear existing MongoDB data to ensure fresh seed
             * log.info("Clearing existing MongoDB data...");
             * studentMongoRepository.deleteAll();
             * instructorMongoRepository.deleteAll();
             * courseMongoRepository.deleteAll();
             * 
             * // Check if SQL database has data
             * if (studentRepository.count() == 0) {
             * log.info("SQL database is empty. Skipping MongoDB seeding.");
             * return;
             * }
             * 
             * log.info("Starting MongoDB data seeding from SQL database...");
             * long startTime = System.currentTimeMillis();
             * 
             * // Seed instructors first (no dependencies)
             * Map<Long, String> instructorIdMap = seedInstructors();
             * 
             * // Seed courses (depends on instructors)
             * Map<Long, String> courseIdMap = seedCourses(instructorIdMap);
             * 
             * // Seed students with enrollments (depends on courses)
             * seedStudents(courseIdMap);
             * 
             * long duration = System.currentTimeMillis() - startTime;
             * log.info("MongoDB seeding completed in {} ms", duration);
             * log.info("MongoDB documents: Students={}, Instructors={}, Courses={}",
             * studentMongoRepository.count(),
             * instructorMongoRepository.count(),
             * courseMongoRepository.count());
             */
        };
    }

    private Map<Long, String> seedInstructors() {
        log.info("Seeding instructors to MongoDB...");
        List<Instructor> instructors = instructorRepository.findAll();
        Map<Long, String> idMap = new HashMap<>();

        List<InstructorDocument> documents = instructors.stream()
                .map(instructor -> {
                    InstructorDocument doc = InstructorDocument.builder()
                            .firstName(instructor.getFirstName())
                            .lastName(instructor.getLastName())
                            .email(instructor.getEmail())
                            .department(instructor.getDepartment())
                            .phoneNumber(instructor.getPhoneNumber())
                            .officeLocation(instructor.getOfficeLocation())
                            .hireDate(instructor.getHireDate())
                            .biography(instructor.getBiography())
                            .build();

                    InstructorDocument saved = instructorMongoRepository.save(doc);
                    idMap.put(instructor.getId(), saved.getId());
                    return saved;
                })
                .collect(Collectors.toList());

        log.info("Seeded {} instructors to MongoDB", documents.size());
        return idMap;
    }

    private Map<Long, String> seedCourses(Map<Long, String> instructorIdMap) {
        log.info("Seeding courses to MongoDB...");
        List<Course> courses = courseRepository.findAll();
        Map<Long, String> idMap = new HashMap<>();

        List<CourseDocument> documents = courses.stream()
                .map(course -> {
                    CourseDocument.CourseDocumentBuilder builder = CourseDocument.builder()
                            .name(course.getName())
                            .description(course.getDescription());

                    // Add embedded instructor info
                    if (course.getInstructor() != null) {
                        Instructor instructor = course.getInstructor();
                        CourseDocument.InstructorInfo instructorInfo = CourseDocument.InstructorInfo.builder()
                                .id(instructorIdMap.get(instructor.getId()))
                                .firstName(instructor.getFirstName())
                                .lastName(instructor.getLastName())
                                .email(instructor.getEmail())
                                .department(instructor.getDepartment())
                                .build();
                        builder.instructor(instructorInfo);
                    }

                    CourseDocument doc = builder.build();
                    CourseDocument saved = courseMongoRepository.save(doc);
                    idMap.put(course.getId(), saved.getId());
                    return saved;
                })
                .collect(Collectors.toList());

        log.info("Seeded {} courses to MongoDB", documents.size());
        return idMap;
    }

    private void seedStudents(Map<Long, String> courseIdMap) {
        log.info("Seeding students to MongoDB...");
        List<Student> students = studentRepository.findAll();
        List<Enrollment> allEnrollments = enrollmentRepository.findAll();

        // Group enrollments by student
        Map<Long, List<Enrollment>> enrollmentsByStudent = allEnrollments.stream()
                .collect(Collectors.groupingBy(e -> e.getStudent().getId()));

        List<StudentDocument> documents = students.stream()
                .map(student -> {
                    StudentDocument.StudentDocumentBuilder builder = StudentDocument.builder()
                            .firstName(student.getFirstName())
                            .lastName(student.getLastName())
                            .email(student.getEmail())
                            .dateOfBirth(student.getDateOfBirth())
                            .phoneNumber(student.getPhoneNumber())
                            .address(student.getAddress())
                            .registrationDate(student.getRegistrationDate());

                    // Add embedded enrollments
                    List<Enrollment> studentEnrollments = enrollmentsByStudent.getOrDefault(student.getId(),
                            Collections.emptyList());
                    List<StudentDocument.EnrollmentInfo> enrollmentInfos = studentEnrollments.stream()
                            .map(enrollment -> {
                                Course course = enrollment.getCourse();
                                return StudentDocument.EnrollmentInfo.builder()
                                        .enrollmentId(enrollment.getId().toString())
                                        .courseId(courseIdMap.get(course.getId()))
                                        .courseName(course.getName())
                                        .courseDescription(course.getDescription())
                                        .grade(enrollment.getGrade())
                                        .enrollmentDate(LocalDate.now()) // Default to now
                                        .build();
                            })
                            .collect(Collectors.toList());

                    builder.enrollments(enrollmentInfos);

                    return studentMongoRepository.save(builder.build());
                })
                .collect(Collectors.toList());

        // Update courses with enrolled students
        updateCoursesWithStudents(courseIdMap);

        log.info("Seeded {} students to MongoDB", documents.size());
    }

    private void updateCoursesWithStudents(Map<Long, String> courseIdMap) {
        log.info("Updating courses with enrolled students...");
        List<Enrollment> allEnrollments = enrollmentRepository.findAll();

        // Group enrollments by course
        Map<Long, List<Enrollment>> enrollmentsByCourse = allEnrollments.stream()
                .collect(Collectors.groupingBy(e -> e.getCourse().getId()));

        // Get all student documents for quick lookup
        Map<String, StudentDocument> studentDocsByEmail = studentMongoRepository.findAll().stream()
                .collect(Collectors.toMap(StudentDocument::getEmail, s -> s));

        enrollmentsByCourse.forEach((courseId, enrollments) -> {
            String mongoCourseId = courseIdMap.get(courseId);
            if (mongoCourseId != null) {
                courseMongoRepository.findById(mongoCourseId).ifPresent(courseDoc -> {
                    List<CourseDocument.StudentInfo> studentInfos = enrollments.stream()
                            .map(enrollment -> {
                                Student student = enrollment.getStudent();
                                StudentDocument studentDoc = studentDocsByEmail.get(student.getEmail());

                                return CourseDocument.StudentInfo.builder()
                                        .studentId(studentDoc != null ? studentDoc.getId() : null)
                                        .firstName(student.getFirstName())
                                        .lastName(student.getLastName())
                                        .email(student.getEmail())
                                        .grade(enrollment.getGrade())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    courseDoc.setEnrolledStudents(studentInfos);
                    courseMongoRepository.save(courseDoc);
                });
            }
        });

        log.info("Updated courses with enrolled students");
    }
}
