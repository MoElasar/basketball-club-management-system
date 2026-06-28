package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//Service Layer Pattern
	// This class acts as a service layer providing business logic for Admin entities.

@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;

	public Admin saveAdmin(Admin admin) {
		return adminRepository.save(admin);
	}

	public Optional<Admin> getAdminById(Long id) {
		return adminRepository.findById(id);
	}

	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}

	public void deleteAdmin(Long id) {
		adminRepository.deleteById(id);
	}

	public Optional<Admin> findAdminByUser(User user) {
		return adminRepository.findByUser(user);
	}
}
