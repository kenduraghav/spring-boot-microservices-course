package com.example.bookstore.order.domain;

import com.example.bookstore.order.catalog.Product;
import com.example.bookstore.order.catalog.ProductServiceClient;
import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.OrderItem;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class OrderValidator {

    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);
    private final ProductServiceClient productServiceClient;

    OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    void validateOrder(CreateOrderRequest orderRequest) {
        for (OrderItem item : orderRequest.items()) {

            Product product = productServiceClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product Code:" + item.code()));

            if (item.price().compareTo(BigDecimal.valueOf(product.price())) != 0) {
                log.error(
                        "Price mismatch for product code {}: expected {}, but got {}",
                        item.code(),
                        product.price(),
                        item.price());
                throw new InvalidOrderException("Price mismatch for product code " + item.code());
            }
        }
    }
}
