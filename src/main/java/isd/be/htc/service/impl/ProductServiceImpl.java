package isd.be.htc.service.impl;

import isd.be.htc.dto.CreateProductDTO;
import isd.be.htc.dto.UpdateProductDTO;
import isd.be.htc.exception.BadRequestException;
import isd.be.htc.exception.NotFoundException;
import isd.be.htc.model.Category;
import isd.be.htc.model.Product;
import isd.be.htc.model.User;
import isd.be.htc.repository.CategoryRepository;
import isd.be.htc.repository.ProductRepository;
import isd.be.htc.repository.UserRepository;
import isd.be.htc.service.ProductService;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts(Long categoryId, Double minPrice, Double maxPrice, String search) {
        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (search != null && !search.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Product createProduct(CreateProductDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImage());
        product.setCategory(category);
        product.setIngredients(dto.getIngredients());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updateProductDTO.getName()); // Không cần check null
        product.setDescription(updateProductDTO.getDescription());
        product.setIngredients(updateProductDTO.getIngredients());
        product.setPrice(updateProductDTO.getPrice());
        product.setImageUrl(updateProductDTO.getImage());

        if (updateProductDTO.getCategoryId() != null) { // Category có thể cần check nếu optional
            Category category = categoryRepository.findById(updateProductDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        productRepository.save(product);
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void addProductToFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (user.getFavoriteProducts().contains(product)) {
            throw new BadRequestException("Product is already in favorites");
        }

        user.getFavoriteProducts().add(product);
        userRepository.save(user);
    }

    @Override
    public void removeProductFromFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (!user.getFavoriteProducts().contains(product)) {
            throw new BadRequestException("Product is not in favorites");
        }

        user.getFavoriteProducts().remove(product);
        userRepository.save(user);
    }

    @Override
    public Set<Product> getFavoriteProducts(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"))
                .getFavoriteProducts();
    }
}
