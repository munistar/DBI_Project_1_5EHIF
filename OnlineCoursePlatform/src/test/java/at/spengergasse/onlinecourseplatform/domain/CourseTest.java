package at.spengergasse.onlinecourseplatform.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CourseTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCourse() {
        // given
        Course course = Course.builder()
                .name("Introduction to Java")
                .description("Learn Java programming from scratch with hands-on examples")
                .instructorId(1L)
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void testCourseNameCannotBeBlank() {
        // given
        Course course = Course.builder()
                .name("")
                .description("A comprehensive course on Java programming")
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "Course name is required",
                        "Course name must be between 3 and 200 characters"
                );
    }

    @Test
    void testCourseNameCannotBeNull() {
        // given
        Course course = Course.builder()
                .name(null)
                .description("A comprehensive course on Java programming")
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Course name is required");
    }

    @Test
    void testCourseNameTooShort() {
        // given
        Course course = Course.builder()
                .name("AB")
                .description("A comprehensive course on Java programming")
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("between 3 and 200 characters");
    }

    @Test
    void testCourseNameTooLong() {
        // given
        String longName = "A".repeat(201);
        Course course = Course.builder()
                .name(longName)
                .description("A comprehensive course on Java programming")
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("between 3 and 200 characters");
    }

    @Test
    void testDescriptionCannotBeBlank() {
        // given
        Course course = Course.builder()
                .name("Introduction to Java")
                .description("")
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "Course description is required",
                        "Course description must be between 10 and 2000 characters"
                );
    }

    @Test
    void testDescriptionCannotBeNull() {
        // given
        Course course = Course.builder()
                .name("Introduction to Java")
                .description(null)
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Course description is required");
    }

    @Test
    void testDescriptionTooShort() {
        // given
        Course course = Course.builder()
                .name("Introduction to Java")
                .description("Too short")
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("between 10 and 2000 characters");
    }

    @Test
    void testDescriptionTooLong() {
        // given
        String longDescription = "A".repeat(2001);
        Course course = Course.builder()
                .name("Introduction to Java")
                .description(longDescription)
                .build();

        // when
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("between 10 and 2000 characters");
    }

    @Test
    void testBuilderPattern() {
        // given & when
        Course course = Course.builder()
                .id(1L)
                .name("Spring Boot Masterclass")
                .description("Master Spring Boot development with real-world projects")
                .instructorId(5L)
                .version(0L)
                .build();

        // then
        assertThat(course.getId()).isEqualTo(1L);
        assertThat(course.getName()).isEqualTo("Spring Boot Masterclass");
        assertThat(course.getDescription()).isEqualTo("Master Spring Boot development with real-world projects");
        assertThat(course.getInstructorId()).isEqualTo(5L);
        assertThat(course.getVersion()).isEqualTo(0L);
    }

    @Test
    void testEqualsWithSameId() {
        // given
        Course course1 = Course.builder().id(1L).name("Course A").description("Description A").build();
        Course course2 = Course.builder().id(1L).name("Course B").description("Description B").build();

        // when & then
        assertThat(course1).isEqualTo(course2);
    }

    @Test
    void testEqualsWithDifferentId() {
        // given
        Course course1 = Course.builder().id(1L).name("Course A").description("Description A").build();
        Course course2 = Course.builder().id(2L).name("Course A").description("Description A").build();

        // when & then
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    void testEqualsWithNull() {
        // given
        Course course = Course.builder().id(1L).name("Course A").description("Description A").build();

        // when & then
        assertThat(course).isNotEqualTo(null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // given
        Course course = Course.builder().id(1L).name("Course A").description("Description A").build();
        String notACourse = "Not a course";

        // when & then
        assertThat(course).isNotEqualTo(notACourse);
    }

    @Test
    void testEqualsWithSameReference() {
        // given
        Course course = Course.builder().id(1L).name("Course A").description("Description A").build();

        // when & then
        assertThat(course).isEqualTo(course);
    }

    @Test
    void testEqualsWithNullIds() {
        // given
        Course course1 = Course.builder().name("Course A").description("Description A").build();
        Course course2 = Course.builder().name("Course B").description("Description B").build();

        // when & then - both have null IDs, should be equal
        assertThat(course1).isEqualTo(course2);
    }

    @Test
    void testHashCodeWithSameId() {
        // given
        Course course1 = Course.builder().id(1L).name("Course A").description("Description A").build();
        Course course2 = Course.builder().id(1L).name("Course B").description("Description B").build();

        // when & then
        assertThat(course1.hashCode()).isEqualTo(course2.hashCode());
    }

    @Test
    void testToString() {
        // given
        Course course = Course.builder()
                .id(1L)
                .name("Java Basics")
                .description("Learn the basics of Java")
                .instructorId(10L)
                .version(0L)
                .build();

        // when
        String result = course.toString();

        // then
        assertThat(result).contains("id=1");
        assertThat(result).contains("name='Java Basics'");
        assertThat(result).contains("instructorId=10");
        assertThat(result).contains("version=0");
    }
}