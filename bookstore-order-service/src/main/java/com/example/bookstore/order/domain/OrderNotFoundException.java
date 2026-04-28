package com.example.bookstore.order.domain;

public class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException forOrderNumber(String orderNumber) {
        return new OrderNotFoundException("Order with Number " + orderNumber + " not found");
    }
}
