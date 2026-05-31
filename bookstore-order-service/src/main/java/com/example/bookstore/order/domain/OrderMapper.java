package com.example.bookstore.order.domain;

import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.OrderItem;
import com.example.bookstore.order.domain.models.OrderStatus;
import com.example.bookstore.order.web.controller.OrderDetailsDTO;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class OrderMapper {

    public static OrderEntity convertToEntity(CreateOrderRequest request) {

        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setCustomer(request.customer());
        newOrder.setAddress(request.deliveryAddress());
        newOrder.setStatus(OrderStatus.NEW);
        Set<OrderItemEntity> items = new HashSet<>();
        for (OrderItem item : request.items()) {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setCode(item.code());
            itemEntity.setName(item.name());
            itemEntity.setPrice(item.price());
            itemEntity.setQuantity(item.quantity());
            itemEntity.setOrder(newOrder);
            items.add(itemEntity);
        }
        newOrder.setItems(items);
        return newOrder;
    }

    static OrderDetailsDTO toOrderDetailsDTO(OrderEntity order) {
        Set<OrderItem> items = new HashSet<>();
        for (OrderItemEntity itemEntity : order.getItems()) {
            OrderItem item = new OrderItem(
                    itemEntity.getCode(), itemEntity.getName(), itemEntity.getPrice(), itemEntity.getQuantity());
            items.add(item);
        }
        return new OrderDetailsDTO(
                order.getOrderNumber(), items, order.getStatus(), order.getCustomer(), order.getAddress());
    }
}
