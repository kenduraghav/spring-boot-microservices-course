package com.example.bookstore.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BookstoreOrderServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(BookstoreOrderServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BookstoreOrderServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(ApplicationContext ctx, ConnectionFactory connectionFactory) {
        return args -> {
            log.info("RabbitMQConfig bean present: {}", ctx.containsBean("rabbitMQConfig"));
            log.info("Exchange bean present: {}", ctx.containsBean("exchange"));
            log.info("newOrdersQueue bean present: {}", ctx.containsBean("newOrdersQueue"));
            log.info("RabbitTemplate bean present: {}", ctx.containsBean("rabbitTemplate"));
            log.info("RabbitAdmin bean present: {}", ctx.containsBean("rabbitAdmin"));

            // Add this part
            try {
                var connection = connectionFactory.createConnection();
                log.info("RabbitMQ connected: {}", connection.isOpen());
                connection.close();
            } catch (Exception e) {
                log.error("RabbitMQ connection FAILED: {}", e.getMessage(), e);
            }
        };
    }
}
