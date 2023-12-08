package mnadolny.studentservices.controller;

import jakarta.validation.Valid;
import mnadolny.studentservices.model.*;
import mnadolny.studentservices.service.StudentService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="students", produces= MediaType.APPLICATION_JSON_VALUE)
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public HttpEntity<StudentDTO> getStudent(@PathVariable("id") Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    public HttpEntity<CollectionModel<StudentDTO>> getAllStudents(Pageable pageable) {
        return studentService.getAllStudents(pageable);
    }

    @PostMapping
    public HttpEntity<Student> addStudent(@RequestBody @Valid Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("/{id}/assign-student-to/{course_id}")
    public HttpEntity<?> assignStudentToCourse(
            @PathVariable("id") Long id,
            @PathVariable("course_id") Long course_id) {
        return studentService.assignStudentToCourse(id, course_id);
    }

    @GetMapping("/{id}/report")
    public  HttpEntity<Student> getStudentReport(@PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/courses")
    public HttpEntity<CollectionModel<CourseParticipation>>
    getStudentParticipations(@PathVariable("id") Long id) {
        return studentService.getStudentParticipations(id);
    }

    @PutMapping("/{id}/grade-student/{course_id}")
    public HttpEntity<CourseParticipation> gradeStudent(
            @PathVariable("id") Long studentId,
            @PathVariable("course_id") Long courseId,
            @RequestParam(name = "grade", defaultValue = "A", required = false)
            String grade
    ) {
        return studentService.gradeStudent(studentId, courseId, grade);
    }
}
