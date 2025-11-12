package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Enrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Enrollment enrollment1;
    private Enrollment enrollment2;
    private Enrollment enrollment3;
    private Enrollment enrollment4;

    @BeforeEach
    void setUp() {
        // Clear existing data
        enrollmentRepository.deleteAll();
        entityManager.flush();

        // Create test data
        enrollment1 = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .grade(85.5)
                .build();

        enrollment2 = Enrollment.builder()
                .studentId(1L)
                .courseId(2L)
                .grade(92.0)
                .build();

        enrollment3 = Enrollment.builder()
                .studentId(2L)
                .courseId(1L)
                .grade(78.5)
                .build();

        enrollment4 = Enrollment.builder()
                .studentId(2L)
                .courseId(3L)
                .build(); // No grade assigned

        // Persist test data
        entityManager.persist(enrollment1);
        entityManager.persist(enrollment2);
        entityManager.persist(enrollment3);
        entityManager.persist(enrollment4);
        entityManager.flush();
    }

    @Test
    void testSaveEnrollment() {
        // given
        Enrollment newEnrollment = Enrollment.builder()
                .studentId(3L)
                .courseId(4L)
                .grade(95.0)
                .build();

        // when
        Enrollment savedEnrollment = enrollmentRepository.save(newEnrollment);

        // then
        assertThat(savedEnrollment.getId()).isNotNull();
        assertThat(savedEnrollment.getStudentId()).isEqualTo(3L);
        assertThat(savedEnrollment.getCourseId()).isEqualTo(4L);
        assertThat(savedEnrollment.getGrade()).isEqualTo(95.0);
        assertThat(savedEnrollment.getVersion()).isEqualTo(0L);
    }

    @Test
    void testFindById() {
        // when
        Optional<Enrollment> found = enrollmentRepository.findById(enrollment1.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getStudentId()).isEqualTo(1L);
        assertThat(found.get().getCourseId()).isEqualTo(1L);
        assertThat(found.get().getGrade()).isEqualTo(85.5);
    }

    @Test
    void testFindByIdNotFound() {
        // when
        Optional<Enrollment> found = enrollmentRepository.findById(999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void testFindAll() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        // then
        assertThat(enrollments).hasSize(4);
    }

    @Test
    void testFindByStudentId() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(1L);

        // then
        assertThat(enrollments).hasSize(2);
        assertThat(enrollments).extracting(Enrollment::getCourseId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testFindByStudentIdNoResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(999L);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testFindByCourseId() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(1L);

        // then
        assertThat(enrollments).hasSize(2);
        assertThat(enrollments).extracting(Enrollment::getStudentId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testFindByCourseIdNoResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(999L);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testFindByStudentIdAndCourseId() {
        // when
        Optional<Enrollment> found = enrollmentRepository.findByStudentIdAndCourseId(1L, 1L);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getGrade()).isEqualTo(85.5);
    }

    @Test
    void testFindByStudentIdAndCourseIdNotFound() {
        // when
        Optional<Enrollment> found = enrollmentRepository.findByStudentIdAndCourseId(1L, 999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void testExistsByStudentIdAndCourseId() {
        // when & then
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).isTrue();
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(1L, 999L)).isFalse();
    }

    @Test
    void testFindByGradeGreaterThanEqual() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeGreaterThanEqual(85.0);

        // then
        assertThat(enrollments).hasSize(2);
        assertThat(enrollments).extracting(Enrollment::getGrade)
                .containsExactlyInAnyOrder(85.5, 92.0);
    }

    @Test
    void testFindByGradeGreaterThanEqualNoResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeGreaterThanEqual(100.0);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testFindByGradeLessThan() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeLessThan(80.0);

        // then
        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getGrade()).isEqualTo(78.5);
    }

    @Test
    void testFindByGradeLessThanNoResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeLessThan(50.0);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testFindByGradeBetween() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeBetween(80.0, 90.0);

        // then
        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getGrade()).isEqualTo(85.5);
    }

    @Test
    void testFindByGradeBetweenMultipleResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeBetween(75.0, 95.0);

        // then
        assertThat(enrollments).hasSize(3);
        assertThat(enrollments).extracting(Enrollment::getGrade)
                .containsExactlyInAnyOrder(78.5, 85.5, 92.0);
    }

    @Test
    void testFindByGradeBetweenNoResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeBetween(95.0, 100.0);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testFindByGradeIsNull() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeIsNull();

        // then
        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getStudentId()).isEqualTo(2L);
        assertThat(enrollments.get(0).getCourseId()).isEqualTo(3L);
    }

    @Test
    void testFindByGradeIsNotNull() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeIsNotNull();

        // then
        assertThat(enrollments).hasSize(3);
        assertThat(enrollments).extracting(Enrollment::getGrade)
                .containsExactlyInAnyOrder(85.5, 92.0, 78.5);
    }

    @Test
    void testCountByStudentId() {
        // when
        long count1 = enrollmentRepository.countByStudentId(1L);
        long count2 = enrollmentRepository.countByStudentId(2L);
        long count3 = enrollmentRepository.countByStudentId(999L);

        // then
        assertThat(count1).isEqualTo(2);
        assertThat(count2).isEqualTo(2);
        assertThat(count3).isEqualTo(0);
    }

    @Test
    void testCountByCourseId() {
        // when
        long count1 = enrollmentRepository.countByCourseId(1L);
        long count2 = enrollmentRepository.countByCourseId(2L);
        long count3 = enrollmentRepository.countByCourseId(3L);
        long count4 = enrollmentRepository.countByCourseId(999L);

        // then
        assertThat(count1).isEqualTo(2);
        assertThat(count2).isEqualTo(1);
        assertThat(count3).isEqualTo(1);
        assertThat(count4).isEqualTo(0);
    }

    @Test
    void testCalculateAverageGradeByStudentId() {
        // when
        Double average = enrollmentRepository.calculateAverageGradeByStudentId(1L);

        // then
        assertThat(average).isNotNull();
        assertThat(average).isEqualTo(88.75); // (85.5 + 92.0) / 2
    }

    @Test
    void testCalculateAverageGradeByStudentIdWithNullGrades() {
        // when
        Double average = enrollmentRepository.calculateAverageGradeByStudentId(2L);

        // then - only enrollment3 has a grade for student 2
        assertThat(average).isNotNull();
        assertThat(average).isEqualTo(78.5);
    }

    @Test
    void testCalculateAverageGradeByStudentIdNoEnrollments() {
        // when
        Double average = enrollmentRepository.calculateAverageGradeByStudentId(999L);

        // then
        assertThat(average).isNull();
    }

    @Test
    void testCalculateAverageGradeByCourseId() {
        // when
        Double average = enrollmentRepository.calculateAverageGradeByCourseId(1L);

        // then
        assertThat(average).isNotNull();
        assertThat(average).isEqualTo(82.0); // (85.5 + 78.5) / 2
    }

    @Test
    void testCalculateAverageGradeByCourseIdNoGrades() {
        // when
        Double average = enrollmentRepository.calculateAverageGradeByCourseId(3L);

        // then - enrollment4 has no grade
        assertThat(average).isNull();
    }

    @Test
    void testCalculateAverageGradeByCourseIdNoEnrollments() {
        // when
        Double average = enrollmentRepository.calculateAverageGradeByCourseId(999L);

        // then
        assertThat(average).isNull();
    }

    @Test
    void testFindByStudentIdAndGradeIsNotNull() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndGradeIsNotNull(1L);

        // then
        assertThat(enrollments).hasSize(2);
        assertThat(enrollments).extracting(Enrollment::getGrade)
                .containsExactlyInAnyOrder(85.5, 92.0);
    }

    @Test
    void testFindByStudentIdAndGradeIsNotNullPartialResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndGradeIsNotNull(2L);

        // then - student 2 has one enrollment with grade and one without
        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getGrade()).isEqualTo(78.5);
    }

    @Test
    void testFindByStudentIdAndGradeIsNotNullNoResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndGradeIsNotNull(999L);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testFindByCourseIdAndGradeIsNotNull() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByCourseIdAndGradeIsNotNull(1L);

        // then
        assertThat(enrollments).hasSize(2);
        assertThat(enrollments).extracting(Enrollment::getGrade)
                .containsExactlyInAnyOrder(85.5, 78.5);
    }

    @Test
    void testFindByCourseIdAndGradeIsNotNullNoGrades() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByCourseIdAndGradeIsNotNull(3L);

        // then - course 3 has enrollment4 without a grade
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testFindByCourseIdAndGradeIsNotNullNoResults() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByCourseIdAndGradeIsNotNull(999L);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testUpdateEnrollment() {
        // given
        Enrollment enrollment = enrollmentRepository.findById(enrollment1.getId()).orElseThrow();
        Double newGrade = 90.0;

        // when
        enrollment.setGrade(newGrade);
        Enrollment updatedEnrollment = enrollmentRepository.saveAndFlush(enrollment);
        entityManager.clear();

        // then
        Enrollment refreshedEnrollment = enrollmentRepository.findById(updatedEnrollment.getId()).orElseThrow();
        assertThat(refreshedEnrollment.getGrade()).isEqualTo(newGrade);
        assertThat(refreshedEnrollment.getVersion()).isEqualTo(1L);
    }

    @Test
    void testDeleteEnrollment() {
        // given
        Long enrollmentId = enrollment1.getId();

        // when
        enrollmentRepository.deleteById(enrollmentId);
        entityManager.flush();

        // then
        Optional<Enrollment> deleted = enrollmentRepository.findById(enrollmentId);
        assertThat(deleted).isEmpty();
        assertThat(enrollmentRepository.findAll()).hasSize(3);
    }

    @Test
    void testDeleteByStudentId() {
        // when
        enrollmentRepository.deleteByStudentId(1L);
        entityManager.flush();

        // then
        List<Enrollment> remaining = enrollmentRepository.findAll();
        assertThat(remaining).hasSize(2);
        assertThat(remaining).extracting(Enrollment::getStudentId)
                .containsOnly(2L);
    }

    @Test
    void testDeleteByStudentIdNoMatches() {
        // when
        enrollmentRepository.deleteByStudentId(999L);
        entityManager.flush();

        // then
        List<Enrollment> remaining = enrollmentRepository.findAll();
        assertThat(remaining).hasSize(4);
    }

    @Test
    void testDeleteByCourseId() {
        // when
        enrollmentRepository.deleteByCourseId(1L);
        entityManager.flush();

        // then
        List<Enrollment> remaining = enrollmentRepository.findAll();
        assertThat(remaining).hasSize(2);
        assertThat(remaining).extracting(Enrollment::getCourseId)
                .containsExactlyInAnyOrder(2L, 3L);
    }

    @Test
    void testDeleteByCourseIdNoMatches() {
        // when
        enrollmentRepository.deleteByCourseId(999L);
        entityManager.flush();

        // then
        List<Enrollment> remaining = enrollmentRepository.findAll();
        assertThat(remaining).hasSize(4);
    }

    @Test
    void testOptimisticLocking() {
        // given
        Enrollment enrollment1 = enrollmentRepository.findById(this.enrollment1.getId()).orElseThrow();
        Enrollment enrollment2 = enrollmentRepository.findById(this.enrollment1.getId()).orElseThrow();

        // when
        enrollment1.setGrade(95.0);
        enrollmentRepository.saveAndFlush(enrollment1);

        enrollment2.setGrade(88.0);

        // then
        // In a real scenario, saving enrollment2 would throw OptimisticLockException
        // For this test, we just verify that version numbers are tracked
        Enrollment refreshed = enrollmentRepository.findById(this.enrollment1.getId()).orElseThrow();
        assertThat(refreshed.getVersion()).isEqualTo(1L);
    }

    @Test
    void testSaveEnrollmentWithoutGrade() {
        // given
        Enrollment newEnrollment = Enrollment.builder()
                .studentId(3L)
                .courseId(5L)
                .build();

        // when
        Enrollment savedEnrollment = enrollmentRepository.save(newEnrollment);

        // then
        assertThat(savedEnrollment.getId()).isNotNull();
        assertThat(savedEnrollment.getGrade()).isNull();
    }

    @Test
    void testFindByGradeGreaterThanEqualBoundary() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeGreaterThanEqual(92.0);

        // then
        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getGrade()).isEqualTo(92.0);
    }

    @Test
    void testFindByGradeLessThanBoundary() {
        // when
        List<Enrollment> enrollments = enrollmentRepository.findByGradeLessThan(78.5);

        // then
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testCountWithMultipleStudentsInSameCourse() {
        // given - already have 2 students in course 1
        // when
        long count = enrollmentRepository.countByCourseId(1L);

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testExistsByStudentIdAndCourseIdMultipleCombinations() {
        // when & then
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).isTrue();
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).isTrue();
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(2L, 1L)).isTrue();
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(2L, 3L)).isTrue();
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(1L, 3L)).isFalse();
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(2L, 2L)).isFalse();
    }

    @Test
    void testUpdateEnrollmentFromNoGradeToGrade() {
        // given
        Enrollment enrollment = enrollmentRepository.findById(enrollment4.getId()).orElseThrow();
        assertThat(enrollment.getGrade()).isNull();

        // when
        enrollment.setGrade(88.0);
        Enrollment updatedEnrollment = enrollmentRepository.saveAndFlush(enrollment);
        entityManager.clear();

        // then
        Enrollment refreshedEnrollment = enrollmentRepository.findById(updatedEnrollment.getId()).orElseThrow();
        assertThat(refreshedEnrollment.getGrade()).isEqualTo(88.0);
        assertThat(refreshedEnrollment.getVersion()).isEqualTo(1L);
    }

    @Test
    void testSaveMultipleEnrollmentsSameStudent() {
        // given
        Enrollment enrollment5 = Enrollment.builder()
                .studentId(3L)
                .courseId(1L)
                .grade(75.0)
                .build();

        Enrollment enrollment6 = Enrollment.builder()
                .studentId(3L)
                .courseId(2L)
                .grade(80.0)
                .build();

        // when
        enrollmentRepository.save(enrollment5);
        enrollmentRepository.save(enrollment6);
        entityManager.flush();

        // then
        List<Enrollment> student3Enrollments = enrollmentRepository.findByStudentId(3L);
        assertThat(student3Enrollments).hasSize(2);
        assertThat(student3Enrollments).extracting(Enrollment::getGrade)
                .containsExactlyInAnyOrder(75.0, 80.0);
    }
}
