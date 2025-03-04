package com.example.shoppingCart.repository;

import com.example.shoppingCart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem , Long> {
    void deleteAllByCartId(Long id);
}
