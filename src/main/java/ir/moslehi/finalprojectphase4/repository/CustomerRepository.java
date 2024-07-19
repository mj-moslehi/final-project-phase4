package ir.moslehi.finalprojectphase4.repository;

import ir.moslehi.finalprojectphase4.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c " +
            "SET c.enabled = TRUE WHERE c.email = ?1")
    void enableCustomer(String email);
}
