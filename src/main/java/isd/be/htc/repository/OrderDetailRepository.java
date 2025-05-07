package isd.be.htc.repository;

import isd.be.htc.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderDetail od WHERE od.product.category.id = :catId")
    void deleteByProductCategoryId(@Param("catId") Long categoryId);
}
