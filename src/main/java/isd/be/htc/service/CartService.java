package isd.be.htc.service;

import isd.be.htc.model.Cart;
import isd.be.htc.model.CartItem;

public interface CartService {
    CartItem addProductToCart(Long productId, int quantity);
    Cart getCart();
}
