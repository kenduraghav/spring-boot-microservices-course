package com.example.bookstore.webapp.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class OrderController {

	@GetMapping("/cart")
	String cart() {
		return "cart";
	}
}
