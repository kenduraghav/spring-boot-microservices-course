package com.example.bookstore.order.web.controller;

import com.example.bookstore.order.domain.OrderService;
import com.example.bookstore.order.domain.SecurityService;
import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.CreateOrderResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@RequestBody @Valid CreateOrderRequest request) {
        String userName = securityService.getCurrentUser();
        log.info("Creating order for user: {}", userName);
        return orderService.createOrder(request, userName);
    }
}
