package isd.be.htc.controller;

import isd.be.htc.dto.CartDTO;
import isd.be.htc.dto.CartItemDTO;
import isd.be.htc.model.Cart;
import isd.be.htc.model.CartItem;
import isd.be.htc.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public CartItemDTO addProductToCart(@RequestParam Long productId, @RequestParam int quantity) {
        CartItem cartItem = cartService.addProductToCart(productId, quantity);
        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice()
        );
    }

    @GetMapping("/")
    public CartDTO getCart() {
        Cart cart = cartService.getCart();
        return new CartDTO(
                cart.getId(),
                cart.getItems().stream().map(cartItem -> new CartItemDTO(
                        cartItem.getId(),
                        cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getQuantity(),
                        cartItem.getProduct().getPrice()
                )).toList()
        );
    }
}
