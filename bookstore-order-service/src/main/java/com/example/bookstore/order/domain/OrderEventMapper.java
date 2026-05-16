package com.example.bookstore.order.domain;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.bookstore.order.domain.models.OrderCreatedEvent;
import com.example.bookstore.order.domain.models.OrderItem;

class OrderEventMapper {

	static OrderCreatedEvent toOrderCreatedEvent(OrderEntity savedOrder) {
		return new OrderCreatedEvent(
				UUID.randomUUID().toString(), 
				savedOrder.getOrderNumber(),
				toOrderItems(savedOrder), 
				savedOrder.getCustomer(), 
				savedOrder.getAddress(), 
				savedOrder.getCreatedAt());
	}

	private static Set<OrderItem> toOrderItems(OrderEntity savedOrder) {
		Set<OrderItem> items = savedOrder.getItems().stream()
				.map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
				.collect(Collectors.toSet());
		return items;
	}

}
