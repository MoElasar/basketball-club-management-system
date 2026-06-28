package com.learning.main.repository.services;

import com.learning.main.enm.StaffRole;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class StaffService {
	@Autowired
	private StaffRepository staffRepository;

	public Staff saveStaff(Staff staff) {
		return staffRepository.save(staff);
	}

	public Optional<Staff> getStaffById(Long id) {
		return staffRepository.findById(id);
	}

	public List<Staff> getAllStaff() {
		return staffRepository.findAll();
	}

	public void deleteStaff(Long id) {
		staffRepository.deleteById(id);
	}

	public Optional<Staff> findStaffByUser(User user) {
		return staffRepository.findByUser(user);
	}

	public List<Staff> findStaffByRole(StaffRole role) {
		return staffRepository.findByRole(role);
	}

	public List<Staff> findStaffByTeam(Team team) {
		return staffRepository.findByTeam(team);
	}
}
