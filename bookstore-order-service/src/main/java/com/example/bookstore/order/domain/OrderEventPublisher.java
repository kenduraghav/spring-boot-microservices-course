package com.example.bookstore.order.domain;

import com.example.bookstore.order.ApplicationProperties;
import com.example.bookstore.order.domain.models.OrderCancelledEvent;
import com.example.bookstore.order.domain.models.OrderCreatedEvent;
import com.example.bookstore.order.domain.models.OrderDeliveredEvent;
import com.example.bookstore.order.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class OrderEventPublisher {

    private final ApplicationProperties properties;
    private final RabbitTemplate template;

    OrderEventPublisher(RabbitTemplate template, ApplicationProperties properties) {
        this.properties = properties;
        this.template = template;
    }

    void publish(OrderCreatedEvent event) {
        this.send(properties.newOrdersQueue(), event);
    }

    void publish(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    void publish(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersQueue(), event);
    }

    void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }

    private void send(String routingkey, Object object) {
        this.template.convertAndSend(properties.orderEventsExchange(), routingkey, object);
    }
}
