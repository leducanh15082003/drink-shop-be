package isd.be.htc.repository;

import isd.be.htc.model.Order;

import isd.be.htc.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
        @EntityGraph(attributePaths = {
                        "orderDetails",
                        "orderDetails.product"
        })
        List<Order> findByUserId(Long userId);

        long countByUserId(Long userId);

        long count();

        long countByStatusAndOrderTimeBetween(OrderStatus status, LocalDateTime from, LocalDateTime to);

        @Query("""
                          SELECT COALESCE(SUM(o.totalAmount), 0)
                          FROM Order o
                          WHERE o.status       = :status
                            AND o.orderTime   BETWEEN :start AND :end
                        """)
        Double sumTotalAmountByStatusBetween(
                        @Param("status") OrderStatus status,
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        @Query(value = """
                        SELECT
                          to_char(o.order_time, 'Mon') AS month,
                          COALESCE(SUM(o.total_amount), 0) AS value
                        FROM orders o
                        WHERE o.status = :status
                          AND extract(year from o.order_time) = :year
                        GROUP BY to_char(o.order_time, 'Mon'), extract(month from o.order_time)
                        ORDER BY extract(month from o.order_time)
                        """, nativeQuery = true)
        List<Object[]> findMonthlyRevenueRaw(
                        @Param("status") String status,
                        @Param("year") int year);

        @Modifying
        @Transactional
        @Query("UPDATE Order o SET o.discount = NULL WHERE o.discount.id = :discountId")
        void clearDiscountFromOrders(@Param("discountId") Long discountId);
}
