package com.example.bookstore.order.domain;

import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.CreateOrderResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CreateOrderResponse createOrder(@Valid CreateOrderRequest request, String userName) {
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Order created with Order Number: {}", savedOrder.getOrderNumber());
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
