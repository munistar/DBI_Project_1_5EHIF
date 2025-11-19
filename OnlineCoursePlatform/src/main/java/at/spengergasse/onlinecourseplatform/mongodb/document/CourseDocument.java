package at.spengergasse.onlinecourseplatform.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document for Course - optimized for frontend with embedded instructor
 * info
 */
@Document(collection = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDocument {

    @Id
    private String id;

    @Indexed
    private String name;

    private String description;

    // Embedded instructor information for frontend optimization
    private InstructorInfo instructor;

    // List of enrolled students (just IDs and basic info)
    @Builder.Default
    private List<StudentInfo> enrolledStudents = new ArrayList<>();

    /**
     * Embedded instructor information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InstructorInfo {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String department;
    }

    /**
     * Basic student information for enrolled students
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentInfo {
        private String studentId;
        private String firstName;
        private String lastName;
        private String email;
        private Double grade;
    }
}
