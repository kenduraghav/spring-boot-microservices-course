package com.example.bookstore.catalog.domain;

public record Product(String code, String name, String description, String imageUrl, Double price) {}
