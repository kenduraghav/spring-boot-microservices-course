package com.example.bookstore.webapp.clients.orders;

import jakarta.validation.constraints.NotBlank;

public record Customer(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Phone is required") String phone) {}
