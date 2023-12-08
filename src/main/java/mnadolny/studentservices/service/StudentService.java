package mnadolny.studentservices.service;

import mnadolny.studentservices.controller.CourseController;
import mnadolny.studentservices.controller.StudentController;
import mnadolny.studentservices.model.*;
import mnadolny.studentservices.model.repository.CourseParticipationRepository;
import mnadolny.studentservices.model.repository.CourseRepository;
import mnadolny.studentservices.model.repository.StudentSqlRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class StudentService {
    private final StudentSqlRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentDTOMapperService studentDTOMapperService;
    private final CourseParticipationRepository courseParticipationRepository;

    public StudentService(StudentSqlRepository studentRepository,
                          CourseRepository courseRepository,
                          StudentDTOMapperService studentDTOMapperService,
                          CourseParticipationRepository courseParticipationRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentDTOMapperService = studentDTOMapperService;
        this.courseParticipationRepository = courseParticipationRepository;
    }

    public HttpEntity<StudentDTO> getStudent(Long id) {
        return studentRepository
                .findById(id)
                .map(studentDTOMapperService)
                .map(
                        dto -> {
                            addLinksToStudentDTO(dto);
                            return ResponseEntity.ok(dto);
                        }
                ).orElse(ResponseEntity.notFound().build());
    }

    public HttpEntity<CollectionModel<StudentDTO>> getAllStudents(Pageable pageable) {
        Page<Student> students = studentRepository.findAll(pageable);
        List<StudentDTO> studentsDTOS = students.stream()
                .map(studentDTOMapperService)
                .map(this::addLinksToStudentDTO)
                .toList();

        Link selfLink = linkTo(methodOn(StudentController.class).getAllStudents(pageable)).withSelfRel();
        var model = CollectionModel.of(studentsDTOS).add(selfLink);
        return ResponseEntity.ok(model);
    }

    public HttpEntity<Student> addStudent(Student student) {
        studentRepository.save(student);
        return ResponseEntity.created(URI.create("/students/" + student.getId())).build();
    }

    @Transactional
    public HttpEntity<StudentDTO> assignStudentToCourse(
            Long id,
            Long course_id) {
        Optional<Course> course = courseRepository.findById(course_id);
        Optional<Student> student = studentRepository.findById(id);

        if (course.isPresent() && student.isPresent()) {
            CourseParticipantKey cpk = new CourseParticipantKey(id, course_id);
            courseParticipationRepository.save(
                    new CourseParticipation(cpk, student.get(), course.get())
            );
            return ResponseEntity.ok(studentDTOMapperService.apply(student.get()));
        }
        return ResponseEntity.notFound().build();
    }

    public  HttpEntity<Student> getStudentReport(Long id) {
        return ResponseEntity.noContent().build();
    }

    public HttpEntity<CollectionModel<CourseParticipation>> getStudentParticipations(Long id) {
        var studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentOpt.get();
        List<CourseParticipation> participations = student.getCourseParticipations();
        var content = CollectionModel.of(participations);
        content.add(linkTo(methodOn(StudentController.class).getStudentParticipations(id))
                .withSelfRel());
        return ResponseEntity.ok(content);
    }

    @Transactional
    public HttpEntity<CourseParticipation> gradeStudent(
            Long studentId,
            Long courseId,
            String grade) {
        CourseParticipation.Grade gEnum = switch (grade) {
            case "B" -> CourseParticipation.Grade.B;
            case "C" -> CourseParticipation.Grade.C;
            case "D" -> CourseParticipation.Grade.D;
            case "E" -> CourseParticipation.Grade.E;
            default -> CourseParticipation.Grade.A;
        };
        var partOpt = courseParticipationRepository.findById(
                new CourseParticipantKey(studentId, courseId)
        );
        return partOpt
                .map(p -> {
                    p.setGrade(gEnum);
                    p.setFinishedOn(LocalDateTime.now());
                    return ResponseEntity.ok(p);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public StudentDTO addLinksToStudentDTO(StudentDTO student) {
        Link studentLink = linkTo(methodOn(StudentController.class).getStudent(student.getId()))
                .withSelfRel();
        Link studentCoursesLink = linkTo(methodOn(StudentController.class).getStudentParticipations(student.getId()))
                .withRel("courses");
        Link studentReportLink = linkTo(methodOn(StudentController.class).getStudentReport(student.getId()))
                .withRel("report");


        student.add(studentLink);
        student.add(studentReportLink);
        student.add(studentCoursesLink);

        return student;
    }
}
