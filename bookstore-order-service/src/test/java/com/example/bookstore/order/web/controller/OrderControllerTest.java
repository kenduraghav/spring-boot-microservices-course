package com.example.bookstore.order.web.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.example.bookstore.order.AbstractIT;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTest {

        @Test
        void shouldCreateOrderSuccessfully() {
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
}
