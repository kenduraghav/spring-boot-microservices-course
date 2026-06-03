package com.example.bookstore.webapp.clients.catalog;

public record Product(Long id, String code, String name, String description, String imageUrl, Double price) {}
