package isd.be.htc.repository;

import isd.be.htc.model.Discount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findByIsActive(boolean isActive);

    boolean existsByCode(String code);
}
