package com.example.bookstore.catalog.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.catalog.domain.PagedResult;
import com.example.bookstore.catalog.domain.Product;
import com.example.bookstore.catalog.domain.ProductService;

@RestController
@RequestMapping("/api/products")
class ProductController {
	
	private final ProductService productService;
	
	ProductController(ProductService productService) {
		this.productService = productService;
	}
	

	@GetMapping
	PagedResult<Product> getAllProducts(@RequestParam(defaultValue = "1") int page) {
		return productService.getAllProducts(page);
	}
}
