package isd.be.htc.service;

import isd.be.htc.dto.CategoryDTO;
import isd.be.htc.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
    Category createCategory(CategoryDTO category);
    Category updateCategory(Long id, CategoryDTO categoryDetails);
    void deleteCategory(Long id);
}
