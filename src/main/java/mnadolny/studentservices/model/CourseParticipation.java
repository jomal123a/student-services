package mnadolny.studentservices.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Entity
public class CourseParticipation extends RepresentationModel<CourseParticipation> {
    @EmbeddedId
    private CourseParticipantKey id;
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student student;
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
    private Grade grade;
    private LocalDateTime signedUp;
    private LocalDateTime finishedOn;

    public CourseParticipation() {
    }

    public CourseParticipation(CourseParticipantKey id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
    }

    @PrePersist
    public void prePersist() {
        signedUp = LocalDateTime.now();
    }

    public CourseParticipantKey getId() {
        return id;
    }

    public void setId(CourseParticipantKey id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public LocalDateTime getSignedUp() {
        return signedUp;
    }

    public void setSignedUp(LocalDateTime signedUp) {
        this.signedUp = signedUp;
    }

    public LocalDateTime getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(LocalDateTime finishedOn) {
        this.finishedOn = finishedOn;
    }

    public enum Grade {
        E, D, C, B, A
    }
}
