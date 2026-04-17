package com.example.bookstore.order;

import org.springframework.boot.SpringApplication;

public class TestBookstoreOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookstoreOrderServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
