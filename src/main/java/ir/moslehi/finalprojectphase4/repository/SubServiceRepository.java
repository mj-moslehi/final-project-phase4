package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Service;
import ir.moslehi.finalprojectphase4.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Long> {

    List<SubService> findByService(Service service);

    Optional<SubService> findByName(String name);


}
