package com.learning.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BasketballClubManagementSystem extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BasketballClubManagementSystem.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// 🟩 GOF Pattern: Builder Pattern
		// This uses SpringApplicationBuilder to fluently configure the application
		// context.
		return application.sources(BasketballClubManagementSystem.class);
	}
}
