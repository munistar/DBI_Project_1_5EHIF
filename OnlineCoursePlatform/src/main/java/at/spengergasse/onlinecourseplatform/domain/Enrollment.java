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

    // Note: student_id foreign key will be added once Student entity is ready
    // Temporary field to store student reference
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    // Note: course_id foreign key will be added once we connect with Course entity
    // Temporary field to store course reference
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @DecimalMin(value = "0.0", message = "Grade must be at least 0.0")
    @DecimalMax(value = "100.0", message = "Grade must not exceed 100.0")
    @Column(name = "grade")
    private Double grade;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment enrollment)) return false;
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
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", grade=" + grade +
                ", version=" + version +
                '}';
    }
}
