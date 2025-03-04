package com.example.shoppingCart.exception;

import com.example.shoppingCart.model.Product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
