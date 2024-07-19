package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion,Long> {

    Optional<Suggestion> findByOrdersAndExpert (Orders orders , Expert expert);

    @Query("select s FROM Suggestion s where s.orders.id=:orderId ORDER BY s.proposedPrice asc, s.expert.score desc ")
    List<Suggestion> suggestionListSorted(@Param("orderId")long orderId);

}
