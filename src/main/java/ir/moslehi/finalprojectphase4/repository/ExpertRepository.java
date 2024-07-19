package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert,Long> {

    Optional<Expert> findByEmail (String email);

}
