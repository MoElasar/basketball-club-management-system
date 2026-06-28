package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Player;
import com.learning.main.model.PlayerContract;

import java.time.LocalDate;

import java.util.List;


public interface PlayerContractRepository extends JpaRepository<PlayerContract, Long> {
	List<PlayerContract> findByPlayer(Player player);
    List<PlayerContract> findByStartDateAfter(LocalDate date);
    List<PlayerContract> findByEndDateBefore(LocalDate date);
}
