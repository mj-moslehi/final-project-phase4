package ir.moslehi.finalprojectphase4.repository;


import ir.moslehi.finalprojectphase4.model.CartInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartInfoRepository extends JpaRepository<CartInfo, Long> {

    Optional<CartInfo> findByCartNumberAndCvv2(String cartNumber, String cvv2);

}
