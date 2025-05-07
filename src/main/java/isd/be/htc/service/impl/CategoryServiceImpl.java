package isd.be.htc.service.impl;

import isd.be.htc.dto.CategoryDTO;
import isd.be.htc.model.Category;
import isd.be.htc.repository.CategoryRepository;
import isd.be.htc.repository.InventoryRepository;
import isd.be.htc.repository.OrderDetailRepository;
import isd.be.htc.repository.ProductRepository;
import isd.be.htc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, InventoryRepository inventoryRepository, OrderDetailRepository orderDetailRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(CategoryDTO category) {
        Optional<Category> existing = categoryRepository.findCategoryByNameIgnoreCase(category.getName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            category.setDescription(categoryDetails.getDescription());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found!"));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        try {
            inventoryRepository.deleteByProductCategoryId(id);
            orderDetailRepository.deleteByProductCategoryId(id);
            productRepository.deleteByCategoryId(id);
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
