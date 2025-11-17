package at.spengergasse.onlinecourseplatform.presentation.mappers;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import at.spengergasse.onlinecourseplatform.presentation.api.mappers.InstructorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class InstructorMapperTest {

    private Instructor instructor;
    @Autowired
    private InstructorMapper InstructorMapper;

    @BeforeEach
    void setup(){
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setEmail("email");
        instructor.setDepartment("department");
    }

    @Test
    void ensureDoctorSummaryMapping(){
        var instructorDto=InstructorMapper.toSummaryDto(instructor);
        assertThat(instructorDto).isNotNull();
        assertThat(instructorDto.lastName()).isEqualTo("Doe");
    }

    @Test
    void ensureDoctorDtoMapping(){
        var instructorDto=InstructorMapper.toDto(instructor);
        assertThat(instructorDto).isNotNull();
        assertThat(instructorDto.firstName()).isEqualTo("Jan");
        assertThat(instructorDto.lastName()).isEqualTo("Doe");
        assertThat(instructorDto.email()).isEqualTo("email");
    }
}


/*


@SpringBootTest
public class DoctorMapperTest {
    private Doctor doctor;
    @Autowired
    private DoctorMapper doctorMapper;

    @BeforeEach
    void setup(){
        doctor= Fixtures.doctor1();
    }

    @Test
    void ensureDoctorSummaryMapping(){
        var doctorDto=doctorMapper.toSummaryDto(doctor);
        assertThat(doctorDto).isNotNull();
        assertThat(doctorDto.lastName()).isEqualTo("Doe");
    }

    @Test
    void ensureDoctorDtoMapping(){
        var doctorDto=doctorMapper.toDto(doctor);
        assertThat(doctorDto).isNotNull();
        assertThat(doctorDto.firstName()).isEqualTo("Jan");
        assertThat(doctorDto.lastName()).isEqualTo("Doe");
        assertThat(doctorDto.emailAddress().value()).isEqualTo("jan@doe.com");
    }
}

 */