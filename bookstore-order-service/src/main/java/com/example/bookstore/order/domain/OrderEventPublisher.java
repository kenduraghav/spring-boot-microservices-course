package com.example.bookstore.order.domain;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.bookstore.order.ApplicationProperties;
import com.example.bookstore.order.domain.models.OrderCreatedEvent;

@Component
class OrderEventPublisher {
	
	private final ApplicationProperties properties;
	private final RabbitTemplate template;
	
	OrderEventPublisher(RabbitTemplate template, ApplicationProperties properties){
		this.properties = properties;
		this.template = template;
	}
	
	public void publish(OrderCreatedEvent event) {
		this.send(properties.newOrdersQueue(),event);
	}

	private void send(String routingkey, Object object) {
		this.template.convertAndSend(properties.orderEventsExchange(), routingkey, object);
	}
}
