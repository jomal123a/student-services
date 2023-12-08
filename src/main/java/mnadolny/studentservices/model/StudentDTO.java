package mnadolny.studentservices.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(itemRelation = "student", collectionRelation = "students")
public class StudentDTO extends RepresentationModel<StudentDTO> {
    private Long id;
    private PersonalInfo personalInfo;

    public StudentDTO(Long id, PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
        this.id = id;
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
