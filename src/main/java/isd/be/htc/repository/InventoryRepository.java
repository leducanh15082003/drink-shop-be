package isd.be.htc.repository;

import isd.be.htc.model.Inventory;
import isd.be.htc.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);
    Inventory findByProduct(Product product);
    @Modifying
    @Transactional
    @Query("""
      DELETE FROM Inventory i
      WHERE i.product.category.id = :categoryId
    """)
    void deleteByProductCategoryId(Long categoryId);
}
