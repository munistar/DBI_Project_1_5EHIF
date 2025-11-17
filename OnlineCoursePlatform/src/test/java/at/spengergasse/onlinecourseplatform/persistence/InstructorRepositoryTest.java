package at.spengergasse.onlinecourseplatform.persistence;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.List;

@DataJpaTest
public class InstructorRepositoryTest {


    private static final Logger logger = LoggerFactory.getLogger(InstructorRepositoryTest.class);

    @Autowired
    private InstructorRepository InstructorRepository;

    @Test
    void can_save_and_reread() {
        logger.info("Starting test: can_save_and_reread");

        Instructor instructor =  new Instructor();
        instructor.setFirstName("FirstName");
        instructor.setLastName("LastName");
        instructor.setEmail("Email");
        instructor.setDepartment("Department");
        Instructor savedInstructor = InstructorRepository.save(instructor);

        assertThat(savedInstructor.getId()).isNotNull();
        assertThat(savedInstructor.getFirstName()).isEqualTo("FirstName");
        assertThat(savedInstructor.getLastName()).isEqualTo("LastName");

        logger.info("Completed test: can_save_and_reread");
    }



}
