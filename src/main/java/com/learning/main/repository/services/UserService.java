package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public Optional<User> findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Boolean existsUserByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public Boolean existsUserByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public List<User> getUsersNotLinkedToPlayers() {
	    List<User> allUsers = userRepository.findAll();
	    return allUsers.stream()
	        .filter(user -> user.getPlayer() == null) // assuming `User` has `@OneToOne(mappedBy = "user") private Player player;`
	        .collect(Collectors.toList());
	}

}