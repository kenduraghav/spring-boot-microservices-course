package com.example.bookstore.order.jobs;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bookstore.order.domain.OrderEventService;

@Component
class OrderEventPublishingJob {
	private final static Logger log = LoggerFactory.getLogger(OrderEventPublishingJob.class);
	private final OrderEventService orderEventService;
	
	OrderEventPublishingJob(OrderEventService orderEventService) {
		this.orderEventService = orderEventService;
	}
	
	
	@Scheduled(cron = "${orders.publish-order-events-job-cron}")
	public void publishOrderEvents() {
		log.info("Starting OrderEventPublishingJob... {}", Instant.now());
		orderEventService.publishOrderEvents();
	}
}
