package com.learning.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ============================================================
// MAIN CLASS — Entry Point of the Application
// ============================================================
// This is where your Spring Boot app STARTS.
//
// @SpringBootApplication does 3 things:
//   1. @Configuration     — This class can define beans (objects managed by Spring)
//   2. @EnableAutoConfiguration — Spring auto-configures based on dependencies
//   3. @ComponentScan     — Spring scans this package (and sub-packages) to find
//                           @Controller, @Service, @Repository, etc.
//
// HOW TO RUN:
//   Option 1: Right-click this file → Run (in your IDE)
//   Option 2: Terminal → mvn spring-boot:run
//
// Once running, open: http://localhost:8080/api/students
// ============================================================

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Student API is running!");
        System.out.println("  Open: http://localhost:8080/api/students");
        System.out.println("==============================================");
    }
}
