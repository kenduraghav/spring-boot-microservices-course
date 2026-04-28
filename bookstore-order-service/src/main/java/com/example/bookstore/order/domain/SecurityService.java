package com.example.bookstore.order.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {
	
	public String getCurrentUser() {
		return "testUser";
	}
}
