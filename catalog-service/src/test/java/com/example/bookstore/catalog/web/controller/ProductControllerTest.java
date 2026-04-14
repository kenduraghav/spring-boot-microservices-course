package com.example.bookstore.catalog.web.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import com.example.bookstore.catalog.AbstractIT;
import com.example.bookstore.catalog.domain.Product;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class ProductControllerTest extends AbstractIT {

    @Test
    void shouldReturnAllProducts() {

        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("data", hasSize(10))
                .body("totalElements", is(15))
                .body("totalPages", is(2))
                .body("pageNumber", is(1))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    void shouldReturnProduct_givenValidProductCode() {

        Product product = given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}", "P100")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .as(Product.class);

        assertThat(product.code()).isEqualTo("P100");
        assertThat(product.price()).isEqualTo(34.0);
    }

    @Test
    void shouldReturnNotFound_givenInvalidProductCode() {

        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}", "INVALID")
                .then()
                .statusCode(404)
                .body("title", is("Product Not Found"))
                .body("detail", is("Product with code INVALID not found"));
    }
}
