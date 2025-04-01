package isd.be.htc.repository;

import isd.be.htc.model.Inventory;
import isd.be.htc.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);
    Inventory findByProduct(Product product);
}
