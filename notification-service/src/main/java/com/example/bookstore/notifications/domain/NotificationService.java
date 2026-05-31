package com.example.bookstore.notifications.domain;

import com.example.bookstore.notifications.ApplicationProperties;
import com.example.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.example.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.example.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.example.bookstore.notifications.domain.models.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final ApplicationProperties properties;

    NotificationService(ApplicationProperties properties, JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        log.info("Sending notification for OrderCreatedEvent: {}", event.orderNumber());

        String message =
                """
				==================
				New Order Created:
				==================
				
				Dear %s,
				
				Order created with Order Number: %s.
				
				Thank you for shopping with us!
				
				Best regards,
				Bookstore Team
				"""
                        .formatted(event.customer().name(), event.orderNumber());

        sendEmail(event.customer().email(), "Order Created Successfully: %s".formatted(event.orderNumber()), message);
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
        log.info("Sending notification for OrderDeliveredEvent: {}", event.orderNumber());

        String message =
                """
				==================
				Order Delivered:
				==================
				
				Dear %s,
				
				Your Order delivered successfully with Order Number: %s.
				
				Thank you for shopping with us!
				
				Best regards,
				Bookstore Team
				"""
                        .formatted(event.customer().name(), event.orderNumber());

        sendEmail(event.customer().email(), "Order Delivered Successfully: %s".formatted(event.orderNumber()), message);
    }

    public void sendOrderCancelledEventNotification(OrderCancelledEvent event) {
        log.info("Sending notification for OrderCancelledEvent: {}", event.orderNumber());

        String message =
                """
				==================
				Order Cancelled:
				==================
				
				Dear %s,
				
				Your Order with Order Number: %s has been cancelled.
				
				Reason: %s
				
				Best regards,
				Bookstore Team
				"""
                        .formatted(event.customer().name(), event.orderNumber(), event.reason());

        sendEmail(event.customer().email(), "Order Cancelled", message);
    }

    public void sendOrderErrorEvent(OrderErrorEvent event) {
        log.info("Sending notification for OrderErrorEvent: {}", event.orderNumber());

        String message =
                """
				==================
				Error Order:
				==================
				
				Dear Support Team,
				
				Order with Order Number: %s has encountered an error.
				
				Reason: %s
				
				Best regards,
				Bookstore Team
				"""
                        .formatted(event.customer().name(), event.orderNumber(), event.errorMessage());

        sendEmail(properties.supportEmail(), "Unable to Place Order Error", message);
    }

    private void sendEmail(String email, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(properties.supportEmail());
            mailSender.send(mimeMessage);
            log.info("Email sent to {} successfully", email);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
        }
    }
}
