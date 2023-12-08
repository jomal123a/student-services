package mnadolny.studentservices.controller;

import jakarta.validation.Valid;
import mnadolny.studentservices.model.Course;
import mnadolny.studentservices.model.Student;
import mnadolny.studentservices.model.StudentDTO;
import mnadolny.studentservices.model.repository.CourseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path="courses", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    HttpEntity<CollectionModel<Course>> getAllCourses(Pageable pageable) {
        List<Course> coursesLinked = courseRepository.findAll(pageable).stream()
                .map(this::addLinksToCourse)
                .toList();

        Link selfLink = linkTo(methodOn(CourseController.class).getAllCourses(pageable)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(coursesLinked).add(selfLink));
    }

    @GetMapping(path="/{id}")
    public HttpEntity<Course> getCourse(@PathVariable(name = "id") Long id) {
        return courseRepository.findById(id)
                .map(course -> ResponseEntity.ok(addLinksToCourse(course)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    HttpEntity<Course> addCourse(@RequestBody @Valid Course course) {
        courseRepository.save(course);
        return ResponseEntity.created(URI.create("/courses/" + course.getId())).build();
    }

    Course addLinksToCourse(Course course) {
        Link studentLink = linkTo(methodOn(CourseController.class).getCourse(course.getId()))
                .withSelfRel();
        course.add(studentLink);

        return course;
    }
}
