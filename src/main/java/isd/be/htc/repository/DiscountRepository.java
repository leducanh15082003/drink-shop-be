package isd.be.htc.repository;

import isd.be.htc.model.Discount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d FROM Discount d WHERE d.isActive = true AND d.startDate <= CURRENT_TIMESTAMP AND d.endDate >= CURRENT_TIMESTAMP AND d.quantity > 0")
    List<Discount> findValidDiscounts();

    boolean existsByCode(String code);
}
