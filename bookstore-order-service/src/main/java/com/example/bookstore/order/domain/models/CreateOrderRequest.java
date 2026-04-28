package com.example.bookstore.order.domain.models;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CreateOrderRequest(
		@Valid @NotEmpty(message = "Items cannot be empty") Set<OrderItem> items,
		@Valid Customer customer,
		@Valid Address deliveryAddress) {

}
