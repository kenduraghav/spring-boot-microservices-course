package com.example.bookstore.webapp.clients.orders;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface OrderServiceClient {

    @PostExchange("/orders/api/orders")
    OrderConfirmationDTO createOrder(
            @RequestBody CreateOrderRequest orderRequest);

    @GetExchange("/orders/api/orders")
    List<OrderSummaryDTO> getOrders();

    @GetExchange("/orders/api/orders/{orderNumber}")
    OrderDetailsDTO getOrder(@PathVariable String orderNumber);
}
