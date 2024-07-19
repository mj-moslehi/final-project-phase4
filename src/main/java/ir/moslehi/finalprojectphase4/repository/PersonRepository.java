package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

}
