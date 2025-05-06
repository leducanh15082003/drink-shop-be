package isd.be.htc.controller;

import isd.be.htc.dto.CreateProductDTO;
import isd.be.htc.dto.ProductDTO;
import isd.be.htc.dto.ProductQuantityDTO;
import isd.be.htc.dto.UpdateProductDTO;
import isd.be.htc.model.Product;
import isd.be.htc.service.ProductService;
import isd.be.htc.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts(@RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search) {
        List<Product> products = productService.getAllProducts(categoryId, minPrice, maxPrice, search);

        return products.stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getImageUrl(),
                        product.getCategory().getName()))
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getImageUrl(),
                product.getIngredients(),
                product.getCategory().getName(),
                product.getCategory().getId());
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId).stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getImageUrl(),
                        product.getCategory().getName()))
                .toList();
    }

    @GetMapping("/favorites")
    public List<ProductDTO> getFavoriteProducts(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findUserByEmail(userDetails.getUsername()).getId();
        return productService.getFavoriteProducts(userId).stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getImageUrl(),
                        product.getName()))
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductDTO product) {
        try {
            Product p = productService.createProduct(product);
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            e.printStackTrace(); // In thông báo lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/favorites/{productId}")
    public ResponseEntity<Void> addProductToFavorite(@PathVariable Long productId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findUserByEmail(userDetails.getUsername()).getId();
        productService.addProductToFavorite(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            Product updatedProduct = productService.updateProduct(id, updateProductDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/favorites/{productId}")
    public ResponseEntity<Void> deleteProductFromFavorite(@PathVariable Long productId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findUserByEmail(userDetails.getUsername()).getId();
        productService.removeProductFromFavorite(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-quantity-sold")
    public ResponseEntity<?> updateQuantitySold(@RequestBody List<ProductQuantityDTO> items) {
        for (ProductQuantityDTO item : items) {
            productService.updateQuantitySold(item.getProductId(), item.getQuantity());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top-sold")
    public List<ProductDTO> getTopSoldProducts() {
        List<Product> topProducts = productService.getTopSoldProducts(3);
        return topProducts.stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getImageUrl(),
                        product.getIngredients(),
                        product.getCategory().getName(),
                        product.getCategory().getId()))
                .toList();
    }
}
