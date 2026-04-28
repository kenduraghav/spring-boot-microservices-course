package com.example.bookstore.order.domain;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.bookstore.order.domain.models.Address;
import com.example.bookstore.order.domain.models.Customer;
import com.example.bookstore.order.domain.models.OrderStatus;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
	@SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", allocationSize = 50)
	private long id;

	@Column(name = "order_number", nullable = false)
	private String orderNumber;

	@Column(name = "username", nullable = false)
	private String userName;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderItemEntity> items;

	@Embedded
	@AttributeOverrides(value= {
			@AttributeOverride(name = "name", column = @Column(name = "customer_name", nullable = false)),
			@AttributeOverride(name = "email", column = @Column(name = "customer_email", nullable = false)),
			@AttributeOverride(name = "phone", column = @Column(name = "customer_phone", nullable = false))
	})
	private Customer customer;
	
	@Embedded
	@AttributeOverrides(value= {
			@AttributeOverride(name = "addressLine1", column = @Column(name = "delivery_address_line1", nullable = false)),
			@AttributeOverride(name = "addressLine2", column = @Column(name = "delivery_address_line2")),
			@AttributeOverride(name = "city", column = @Column(name = "delivery_address_city", nullable = false)),
			@AttributeOverride(name = "state", column = @Column(name = "delivery_address_state", nullable = false)),
			@AttributeOverride(name = "zipCode", column = @Column(name = "delivery_address_zipcode", nullable = false)),
			@AttributeOverride(name = "country", column = @Column(name = "delivery_address_country", nullable = false))
	})
	private Address address;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	private String comments;
	
	@Column(name = "create_at", nullable = false, updatable = false)	
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name ="updated_at")
	private LocalDateTime updatedAt;

}