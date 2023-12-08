package mnadolny.studentservices.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Entity
@Table(name = "courses")
@Relation(itemRelation = "course", collectionRelation = "courses")
public class Course extends RepresentationModel<Course> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    String description;
    @OneToMany(mappedBy = "course")
    private Set<CourseParticipation> students;

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    Set<CourseParticipation> getStudents() {
        return students;
    }

    void setStudents(Set<CourseParticipation> students) {
        this.students = students;
    }
}
