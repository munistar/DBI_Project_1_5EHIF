package at.spengergasse.onlinecourseplatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "fk_enrollment_student"))
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_enrollment_course"))
    private Course course;

    @DecimalMin(value = "0.0", message = "Grade must be at least 0.0")
    @DecimalMax(value = "100.0", message = "Grade must not exceed 100.0")
    @Column(name = "grade")
    private Double grade;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Enrollment enrollment))
            return false;
        return Objects.equals(id, enrollment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", studentId=" + (student != null ? student.getId() : null) +
                ", courseId=" + (course != null ? course.getId() : null) +
                ", grade=" + grade +
                ", version=" + version +
                '}';
    }
}
