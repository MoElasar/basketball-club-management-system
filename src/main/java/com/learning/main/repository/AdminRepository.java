package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.main.model.Admin;
import com.learning.main.model.User;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser(User user);
}
