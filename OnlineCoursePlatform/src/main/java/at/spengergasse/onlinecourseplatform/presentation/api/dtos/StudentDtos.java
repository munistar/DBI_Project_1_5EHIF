package at.spengergasse.onlinecourseplatform.presentation.api.dtos;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import at.spengergasse.onlinecourseplatform.domain.Student;
import jakarta.validation.constraints.NotNull;

public class StudentDtos {

    public record Summary(
            String firstName,
            String lastName
    ){
        public Summary(@NotNull Student student){

            this(student.getFirstName(), student.getLastName());
        }
    }

    public record Dto(String firstName, String lastName, String email){
        public Dto(Student student){
            this(student.getFirstName(), student.getLastName(), student.getEmail());
        }
    }



}



