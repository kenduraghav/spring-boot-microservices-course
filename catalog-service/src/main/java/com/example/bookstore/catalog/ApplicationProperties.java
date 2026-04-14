package com.example.bookstore.catalog;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import jakarta.validation.constraints.Min;

@ConfigurationProperties(prefix="catalog")
public record ApplicationProperties(
		@DefaultValue("10")
		@Min(1)
		int pageSize
		) {

}
