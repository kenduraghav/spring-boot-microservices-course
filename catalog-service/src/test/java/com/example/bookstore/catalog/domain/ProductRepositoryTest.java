package com.example.bookstore.catalog.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {"spring.test.database.replace=NONE", "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"})
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void shouldReturnProduct_givenExistingProductCode() {

        ProductEntity product = productRepository.findByCode("P100").orElseThrow();

        assertThat(product.getCode()).isEqualTo("P100");
        assertThat(product.getName()).isEqualTo("The Hunger Games");
    }

    @Test
    void shouldReturnEmpty_givenNonExistingProductCode() {

        assertThat(productRepository.findByCode("P999")).isEmpty();
    }
}
