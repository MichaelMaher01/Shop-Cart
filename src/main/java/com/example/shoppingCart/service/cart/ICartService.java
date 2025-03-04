package com.example.shoppingCart.service.cart;

import com.example.shoppingCart.model.Cart;
import com.example.shoppingCart.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal gerTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
