package at.spengergasse.onlinecourseplatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 200, message = "Course name must be between 3 and 200 characters")
    @Column(nullable = false, length = 200)
    private String name;

    @NotBlank(message = "Course description is required")
    @Size(min = 10, max = 2000, message = "Course description must be between 10 and 2000 characters")
    @Column(nullable = false, length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", foreignKey = @ForeignKey(name = "fk_course_instructor"))
    private Instructor instructor;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Course course))
            return false;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", instructorId=" + (instructor != null ? instructor.getId() : null) +
                ", version=" + version +
                '}';
    }
}
