package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.model.ExpertSubService;
import ir.moslehi.finalprojectphase4.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Expert_SubServiceRepository extends JpaRepository<ExpertSubService, Long> {

    @Modifying
    @Query("select es.subService from ExpertSubService es where es.expert=:expert")
    List<SubService> findSubServiceByExpert(Expert expert);

    Optional<ExpertSubService> findByExpertAndSubService(Expert expert, SubService subService);

}
