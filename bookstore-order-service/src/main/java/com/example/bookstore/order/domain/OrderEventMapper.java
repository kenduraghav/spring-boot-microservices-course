package com.example.bookstore.order.domain;

import com.example.bookstore.order.domain.models.OrderCancelledEvent;
import com.example.bookstore.order.domain.models.OrderCreatedEvent;
import com.example.bookstore.order.domain.models.OrderDeliveredEvent;
import com.example.bookstore.order.domain.models.OrderErrorEvent;
import com.example.bookstore.order.domain.models.OrderItem;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    static OrderDeliveredEvent toOrderDeliveredEvent(OrderEntity order) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                toOrderItems(order),
                order.getCustomer(),
                order.getAddress(),
                order.getUpdatedAt());
    }

    static OrderCancelledEvent toOrderCancelledEvent(OrderEntity order, String reason) {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                toOrderItems(order),
                order.getCustomer(),
                order.getAddress(),
                reason,
                order.getUpdatedAt());
    }

    static OrderErrorEvent toOrderErrorEvent(OrderEntity order, String reason) {
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                toOrderItems(order),
                order.getCustomer(),
                order.getAddress(),
                reason,
                order.getUpdatedAt());
    }

    private static Set<OrderItem> toOrderItems(OrderEntity savedOrder) {
        Set<OrderItem> items = savedOrder.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());
        return items;
    }
}
