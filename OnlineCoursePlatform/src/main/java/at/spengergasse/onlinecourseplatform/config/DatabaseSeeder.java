package at.spengergasse.onlinecourseplatform.config;

import at.spengergasse.onlinecourseplatform.domain.Course;
import at.spengergasse.onlinecourseplatform.domain.Enrollment;
import at.spengergasse.onlinecourseplatform.domain.Instructor;
import at.spengergasse.onlinecourseplatform.domain.Student;
import at.spengergasse.onlinecourseplatform.persistence.CourseRepository;
import at.spengergasse.onlinecourseplatform.persistence.EnrollmentRepository;
import at.spengergasse.onlinecourseplatform.persistence.InstructorRepository;
import at.spengergasse.onlinecourseplatform.persistence.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Value("${seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${seed.students.count:100}")
    private int studentCount;

    @Value("${seed.instructors.count:10}")
    private int instructorCount;

    @Value("${seed.courses.count:20}")
    private int courseCount;

    @Value("${seed.enrollments.count:200}")
    private int enrollmentCount;

    private final Random random = new Random();

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // Check if seeding is enabled
            if (!seedEnabled) {
                log.info("Seed data generation is disabled. Set seed.enabled=true to enable.");
                return;
            }

            // Check if data already exists
            if (studentRepository.count() > 0) {
                log.info("Database already contains data. Skipping seeding.");
                return;
            }

            // Validate counts
            validateCounts();

            log.info("Starting database seeding with configuration:");
            log.info("  Students: {}, Instructors: {}, Courses: {}, Enrollments: {}",
                    studentCount, instructorCount, courseCount, enrollmentCount);
            long startTime = System.currentTimeMillis();

            // Generate data with configured counts
            List<Student> students = generateStudents(studentCount);
            List<Instructor> instructors = generateInstructors(instructorCount);
            List<Course> courses = generateCourses(courseCount, instructors);
            List<Enrollment> enrollments = generateEnrollments(enrollmentCount, students, courses);

            log.info("Data generation completed in {} ms", System.currentTimeMillis() - startTime);
            log.info("Total records: {} (Students: {}, Instructors: {}, Courses: {}, Enrollments: {})",
                    students.size() + instructors.size() + courses.size() + enrollments.size(),
                    students.size(), instructors.size(), courses.size(), enrollments.size());
        };
    }

    @Transactional
    protected List<Student> generateStudents(int count) {
        log.info("Generating {} students...", count);
        List<Student> students = new ArrayList<>();
        int batchSize = 1000;

        String[] firstNames = { "Emma", "Liam", "Olivia", "Noah", "Ava", "Ethan", "Sophia", "Mason",
                "Isabella", "William", "Mia", "James", "Charlotte", "Benjamin", "Amelia" };
        String[] lastNames = { "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
                "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson" };
        String[] cities = { "Vienna", "Salzburg", "Graz", "Innsbruck", "Linz", "Klagenfurt", "Villach", "Wels" };

        for (int i = 0; i < count; i++) {
            Student student = Student.builder()
                    .firstName(firstNames[random.nextInt(firstNames.length)] + i)
                    .lastName(lastNames[random.nextInt(lastNames.length)])
                    .email("student" + i + "@university.edu")
                    .dateOfBirth(LocalDate.now().minusYears(18 + random.nextInt(25)))
                    .phoneNumber("+43" + (600000000 + random.nextInt(99999999)))
                    .address(cities[random.nextInt(cities.length)] + " Street " + (i % 1000))
                    .registrationDate(LocalDate.now().minusDays(random.nextInt(1095)))
                    .build();

            students.add(student);

            // Save in batches
            if (i > 0 && i % batchSize == 0) {
                studentRepository.saveAll(students);
                students.clear();
                log.info("Saved {} students", i);
            }
        }

        // Save remaining
        if (!students.isEmpty()) {
            studentRepository.saveAll(students);
        }

        log.info("Completed generating {} students", count);
        return new ArrayList<>(studentRepository.findAll());
    }

    @Transactional
    protected List<Instructor> generateInstructors(int count) {
        log.info("Generating {} instructors...", count);
        List<Instructor> instructors = new ArrayList<>();
        String[] departments = { "Computer Science", "Mathematics", "Physics", "Chemistry",
                "Biology", "Engineering", "Business", "Arts", "History", "Literature" };
        String[] firstNames = { "John", "Sarah", "Michael", "Emily", "David", "Jessica", "Robert",
                "Lisa", "Daniel", "Jennifer", "Thomas", "Maria", "Christopher", "Anna" };
        String[] lastNames = { "Anderson", "Taylor", "Thomas", "Moore", "Jackson", "Martin", "Lee",
                "Perez", "Thompson", "White", "Harris", "Clark", "Lewis", "Walker" };
        String[] buildings = { "Building A", "Building B", "Building C", "Main Campus", "North Wing", "South Wing" };

        for (int i = 0; i < count; i++) {
            Instructor instructor = Instructor.builder()
                    .firstName(firstNames[random.nextInt(firstNames.length)])
                    .lastName(lastNames[random.nextInt(lastNames.length)] + i)
                    .email("instructor" + i + "@university.edu")
                    .department(departments[random.nextInt(departments.length)])
                    .phoneNumber("+43" + (660000000 + random.nextInt(99999999)))
                    .officeLocation(buildings[random.nextInt(buildings.length)] + " Room " + (100 + i % 900))
                    .hireDate(LocalDate.now().minusYears(random.nextInt(20)))
                    .biography("Experienced professor with " + (1 + random.nextInt(25)) + " years in academia. " +
                            "Specializes in various aspects of " + departments[i % departments.length] + ".")
                    .build();

            instructors.add(instructor);
        }

        instructorRepository.saveAll(instructors);
        log.info("Completed generating {} instructors", count);
        return new ArrayList<>(instructorRepository.findAll());
    }

    @Transactional
    protected List<Course> generateCourses(int count, List<Instructor> instructors) {
        log.info("Generating {} courses...", count);
        List<Course> courses = new ArrayList<>();
        String[] subjects = { "Introduction to", "Advanced", "Fundamentals of", "Applied",
                "Theoretical", "Practical", "Modern", "Classical" };
        String[] topics = { "Programming", "Algorithms", "Data Structures", "Web Development",
                "Machine Learning", "Database Systems", "Networks", "Security",
                "Software Engineering", "AI", "Cloud Computing", "Mobile Development" };

        for (int i = 0; i < count; i++) {
            Course course = new Course();
            course.setName(subjects[random.nextInt(subjects.length)] + " " +
                    topics[random.nextInt(topics.length)] + " " + (i + 1));
            course.setDescription("This is a comprehensive course covering various aspects of the subject. " +
                    "Students will learn through lectures, assignments, and projects. " +
                    "Course code: CS-" + (1000 + i));

            // Assign random instructor
            Instructor instructor = instructors.get(random.nextInt(instructors.size()));
            course.setInstructor(instructor);
            course.setVersion(0L);

            courses.add(course);
        }

        courseRepository.saveAll(courses);
        log.info("Completed generating {} courses", count);
        return new ArrayList<>(courseRepository.findAll());
    }

    @Transactional
    protected List<Enrollment> generateEnrollments(int count, List<Student> students, List<Course> courses) {
        log.info("Generating {} enrollments...", count);
        List<Enrollment> enrollments = new ArrayList<>();
        int batchSize = 1000;

        for (int i = 0; i < count; i++) {
            Enrollment enrollment = new Enrollment();

            // Random student and course
            Student student = students.get(random.nextInt(students.size()));
            Course course = courses.get(random.nextInt(courses.size()));

            enrollment.setStudent(student);
            enrollment.setCourse(course);

            // Random grade between 1.0 and 5.0 (or null for in-progress courses)
            if (random.nextBoolean()) {
                enrollment.setGrade(1.0 + (random.nextDouble() * 4.0));
            }

            enrollment.setVersion(0L);
            enrollments.add(enrollment);

            // Save in batches
            if (i > 0 && i % batchSize == 0) {
                enrollmentRepository.saveAll(enrollments);
                enrollments.clear();
                log.info("Saved {} enrollments", i);
            }
        }

        // Save remaining
        if (!enrollments.isEmpty()) {
            enrollmentRepository.saveAll(enrollments);
        }

        log.info("Completed generating {} enrollments", count);
        return new ArrayList<>(enrollmentRepository.findAll());
    }

    /**
     * Validates that seed counts are within acceptable range (10 - 100,000)
     */
    private void validateCounts() {
        validateCount("students", studentCount);
        validateCount("instructors", instructorCount);
        validateCount("courses", courseCount);
        validateCount("enrollments", enrollmentCount);
    }

    private void validateCount(String entity, int count) {
        if (count < 10 || count > 100000) {
            throw new IllegalArgumentException(
                    String.format("Seed count for %s must be between 10 and 100,000. Got: %d", entity, count));
        }
    }
}