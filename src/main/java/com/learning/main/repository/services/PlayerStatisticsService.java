package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class PlayerStatisticsService {
	@Autowired
	private PlayerStatisticsRepository playerStatisticsRepository;

	public PlayerStatistics savePlayerStatistics(PlayerStatistics playerStatistics) {
		return playerStatisticsRepository.save(playerStatistics);
	}

	public Optional<PlayerStatistics> getPlayerStatisticsById(Long id) {
		return playerStatisticsRepository.findById(id);
	}

	public List<PlayerStatistics> getAllPlayerStatistics() {
		return playerStatisticsRepository.findAll();
	}

	public void deletePlayerStatistics(Long id) {
		playerStatisticsRepository.deleteById(id);
	}

	public List<PlayerStatistics> findPlayerStatisticsByPlayer(Player player) {
		return playerStatisticsRepository.findByPlayer(player);
	}

	public List<PlayerStatistics> findPlayerStatisticsByGame(Game game) {
		return playerStatisticsRepository.findByGame(game);
	}

	public List<PlayerStatistics> findPlayerStatisticsByPlayerAndGame(Player player, Game game) {
		return playerStatisticsRepository.findByPlayerAndGame(player, game);
	}

	public List<PlayerStatistics> getAllPlayerStats(Long playerId) {
		return playerStatisticsRepository.findByPlayer_PlayerId(playerId);
	}

	public List<PlayerStatistics> getRecentPlayerStats(Long playerId) {
		return playerStatisticsRepository.findTop5ByPlayer_PlayerIdOrderByGame_GameDateTimeDesc(playerId);
	}

	public List<PlayerStatistics> getRecentPlayerStats(Long playerId, int limit) {
		return playerStatisticsRepository.findByPlayer_PlayerIdOrderByGame_GameDateTimeDesc(playerId,
				PageRequest.of(0, limit));
	}

	public PlayerStatistics getSeasonStats(Long playerId) {
		// Example logic: summarize all stats for the season
		List<PlayerStatistics> stats = playerStatisticsRepository.findByPlayer_PlayerId(playerId);
		PlayerStatistics summary = new PlayerStatistics();
		for (PlayerStatistics s : stats) {
			summary.setPoints(summary.getPoints() + s.getPoints());
			summary.setRebounds(summary.getRebounds() + s.getRebounds());
			// Add other fields similarly...
		}
		return summary;
	}

}