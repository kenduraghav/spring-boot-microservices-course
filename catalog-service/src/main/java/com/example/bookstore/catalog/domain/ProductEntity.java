package com.example.bookstore.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq")
    private Long id;

    @NotEmpty(message = "Product code must not be empty") @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotEmpty(message = "Product name must not be empty") @Column(name = "name", nullable = false)
    private String name;

    private String description;

    private String imageUrl;

    @NotNull(message = "Product price must not be null") @DecimalMin(value = "0.1") @Column(nullable = false)
    private Double price;
}
