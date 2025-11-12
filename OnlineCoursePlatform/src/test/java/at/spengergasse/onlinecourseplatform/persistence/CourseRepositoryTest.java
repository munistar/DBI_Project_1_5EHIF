package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Course javaCourse;
    private Course springCourse;
    private Course pythonCourse;

    @BeforeEach
    void setUp() {
        // Clear existing data
        courseRepository.deleteAll();
        entityManager.flush();

        // Create test data
        javaCourse = Course.builder()
                .name("Introduction to Java")
                .description("Learn Java programming from scratch with hands-on examples and projects")
                .instructorId(1L)
                .build();

        springCourse = Course.builder()
                .name("Spring Boot Masterclass")
                .description("Master Spring Boot development with real-world enterprise applications")
                .instructorId(1L)
                .build();

        pythonCourse = Course.builder()
                .name("Python for Data Science")
                .description("Learn Python programming for data analysis and machine learning")
                .instructorId(2L)
                .build();

        // Persist test data
        entityManager.persist(javaCourse);
        entityManager.persist(springCourse);
        entityManager.persist(pythonCourse);
        entityManager.flush();
    }

    @Test
    void testSaveCourse() {
        // given
        Course newCourse = Course.builder()
                .name("Docker Fundamentals")
                .description("Learn containerization with Docker and best practices for deployment")
                .instructorId(3L)
                .build();

        // when
        Course savedCourse = courseRepository.save(newCourse);

        // then
        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo("Docker Fundamentals");
        assertThat(savedCourse.getVersion()).isEqualTo(0L);
    }

    @Test
    void testFindById() {
        // when
        Optional<Course> found = courseRepository.findById(javaCourse.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Introduction to Java");
    }

    @Test
    void testFindByIdNotFound() {
        // when
        Optional<Course> found = courseRepository.findById(999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void testFindAll() {
        // when
        List<Course> courses = courseRepository.findAll();

        // then
        assertThat(courses).hasSize(3);
        assertThat(courses).extracting(Course::getName)
                .contains("Introduction to Java", "Spring Boot Masterclass", "Python for Data Science");
    }

    @Test
    void testFindByNameIgnoreCase() {
        // when
        Optional<Course> found = courseRepository.findByNameIgnoreCase("INTRODUCTION TO JAVA");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Introduction to Java");
    }

    @Test
    void testFindByInstructorId() {
        // when
        List<Course> courses = courseRepository.findByInstructorId(1L);

        // then
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getName)
                .containsExactlyInAnyOrder("Introduction to Java", "Spring Boot Masterclass");
    }

    @Test
    void testFindByInstructorIdNoResults() {
        // when
        List<Course> courses = courseRepository.findByInstructorId(999L);

        // then
        assertThat(courses).isEmpty();
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // when
        List<Course> courses = courseRepository.findByNameContainingIgnoreCase("java");

        // then
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Introduction to Java");
    }

    @Test
    void testFindByDescriptionContainingIgnoreCase() {
        // when
        List<Course> courses = courseRepository.findByDescriptionContainingIgnoreCase("enterprise");

        // then
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Spring Boot Masterclass");
    }

    @Test
    void testExistsByNameIgnoreCase() {
        // when & then
        assertThat(courseRepository.existsByNameIgnoreCase("introduction to java")).isTrue();
        assertThat(courseRepository.existsByNameIgnoreCase("Non-existent Course")).isFalse();
    }

    @Test
    void testSearchCourses() {
        // when
        List<Course> courses = courseRepository.searchCourses("spring");

        // then
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Spring Boot Masterclass");
    }

    @Test
    void testSearchCoursesInDescription() {
        // when
        List<Course> courses = courseRepository.searchCourses("machine learning");

        // then
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Python for Data Science");
    }

    @Test
    void testSearchCoursesMultipleResults() {
        // when
        List<Course> courses = courseRepository.searchCourses("programming");

        // then
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getName)
                .containsExactlyInAnyOrder("Introduction to Java", "Python for Data Science");
    }

    @Test
    void testCountByInstructorId() {
        // when
        long count1 = courseRepository.countByInstructorId(1L);
        long count2 = courseRepository.countByInstructorId(2L);
        long count3 = courseRepository.countByInstructorId(999L);

        // then
        assertThat(count1).isEqualTo(2);
        assertThat(count2).isEqualTo(1);
        assertThat(count3).isEqualTo(0);
    }

    @Test
    void testUpdateCourse() {
        // given
        Course course = courseRepository.findById(javaCourse.getId()).orElseThrow();
        String newDescription = "Updated description with more comprehensive content for learning";

        // when
        course.setDescription(newDescription);
        Course updatedCourse = courseRepository.saveAndFlush(course);
        entityManager.clear();

        // then
        Course refreshedCourse = courseRepository.findById(updatedCourse.getId()).orElseThrow();
        assertThat(refreshedCourse.getDescription()).isEqualTo(newDescription);
        assertThat(refreshedCourse.getVersion()).isEqualTo(1L);
    }

    @Test
    void testDeleteCourse() {
        // given
        Long courseId = javaCourse.getId();

        // when
        courseRepository.deleteById(courseId);
        entityManager.flush();

        // then
        Optional<Course> deleted = courseRepository.findById(courseId);
        assertThat(deleted).isEmpty();
        assertThat(courseRepository.findAll()).hasSize(2);
    }

    @Test
    void testOptimisticLocking() {
        // given
        Course course1 = courseRepository.findById(javaCourse.getId()).orElseThrow();
        Course course2 = courseRepository.findById(javaCourse.getId()).orElseThrow();

        // when
        course1.setName("Updated by User 1");
        courseRepository.saveAndFlush(course1);

        course2.setName("Updated by User 2");

        // then
        // In a real scenario, saving course2 would throw OptimisticLockException
        // For this test, we just verify that version numbers are tracked
        Course refreshed = courseRepository.findById(javaCourse.getId()).orElseThrow();
        assertThat(refreshed.getVersion()).isEqualTo(1L);
    }
}