package isd.be.htc.service.impl;

import isd.be.htc.dto.CreateProductDTO;
import isd.be.htc.exception.BadRequestException;
import isd.be.htc.exception.NotFoundException;
import isd.be.htc.model.Category;
import isd.be.htc.model.Product;
import isd.be.htc.model.User;
import isd.be.htc.repository.CategoryRepository;
import isd.be.htc.repository.ProductRepository;
import isd.be.htc.repository.UserRepository;
import isd.be.htc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Product> getAllProducts() {
        return productRepository.findAll();
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
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setImageUrl(productDetails.getImageUrl());
            product.setCategory(productDetails.getCategory());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found!"));
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
