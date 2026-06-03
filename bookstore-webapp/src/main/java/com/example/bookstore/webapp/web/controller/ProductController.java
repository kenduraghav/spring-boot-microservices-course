package com.example.bookstore.webapp.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bookstore.webapp.clients.catalog.CatalogServiceClient;
import com.example.bookstore.webapp.clients.catalog.PagedResult;
import com.example.bookstore.webapp.clients.catalog.Product;

@Controller
class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	private final CatalogServiceClient catalogClient;

	ProductController(CatalogServiceClient catalogClient) {
		this.catalogClient = catalogClient;
	}

	@GetMapping
	String index() {
		return "redirect:/products";
	}

	@GetMapping("/products")
	String showProductsPage(@RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
		model.addAttribute("pageNo", pageNo);
		return "products";
	}

	@GetMapping("/api/products")
	@ResponseBody
	PagedResult<Product> products(@RequestParam(defaultValue = "1") int page, Model model) {
		log.info("Fetching products for page: {}", page);
		return catalogClient.getProducts(page);
	}
}
