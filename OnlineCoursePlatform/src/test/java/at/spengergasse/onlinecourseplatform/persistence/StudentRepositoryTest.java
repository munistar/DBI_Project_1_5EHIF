package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import at.spengergasse.onlinecourseplatform.domain.Student;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class StudentRepositoryTest {


    private static final Logger logger = LoggerFactory.getLogger(StudentRepositoryTest.class);

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void can_save_and_reread() {
        logger.info("Starting test: can_save_and_reread");

        Student student =  new Student();
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setEmail("Email@gmail.com");
        student.setDateOfBirth(LocalDate.of(1980, 1, 1));
        studentRepository.save(student);
        Student savedStudent = studentRepository.save(student);

        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getFirstName()).isEqualTo("FirstName");
        assertThat(savedStudent.getLastName()).isEqualTo("LastName");

        logger.info("Completed test: can_save_and_reread");
    }



}
