package isd.be.htc.repository;

import isd.be.htc.model.Order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.product"
    })
    List<Order> findByUserId(Long userId);
}
