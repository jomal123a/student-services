package mnadolny.studentservices.model.repository;

import mnadolny.studentservices.model.Student;
import org.hibernate.mapping.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StudentSqlRepository extends JpaRepository<Student, Long> {
}
