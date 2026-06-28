package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.main.enm.StaffRole;
import com.learning.main.model.Staff;
import com.learning.main.model.Team;
import com.learning.main.model.User;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
	List<Staff> findByTeam(Team team);

	Optional<Staff> findByUser(User user);

	List<Staff> findByRole(StaffRole role);
}