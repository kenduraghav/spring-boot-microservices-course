package com.example.bookstore.order.web.controller;

import com.example.bookstore.order.domain.OrderService;
import com.example.bookstore.order.domain.SecurityService;
import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.CreateOrderResponse;
import com.example.bookstore.order.domain.models.OrderSummaryDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    List<OrderSummaryDTO> getOrders() {
        String currentUser = securityService.getCurrentUser();
        log.info("Getting orders for user: {}", currentUser);
        return orderService.getOrdersForUser(currentUser);
    }

    @GetMapping("/{orderNumber}")
    OrderDetailsDTO getOrderDetails(@PathVariable String orderNumber) {
        String currentUser = securityService.getCurrentUser();
        log.info("Getting order details for order number: {} and user: {}", orderNumber, currentUser);
        return orderService.getUserOrderDetails(orderNumber, currentUser);
    }
}
