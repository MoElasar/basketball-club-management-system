package com.learning.main.repository.services;

import com.learning.main.enm.ERole;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	public Optional<Role> getRoleById(Integer id) {
		return roleRepository.findById(id);
	}

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public void deleteRole(Integer id) {
		roleRepository.deleteById(id);
	}

	public Optional<Role> findRoleByName(ERole name) {
		return roleRepository.findByName(name);
	}
	
	public Boolean existsByName(ERole name) {
	    return roleRepository.existsByName(name);
	}
}
