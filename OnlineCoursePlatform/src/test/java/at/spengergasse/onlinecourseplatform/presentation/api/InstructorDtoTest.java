package at.spengergasse.onlinecourseplatform.presentation.api;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import at.spengergasse.onlinecourseplatform.presentation.api.dtos.InstructorDtos;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public class InstructorDtoTest {
    @Test
    void should_convert_Instructor_to_summary_dto() throws ParseException {

        Instructor inst = new Instructor();
        inst.setFirstName("John");
        inst.setLastName("Doe");
        inst.setEmail("john.doe@email.com");
        inst.setDepartment("Department");

        InstructorDtos.Summary summary = new InstructorDtos.Summary(inst);

        assertThat(summary.firstName()).isEqualTo("John");
        assertThat(summary.lastName()).isEqualTo("Doe");

    }

    @Test
    void should_convert_Insturctor_to_full_dto() throws ParseException {
        Instructor inst = new Instructor();
        inst.setFirstName("John");
        inst.setLastName("Doe");
        inst.setEmail("email");
        inst.setDepartment("department");

        InstructorDtos.Dto dto = new InstructorDtos.Dto(inst);

        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.email()).isEqualTo("email");
        assertThat(dto.department()).isEqualTo("department");
    }
}

