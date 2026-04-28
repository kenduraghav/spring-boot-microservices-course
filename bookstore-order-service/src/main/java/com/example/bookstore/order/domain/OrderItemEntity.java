package com.example.bookstore.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_seq")
    @SequenceGenerator(name = "order_item_id_seq", sequenceName = "order_item_id_seq", allocationSize = 50)
    private long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    private int quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
