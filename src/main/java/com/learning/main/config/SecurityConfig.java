package com.learning.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/home", "/about", "/contact", "/login", "/register", "/css/**", "/js/**",
						"/images/**")
				.permitAll().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/player/**")
				.hasRole("PLAYER").requestMatchers("/staff/**").hasRole("STAFF").requestMatchers("/customer/**")
				.hasRole("CUSTOMER").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login") // This should match your form
																							// action
						.defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true")
						.invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll())
				.csrf().disable(); // For development only, enable in production
		// 🟩 GOF Pattern: Facade Pattern
		// Spring Security wraps complex authentication/authorization behind fluent DSL
		// methods.

		return http.build();
	}
	// 🟩 GOF Pattern: Strategy Pattern
	// BCryptPasswordEncoder is a pluggable encryption strategy for passwords.

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	// 🟩 GOF Pattern: Factory Method
	// AuthenticationManager is constructed internally via Spring’s configuration
	// factories.

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}