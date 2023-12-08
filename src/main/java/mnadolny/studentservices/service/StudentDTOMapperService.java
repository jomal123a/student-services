package mnadolny.studentservices.service;

import mnadolny.studentservices.model.Student;
import mnadolny.studentservices.model.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentDTOMapperService implements Function<Student, StudentDTO> {
    @Override
    public StudentDTO apply(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getPersonalInfo()
        );
    }
}
