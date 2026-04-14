package com.example.bookstore.catalog.domain;

public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException forCode(String code) {
        return new ProductNotFoundException("Product with code " + code + " not found");
    }
}
