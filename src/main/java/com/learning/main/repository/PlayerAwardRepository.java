package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Player;
import com.learning.main.model.PlayerAward;
import java.time.LocalDate;
import java.util.List;

public interface PlayerAwardRepository extends JpaRepository<PlayerAward, Long> {
	List<PlayerAward> findByPlayer(Player player);

	List<PlayerAward> findByAwardDateBetween(LocalDate start, LocalDate end);
}