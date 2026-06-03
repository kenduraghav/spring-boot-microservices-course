package com.example.bookstore.order.domain;

import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.CreateOrderResponse;
import com.example.bookstore.order.domain.models.OrderCreatedEvent;
import com.example.bookstore.order.domain.models.OrderStatus;
import com.example.bookstore.order.domain.models.OrderSummaryDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private static final List<String> ALLOWED_COUNTRIES = List.of("INDIA", "SINGAPORE", "GERMANY", "UK");

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    public OrderService(
            OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
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

    public void processOrders() {
        log.info("Processing orders with status NEW...");
        List<OrderEntity> newOrders = orderRepository.findByStatus(OrderStatus.NEW);
        for (OrderEntity order : newOrders) {
            this.processOrder(order);
        }
    }

    public List<OrderSummaryDTO> getOrdersForUser(String currentUser) {
        return orderRepository.findByUserName(currentUser);
    }

    public com.example.bookstore.order.domain.models.OrderDetailsDTO getUserOrderDetails(
            String orderNumber, String currentUser) {
        OrderEntity order = orderRepository
                .findUserOrderDetails(orderNumber, currentUser)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found for order number: " + orderNumber + " and user: " + currentUser));
        return OrderMapper.toOrderDetailsDTO(order);
    }

    private void processOrder(OrderEntity order) {

        try {

            if (canBeDeliver(order)) {
                log.info(
                        "Order {} can be delivered to Country:{}",
                        order.getOrderNumber(),
                        order.getAddress().country());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.toOrderDeliveredEvent(order));
            } else {
                log.info(
                        "Order {} cannot be delivered to Country:{}",
                        order.getOrderNumber(),
                        order.getAddress().country());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(
                        OrderEventMapper.toOrderCancelledEvent(order, "Order cannot be delivered to this country"));
            }

        } catch (Exception e) {
            log.error("Error processing order {}: {}", order.getOrderNumber(), e.getMessage());
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.toOrderErrorEvent(order, "Error processing order"));
        }
    }

    private boolean canBeDeliver(OrderEntity order) {
        return ALLOWED_COUNTRIES.contains(order.getAddress().country().toUpperCase());
    }
}
