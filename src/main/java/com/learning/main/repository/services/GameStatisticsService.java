package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameStatisticsService {
	@Autowired
	private GameStatisticsRepository gameStatisticsRepository;

	public GameStatistics saveGameStatistics(GameStatistics gameStatistics) {
		return gameStatisticsRepository.save(gameStatistics);
	}

	public Optional<GameStatistics> getGameStatisticsById(Long id) {
		return gameStatisticsRepository.findById(id);
	}

	public List<GameStatistics> getAllGameStatistics() {
		return gameStatisticsRepository.findAll();
	}

	public void deleteGameStatistics(Long id) {
		gameStatisticsRepository.deleteById(id);
	}
}
