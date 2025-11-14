package at.spengergasse.onlinecourseplatform.presentation.api.dtos;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import jakarta.validation.constraints.NotNull;

public class InstructorDtos {

    public record Summary(
            String firstName,
            String lastName
    ){
        public Summary(@NotNull Instructor inst){

            this(inst.getFirstName(), inst.getLastName());
        }
    }

    public record Dto(String firstName, String lastName, String email, String department){
        public Dto(Instructor inst){
            this(inst.getFirstName(), inst.getLastName(), inst.getEmail(), inst.getDepartment());
        }
    }



}



