package isd.be.htc.service;

import isd.be.htc.model.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> getAllInventories();
    Optional<Inventory> getInventoryById(Long id);
    Optional<Inventory> getInventoryByProductId(Long productId);
    Inventory createInventory(Inventory inventory);
    Inventory updateInventory(Long id, Inventory inventoryDetails);
    void deleteInventory(Long id);
}
