package com.learning.main.repository;

import com.learning.main.model.GameStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStatisticsRepository extends JpaRepository<GameStatistics, Long> {
	GameStatistics findByGameGameId(Long gameId); // Changed from findByGameId to findByGameGameId
}