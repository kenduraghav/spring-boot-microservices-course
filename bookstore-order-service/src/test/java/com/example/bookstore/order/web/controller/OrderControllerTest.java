package com.example.bookstore.order.web.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.example.bookstore.order.AbstractIT;
import com.example.bookstore.order.domain.models.OrderDetailsDTO;
import com.example.bookstore.order.domain.models.OrderSummaryDTO;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders-data.sql")
class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTest {

        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P1001", "Laptop", 75000.50);
            mockGetProductByCode("P2002", "Wireless Mouse", 1500.00);
            var payload =
                    """
										{
					    "items": [
					        {
					            "code": "P1001",
					            "name": "Laptop",
					            "price": 75000.50,
					            "quantity": 1
					        },
					        {
					            "code": "P2002",
					            "name": "Wireless Mouse",
					            "price": 1500.00,
					            "quantity": 2
					        }
					    ],
					    "customer": {
					        "name": "Raghav",
					        "email": "raghav@example.com",
					        "phone": "9876543210"
					    },
					    "deliveryAddress": {
					        "addressLine1": "123, Anna Salai",
					        "addressLine2": "Near Teynampet Signal",
					        "city": "Chennai",
					        "state": "Tamil Nadu",
					        "zipCode": "600118",
					        "country": "India"
					    }
					}
										""";

            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(201)
                    .body("orderNumber", is(notNullValue()));
        }

        @Test
        void shouldReturnBadRequest_givenInvalidPayLoad() {
            var payload =
                    """
										{
					    "items": [
					        {
					            "code": "P1001",
					            "name": "Laptop",
					            "price": 75000.50,
					            "quantity": 1
					        },
					        {
					            "code": "P2002",
					            "name": "Wireless Mouse",
					            "price": 1500.00,
					            "quantity": 2
					        }
					    ],
					    "customer": {
					        "name": "Raghav",
					        "email": "raghav@example.com",
					        "phone": "9876543210"
					    },
					    "deliveryAddress": {
					        "addressLine1": "123, Anna Salai",
					        "addressLine2": "Near Teynampet Signal",
					        "city": "Chennai",
					        "state": "Tamil Nadu",
					        "zipCode": "",
					        "country": ""
					    }
					}
										""";
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation Error"));
        }
    }

    @Nested
    class GetOrdersApiTest {
        @Test
        void shouldReturnOrderSummarySuccessfully() {
            List<OrderSummaryDTO> orders = given().contentType(ContentType.JSON)
                    .when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertThat(orders).isNotNull();
            assertThat(orders).hasSize(2);
        }
    }

    @Nested
    class GetOrderByOrderNumberApiTest {

        @Test
        void shouldReturnOrderDetailsSuccessfully_givenValidOrderNumber() {
            String orderNumber = "ORD-001";
            OrderDetailsDTO order = given().contentType(ContentType.JSON)
                    .when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertThat(order).isNotNull();
            assertThat(order.items()).hasSize(2);
        }
    }
}
