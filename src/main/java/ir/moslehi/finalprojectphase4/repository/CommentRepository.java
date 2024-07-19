package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Comment;
import ir.moslehi.finalprojectphase4.model.Expert;
import ir.moslehi.finalprojectphase4.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findByOrders(Orders orders);

    List<Comment> findByExpert(Expert expert);

}
