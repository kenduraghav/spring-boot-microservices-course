package com.example.bookstore.order.domain;

import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.CreateOrderResponse;
import com.example.bookstore.order.domain.models.OrderCreatedEvent;

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
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
		this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(@Valid CreateOrderRequest request, String userName) {
        orderValidator.validateOrder(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Order created with Order Number: {}", savedOrder.getOrderNumber());
        
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.toOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
