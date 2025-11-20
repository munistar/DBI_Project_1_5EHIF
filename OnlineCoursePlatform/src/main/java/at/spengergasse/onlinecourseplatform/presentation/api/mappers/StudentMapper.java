package at.spengergasse.onlinecourseplatform.presentation.api.mappers;

import at.spengergasse.onlinecourseplatform.domain.Student;
import at.spengergasse.onlinecourseplatform.presentation.api.dtos.StudentDtos;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentMapper MAPPER = Mappers.getMapper(StudentMapper.class);

    StudentDtos.Summary toSummaryDto(Student student);

    List<StudentDtos.Summary> toSummaryDtos(Iterable<Student> student);

    StudentDtos.Dto toDto(Student student);

    List<StudentDtos.Dto> toDtos(Iterable<Student> student);

}
