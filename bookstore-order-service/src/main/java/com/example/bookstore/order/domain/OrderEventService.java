package com.example.bookstore.order.domain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookstore.order.domain.models.OrderCreatedEvent;
import com.example.bookstore.order.domain.models.OrderEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class OrderEventService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

	private final OrderEventRepository orderEventRepository;
	private final ObjectMapper objectMapper;
	private final OrderEventPublisher eventPublisher;

	OrderEventService(OrderEventRepository orderEventRepository, ObjectMapper objectMapper,
			OrderEventPublisher eventPublisher) {
		this.orderEventRepository = orderEventRepository;
		this.objectMapper = objectMapper;
		this.eventPublisher = eventPublisher;
	}

	void save(OrderCreatedEvent event) {
		OrderEventEntity eventEntity = new OrderEventEntity();
		eventEntity.setEventId(event.eventId());
		eventEntity.setOrderNumber(event.orderNumber());
		eventEntity.setEventType(OrderEventType.ORDER_CREATED);
		eventEntity.setPayload(toJsonPayload(event));
		orderEventRepository.save(eventEntity);
	}

	public void publishOrderEvents() {
		Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
		//Use Pagination when data is intensive.
		List<OrderEventEntity> orderEvents = orderEventRepository.findAll(sort);

		for (OrderEventEntity event : orderEvents) {
			this.publishOrder(event);
			orderEventRepository.delete(event);
		}

	}

	private void publishOrder(OrderEventEntity entity) {
		OrderEventType type = entity.getEventType();

		switch (type) {
		case ORDER_CREATED:
			OrderCreatedEvent event = fromJsonPayload(entity.getPayload(), OrderCreatedEvent.class);
			eventPublisher.publish(event);
			break;
		default:
			log.warn("Event not supported yet");
		}
	}

	private String toJsonPayload(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException("Failed to serialize order event payload", e);
		}
	}

	private <T> T fromJsonPayload(String payload, Class<T> type) {
		try {
			return objectMapper.readValue(payload, type);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to parse the json payload", e);
		}
	}
}
