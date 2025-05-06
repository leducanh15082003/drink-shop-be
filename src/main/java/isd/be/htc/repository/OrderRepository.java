package isd.be.htc.repository;

import isd.be.htc.model.Order;

import isd.be.htc.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.product"
    })
    List<Order> findByUserId(Long userId);
    long count();
    long countByStatusAndOrderTimeBetween(OrderStatus status, LocalDateTime from, LocalDateTime to);
}
