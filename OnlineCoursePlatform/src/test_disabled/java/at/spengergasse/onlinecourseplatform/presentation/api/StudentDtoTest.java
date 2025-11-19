package at.spengergasse.onlinecourseplatform.presentation.api;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import at.spengergasse.onlinecourseplatform.domain.Student;
import at.spengergasse.onlinecourseplatform.presentation.api.dtos.InstructorDtos;
import at.spengergasse.onlinecourseplatform.presentation.api.dtos.StudentDtos;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;


public class StudentDtoTest {
    @Test
    void should_convert_Student_to_summary_dto() throws ParseException {

        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@email.com");


        StudentDtos.Summary summary = new StudentDtos.Summary(student);

        assertThat(summary.firstName()).isEqualTo("John");
        assertThat(summary.lastName()).isEqualTo("Doe");

    }

    @Test
    void should_convert_Student_to_full_dto() throws ParseException {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("email");

        StudentDtos.Dto dto = new StudentDtos.Dto(student);

        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.email()).isEqualTo("email");
    }
}

