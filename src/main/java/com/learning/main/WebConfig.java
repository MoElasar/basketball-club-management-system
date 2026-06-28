package com.learning.main;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	// 🟩 GOF Pattern: Strategy Pattern
	// Different endpoint mappings (strategies) are configured dynamically for
	// /api/** and /auth/**

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// CORS configuration for /api/** endpoints
		registry.addMapping("/api/**").allowedOrigins("http://localhost:3000")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS").allowedHeaders("*")
				.exposedHeaders("Authorization", "Content-Disposition").allowCredentials(true).maxAge(3600);

		// CORS configuration for /auth/** endpoints (e.g., /auth/me, /auth/login,
		// /auth/signup)
		registry.addMapping("/auth/**") // Added mapping for authentication endpoints
				.allowedOrigins("http://localhost:3000")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS").allowedHeaders("*")
				.exposedHeaders("Authorization", "Content-Disposition").allowCredentials(true).maxAge(3600);
	}

	// 🟩 GOF Pattern: Facade Pattern
	// Abstracts the complexity of static file handling behind simple configuration.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Resource handler for serving uploaded files
		registry.addResourceHandler("/api/files/**").addResourceLocations("file:uploads/").setCachePeriod(3600)
				.resourceChain(true);
	}
}