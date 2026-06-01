package com.example.bookstore.notifications.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.bookstore.notifications.domain.NotificationService;
import com.example.bookstore.notifications.domain.OrderEventEntity;
import com.example.bookstore.notifications.domain.OrderEventRepository;
import com.example.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.example.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.example.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.example.bookstore.notifications.domain.models.OrderErrorEvent;

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
        if(repository.existsByEventId(event.eventId())) {
			log.warn("Duplicate OrderCreatedEvent received for order number: " + event.orderNumber());
			return;
		}
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        repository.save(entity);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Handling OrderDeliveredEvent: " + event);
        if(repository.existsByEventId(event.eventId())) {
			log.warn("Duplicate OrderDeliveredEvent received for order number: " + event.orderNumber());
			return;
		}
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        repository.save(entity);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Handling OrderCancelledEvent: " + event);
        if(repository.existsByEventId(event.eventId())) {
			log.warn("Duplicate OrderCancelledEvent received for order number: " + event.orderNumber());
			return;
		}
        notificationService.sendOrderCancelledEventNotification(event);
        OrderEventEntity entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        repository.save(entity);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        log.info("Handling OrderErrorEvent: " + event);
        if(repository.existsByEventId(event.eventId())) {
			log.warn("Duplicate OrderErrorEvent received for order number: " + event.orderNumber());
			return;
		}
        notificationService.sendOrderErrorEvent(event);
        OrderEventEntity entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        repository.save(entity);
    }
}
