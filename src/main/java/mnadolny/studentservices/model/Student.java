package mnadolny.studentservices.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private PersonalInfo personalInfo = new PersonalInfo();
    @OneToMany(mappedBy = "student", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<CourseParticipation> courseParticipations;

    public Student() {
    }

    public List<Course> getCourses() {
        return this.courseParticipations
                .stream()
                .map(CourseParticipation::getCourse)
                .toList();
    }

    public List<CourseParticipation> getCourseParticipations() {
        return courseParticipations;
    }

    public void setCourseParticipations(List<CourseParticipation> courseParticipations) {
        this.courseParticipations = courseParticipations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
}
