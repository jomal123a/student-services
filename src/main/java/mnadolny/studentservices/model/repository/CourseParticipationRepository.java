package mnadolny.studentservices.model.repository;

import mnadolny.studentservices.model.CourseParticipantKey;
import mnadolny.studentservices.model.CourseParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseParticipationRepository
        extends JpaRepository<CourseParticipation, CourseParticipantKey> {
}
