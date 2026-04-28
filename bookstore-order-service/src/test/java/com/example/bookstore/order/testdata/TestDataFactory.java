package com.example.bookstore.order.testdata;

import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.util.Set;

import org.instancio.Instancio;

import com.example.bookstore.order.domain.models.Address;
import com.example.bookstore.order.domain.models.CreateOrderRequest;
import com.example.bookstore.order.domain.models.Customer;
import com.example.bookstore.order.domain.models.OrderItem;

public class TestDataFactory {

	public static CreateOrderRequest validOrder() {
	    return Instancio.of(CreateOrderRequest.class)
	            .generate(field(OrderItem::quantity), gen -> gen.ints().range(1, 100))
	            .generate(field(OrderItem::price), gen -> gen.math().bigDecimal().min(BigDecimal.ONE))
	            .generate(field(Customer::name), gen -> gen.string().prefix("Customer-"))
	            .generate(field(Customer::email), gen -> gen.net().email())
	            .generate(field(Customer::phone), gen -> gen.string().digits().length(10))
	            .generate(field(Address::addressLine1), gen -> gen.string().prefix("Street-"))
	            .generate(field(Address::city), gen -> gen.oneOf("Chennai", "Mumbai", "Delhi"))
	            .generate(field(Address::state), gen -> gen.oneOf("TN", "MH", "DL"))
	            .generate(field(Address::zipCode), gen -> gen.string().digits().length(6))
	            .generate(field(Address::country), gen -> gen.oneOf("India", "USA", "UK"))
	            .create();
	}

	public static CreateOrderRequest orderWithInvalidQuantity() {
	    return Instancio.of(CreateOrderRequest.class)
	            .generate(field(OrderItem::quantity), gen -> gen.ints().range(Integer.MIN_VALUE, 0))
	            .create();
	}

	public static CreateOrderRequest orderWithBlankCustomerName() {
	    return Instancio.of(CreateOrderRequest.class)
	            .generate(field(Customer::name), gen -> gen.string().length(0))
	            .create();
	}

	public static CreateOrderRequest orderWithBlankCity() {
	    return Instancio.of(CreateOrderRequest.class)
	            .generate(field(Address::city), gen -> gen.string().length(0))
	            .create();
	}

	public static CreateOrderRequest orderWithNullPrice() {
	    return Instancio.of(CreateOrderRequest.class)
	            .set(field(OrderItem::price), null)  // null stays as set()
	            .create();
	}
	
	public static CreateOrderRequest orderWithEmptyItems() {
	    return Instancio.of(CreateOrderRequest.class)
	            .generate(field(CreateOrderRequest::items), gen -> gen.collection().size(0))
	            .create();
	}
}
