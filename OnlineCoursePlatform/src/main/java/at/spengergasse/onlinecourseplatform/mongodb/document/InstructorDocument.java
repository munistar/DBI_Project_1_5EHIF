package at.spengergasse.onlinecourseplatform.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

/**
 * MongoDB document for Instructor
 */
@Document(collection = "instructors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorDocument {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String department;
    private String phoneNumber;
    private String officeLocation;
    private LocalDate hireDate;
    private String biography;
}
