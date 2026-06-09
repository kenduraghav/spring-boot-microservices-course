package com.example.bookstore.order.web.controller;

import static com.example.bookstore.order.testdata.TestDataFactory.orderWithBlankCity;
import static com.example.bookstore.order.testdata.TestDataFactory.orderWithBlankCustomerName;
import static com.example.bookstore.order.testdata.TestDataFactory.orderWithEmptyItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.example.bookstore.order.domain.OrderService;
import com.example.bookstore.order.domain.SecurityService;
import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
class OrderControllerUnitTest {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private SecurityService securityService;

    @Autowired
    MockMvcTester mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        given(securityService.getCurrentUser()).willReturn("testuser");
    }

    @ParameterizedTest(name = "[{index}]- {0}")
    @MethodSource("createOrderRequestProvider")
    @WithMockUser
    void shouldReturnBadRequest_givenInvalidOrderRequest(CreateOrderRequest request) throws Exception {
        System.out.println("Order Request:" + request);
        var result = mockMvc.post()
                .uri("/api/orders")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                Arguments.of(named("Order with Blank Customer Name", orderWithBlankCustomerName())),
                Arguments.of(named("Order with Empty Items", orderWithEmptyItems())),
                Arguments.of(named("Order with Blank City", orderWithBlankCity())));
    }
}
