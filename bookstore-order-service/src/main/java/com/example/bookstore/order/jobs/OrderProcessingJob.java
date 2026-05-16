package com.example.bookstore.order.jobs;

import com.example.bookstore.order.domain.OrderService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class OrderProcessingJob {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingJob.class);
    private final OrderService orderService;

    OrderProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${orders.process-orders-job-cron}")
    @SchedulerLock(name = "processsOrder")
    public void processsOrder() {
        LockAssert.assertLocked();
        log.info("Processing Orders for Order Status New: {}", Instant.now());
        orderService.processOrders();
    }
}
