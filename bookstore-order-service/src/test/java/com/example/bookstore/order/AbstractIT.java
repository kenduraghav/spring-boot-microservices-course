package com.example.bookstore.order;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIT {

    @LocalServerPort
    int port;

    static WireMockContainer wireMockServer = new WireMockContainer("wiremock/wiremock:3.6.0-alpine");

    @BeforeAll
    static void beforeAll() {
        wireMockServer.start();
        configureFor(wireMockServer.getHost(), wireMockServer.getPort());
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url", wireMockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected void mockGetProductByCode(String code, String name, Double price) {
        stubFor(get(urlMatching("/api/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                """
								{
									"code": "%s",
									"name": "%s",
									"price": %s
								}
								"""
                                        .formatted(code, name, price))));
    }
}
