package isd.be.htc.service.impl;

import isd.be.htc.model.Inventory;
import isd.be.htc.repository.InventoryRepository;
import isd.be.htc.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Optional<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        inventory.setUpdatedAt(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventory.setProduct(inventoryDetails.getProduct());
            inventory.setQuantity(inventoryDetails.getQuantity());
            inventory.setUpdatedAt(LocalDateTime.now());
            return inventoryRepository.save(inventory);
        }).orElseThrow(() -> new RuntimeException("Inventory not found!"));
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
