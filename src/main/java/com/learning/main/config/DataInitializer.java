package com.learning.main.config;

import com.learning.main.enm.ERole;
import com.learning.main.model.Role;
import com.learning.main.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;

	public DataInitializer(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	// GOF Pattern: Singleton Pattern
		// Spring manages this as a singleton bean.
		// This also follows Factory behavior by conditionally creating `Role` objects.

	@Override
	public void run(String... args) throws Exception {
		// Create roles if they don't exist
		for (ERole role : ERole.values()) {
			if (!roleRepository.existsByName(role)) {
				Role newRole = new Role(role);
				roleRepository.save(newRole);
			}
		}
	}
}