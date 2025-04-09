package isd.be.htc.service.impl;

import isd.be.htc.exception.BadRequestException;
import isd.be.htc.exception.NotFoundException;
import isd.be.htc.model.Product;
import isd.be.htc.model.User;
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

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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
    public Product createProduct(Product product) {
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
