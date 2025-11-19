package at.spengergasse.onlinecourseplatform.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document for Student - optimized for frontend with embedded
 * enrollments
 */
@Document(collection = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDocument {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String address;
    private LocalDate registrationDate;

    // Embedded enrollments for frontend optimization
    @Builder.Default
    private List<EnrollmentInfo> enrollments = new ArrayList<>();

    /**
     * Embedded enrollment information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EnrollmentInfo {
        private String enrollmentId;
        private String courseId;
        private String courseName;
        private String courseDescription;
        private Double grade;
        private LocalDate enrollmentDate;
    }
}
