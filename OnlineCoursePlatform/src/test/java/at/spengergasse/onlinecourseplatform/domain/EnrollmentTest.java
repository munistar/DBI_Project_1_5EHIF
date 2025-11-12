package at.spengergasse.onlinecourseplatform.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EnrollmentTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidEnrollment() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(85.5)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidEnrollmentWithoutGrade() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void testGradeMinimumBoundary() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(0.0)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void testGradeMaximumBoundary() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(100.0)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void testGradeBelowMinimum() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(-0.1)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Grade must be at least 0.0");
    }

    @Test
    void testGradeAboveMaximum() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(100.1)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Grade must not exceed 100.0");
    }

    @Test
    void testGradeWayBelowMinimum() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(-50.0)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Grade must be at least 0.0");
    }

    @Test
    void testGradeWayAboveMaximum() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(150.0)
                .build();

        // when
        Set<ConstraintViolation<Enrollment>> violations = validator.validate(enrollment);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Grade must not exceed 100.0");
    }

    @Test
    void testBuilderPattern() {
        // given & when
        Enrollment enrollment = Enrollment.builder()
                .id(1L)
                .studentId(5L)
                .courseId(10L)
                .grade(92.5)
                .version(0L)
                .build();

        // then
        assertThat(enrollment.getId()).isEqualTo(1L);
        assertThat(enrollment.getStudentId()).isEqualTo(5L);
        assertThat(enrollment.getCourseId()).isEqualTo(10L);
        assertThat(enrollment.getGrade()).isEqualTo(92.5);
        assertThat(enrollment.getVersion()).isEqualTo(0L);
    }

    @Test
    void testNoArgsConstructor() {
        // when
        Enrollment enrollment = new Enrollment();

        // then
        assertThat(enrollment).isNotNull();
        assertThat(enrollment.getId()).isNull();
        assertThat(enrollment.getStudentId()).isNull();
        assertThat(enrollment.getCourseId()).isNull();
        assertThat(enrollment.getGrade()).isNull();
        assertThat(enrollment.getVersion()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        // when
        Enrollment enrollment = new Enrollment(1L, 2L, 3L, 88.0, 0L);

        // then
        assertThat(enrollment.getId()).isEqualTo(1L);
        assertThat(enrollment.getStudentId()).isEqualTo(2L);
        assertThat(enrollment.getCourseId()).isEqualTo(3L);
        assertThat(enrollment.getGrade()).isEqualTo(88.0);
        assertThat(enrollment.getVersion()).isEqualTo(0L);
    }

    @Test
    void testSetters() {
        // given
        Enrollment enrollment = new Enrollment();

        // when
        enrollment.setId(1L);
        enrollment.setStudentId(2L);
        enrollment.setCourseId(3L);
        enrollment.setGrade(95.0);
        enrollment.setVersion(1L);

        // then
        assertThat(enrollment.getId()).isEqualTo(1L);
        assertThat(enrollment.getStudentId()).isEqualTo(2L);
        assertThat(enrollment.getCourseId()).isEqualTo(3L);
        assertThat(enrollment.getGrade()).isEqualTo(95.0);
        assertThat(enrollment.getVersion()).isEqualTo(1L);
    }

    @Test
    void testEqualsWithSameId() {
        // given
        Enrollment enrollment1 = Enrollment.builder().id(1L).studentId(1L).courseId(1L).build();
        Enrollment enrollment2 = Enrollment.builder().id(1L).studentId(2L).courseId(2L).build();

        // when & then
        assertThat(enrollment1).isEqualTo(enrollment2);
    }

    @Test
    void testEqualsWithDifferentId() {
        // given
        Enrollment enrollment1 = Enrollment.builder().id(1L).studentId(1L).courseId(1L).build();
        Enrollment enrollment2 = Enrollment.builder().id(2L).studentId(1L).courseId(1L).build();

        // when & then
        assertThat(enrollment1).isNotEqualTo(enrollment2);
    }

    @Test
    void testEqualsWithNull() {
        // given
        Enrollment enrollment = Enrollment.builder().id(1L).studentId(1L).courseId(1L).build();

        // when & then
        assertThat(enrollment).isNotEqualTo(null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // given
        Enrollment enrollment = Enrollment.builder().id(1L).studentId(1L).courseId(1L).build();
        String notAnEnrollment = "Not an enrollment";

        // when & then
        assertThat(enrollment).isNotEqualTo(notAnEnrollment);
    }

    @Test
    void testEqualsWithSameReference() {
        // given
        Enrollment enrollment = Enrollment.builder().id(1L).studentId(1L).courseId(1L).build();

        // when & then
        assertThat(enrollment).isEqualTo(enrollment);
    }

    @Test
    void testEqualsWithNullIds() {
        // given
        Enrollment enrollment1 = Enrollment.builder().studentId(1L).courseId(1L).build();
        Enrollment enrollment2 = Enrollment.builder().studentId(2L).courseId(2L).build();

        // when & then - both have null IDs, should be equal
        assertThat(enrollment1).isEqualTo(enrollment2);
    }

    @Test
    void testEqualsWithOneNullId() {
        // given
        Enrollment enrollment1 = Enrollment.builder().id(1L).studentId(1L).courseId(1L).build();
        Enrollment enrollment2 = Enrollment.builder().studentId(2L).courseId(2L).build();

        // when & then
        assertThat(enrollment1).isNotEqualTo(enrollment2);
    }

    @Test
    void testHashCodeWithSameId() {
        // given
        Enrollment enrollment1 = Enrollment.builder().id(1L).studentId(1L).courseId(1L).build();
        Enrollment enrollment2 = Enrollment.builder().id(1L).studentId(2L).courseId(2L).build();

        // when & then
        assertThat(enrollment1.hashCode()).isEqualTo(enrollment2.hashCode());
    }

    @Test
    void testHashCodeWithNullId() {
        // given
        Enrollment enrollment1 = Enrollment.builder().studentId(1L).courseId(1L).build();
        Enrollment enrollment2 = Enrollment.builder().studentId(2L).courseId(2L).build();

        // when & then
        assertThat(enrollment1.hashCode()).isEqualTo(enrollment2.hashCode());
    }

    @Test
    void testToString() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .id(1L)
                .studentId(5L)
                .courseId(10L)
                .grade(87.5)
                .version(0L)
                .build();

        // when
        String result = enrollment.toString();

        // then
        assertThat(result).contains("id=1");
        assertThat(result).contains("studentId=5");
        assertThat(result).contains("courseId=10");
        assertThat(result).contains("grade=87.5");
        assertThat(result).contains("version=0");
    }

    @Test
    void testToStringWithNullGrade() {
        // given
        Enrollment enrollment = Enrollment.builder()
                .id(1L)
                .studentId(5L)
                .courseId(10L)
                .version(0L)
                .build();

        // when
        String result = enrollment.toString();

        // then
        assertThat(result).contains("id=1");
        assertThat(result).contains("studentId=5");
        assertThat(result).contains("courseId=10");
        assertThat(result).contains("grade=null");
        assertThat(result).contains("version=0");
    }

    @Test
    void testToStringWithAllNullValues() {
        // given
        Enrollment enrollment = new Enrollment();

        // when
        String result = enrollment.toString();

        // then
        assertThat(result).contains("Enrollment{");
        assertThat(result).contains("id=null");
        assertThat(result).contains("studentId=null");
        assertThat(result).contains("courseId=null");
        assertThat(result).contains("grade=null");
        assertThat(result).contains("version=null");
    }
}
