package com.example.bookstore.webapp.web.controller;

import com.example.bookstore.webapp.clients.orders.CreateOrderRequest;
import com.example.bookstore.webapp.clients.orders.OrderConfirmationDTO;
import com.example.bookstore.webapp.clients.orders.OrderDetailsDTO;
import com.example.bookstore.webapp.clients.orders.OrderServiceClient;
import com.example.bookstore.webapp.clients.orders.OrderSummaryDTO;
import com.example.bookstore.webapp.service.SecurityHelper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderServiceClient orderServiceClient;
    private final SecurityHelper securityHelper;

    OrderController(OrderServiceClient orderServiceClient, SecurityHelper securityHelper) {
        this.orderServiceClient = orderServiceClient;
        this.securityHelper = securityHelper;
    }

    @GetMapping("/cart")
    String cart() {
        return "cart";
    }

    @GetMapping("/orders/{orderNumber}")
    String showOrderDetails(@PathVariable String orderNumber, Model model) {
        model.addAttribute("orderNumber", orderNumber);
        return "order_details";
    }

    @GetMapping("/orders")
    String showOrderHistory() {
        return "orders";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    OrderConfirmationDTO createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        log.info("Creating order: {}", orderRequest);
        return orderServiceClient.createOrder(getHeaders(), orderRequest);
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDetailsDTO getOrder(@PathVariable String orderNumber) {
        log.info("Fetching order details for orderNumber: {}", orderNumber);
        return orderServiceClient.getOrder(getHeaders(), orderNumber);
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderSummaryDTO> getOrders() {
        log.info("Fetching orders");
        return orderServiceClient.getOrders(getHeaders());
    }

    private Map<String, String> getHeaders() {
        String accessToken = securityHelper.getAccessToken();
        Map<String, String> headers = Map.of("Authorization", "Bearer " + accessToken);
        log.info("headers: " + headers.get("Authorization"));
        return headers;
    }
}
