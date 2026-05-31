package com.example.bookstore.order.web.controller;

import com.example.bookstore.order.domain.models.Address;
import com.example.bookstore.order.domain.models.Customer;
import com.example.bookstore.order.domain.models.OrderItem;
import com.example.bookstore.order.domain.models.OrderStatus;
import java.util.Set;

public record OrderDetailsDTO(
        String orderNumber, Set<OrderItem> items, OrderStatus status, Customer customer, Address address) {}
