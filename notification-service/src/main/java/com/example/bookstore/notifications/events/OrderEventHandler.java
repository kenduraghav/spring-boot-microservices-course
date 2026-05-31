package com.example.bookstore.notifications.events;

import com.example.bookstore.notifications.domain.NotificationService;
import com.example.bookstore.notifications.domain.OrderEventRepository;
import com.example.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.example.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.example.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.example.bookstore.notifications.domain.models.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository repository;

    OrderEventHandler(NotificationService notificationService, OrderEventRepository repository) {
        this.notificationService = notificationService;
        this.repository = repository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Handling OrderCreatedEvent: " + event);
        notificationService.sendOrderCreatedNotification(event);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Handling OrderDeliveredEvent: " + event);
        notificationService.sendOrderDeliveredNotification(event);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Handling OrderCancelledEvent: " + event);
        notificationService.sendOrderCancelledEventNotification(event);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        log.info("Handling OrderCancelledEvent: " + event);
        notificationService.sendOrderErrorEvent(event);
    }
}
