package at.spengergasse.onlinecourseplatform.presentation.api.mappers;

import at.spengergasse.onlinecourseplatform.domain.Instructor;
import at.spengergasse.onlinecourseplatform.presentation.api.dtos.InstructorDtos;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel="spring")
public interface InstructorMapper {

    InstructorDtos.Summary toSummaryDto(Instructor instructor);
    List<InstructorDtos.Summary> toSummaryDtos(Iterable<Instructor> instructor);
    InstructorDtos.Dto toDto(Instructor instructor);
    List<InstructorDtos.Dto> toDtos(Iterable<Instructor> instructor);

}

