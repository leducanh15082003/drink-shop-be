package isd.be.htc.service;

import isd.be.htc.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {
    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    List<Product> getProductsByCategory(Long categoryId);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product productDetails);

    void deleteProduct(Long id);

    void addProductToFavorite(Long userId, Long productId);

    void removeProductFromFavorite(Long userId, Long productId);

    Set<Product> getFavoriteProducts(Long userId);
}
