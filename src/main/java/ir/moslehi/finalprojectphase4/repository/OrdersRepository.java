package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Customer;
import ir.moslehi.finalprojectphase4.model.Orders;
import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByCustomerAndOrderStatus(Customer customer, OrderStatus orderStatus);

}
