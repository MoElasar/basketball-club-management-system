package com.learning.main.repository.services;

import com.learning.main.enm.GameStatus;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
	@Autowired
	private GameRepository gameRepository;

	public Game saveGame(Game game) {
		return gameRepository.save(game);
	}

	public Optional<Game> getGameById(Long id) {
		return gameRepository.findById(id);
	}

	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	public void deleteGame(Long id) {
		gameRepository.deleteById(id);
	}

	public List<Game> findGamesByHomeTeam(Team team) {
		return gameRepository.findByHomeTeam(team);
	}

	public List<Game> findGamesByAwayTeam(Team team) {
		return gameRepository.findByAwayTeam(team);
	}

	public List<Game> findGamesByStatus(GameStatus status) {
		return gameRepository.findByStatus(status);
	}

	public List<Game> findGamesByGameDateTimeBetween(LocalDateTime start, LocalDateTime end) {
		return gameRepository.findByGameDateTimeBetween(start, end);
	}

	public List<Game> getGamesBetweenDates(LocalDateTime start, LocalDateTime end) {
		return gameRepository.findByGameDateTimeBetween(start, end);
	}

	public boolean existsByHomeTeamAndAwayTeamAndGameDateTime(Team homeTeam, Team awayTeam,
			LocalDateTime gameDateTime) {
		return gameRepository.findByHomeTeamAndAwayTeamAndGameDateTime(homeTeam, awayTeam, gameDateTime).isPresent();
	}

	;

	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public List<Game> getUpcomingGames() {
		return gameRepository.findByGameDateTimeAfterOrderByGameDateTimeAsc(LocalDateTime.now());
	}

	public List<Game> getUpcomingGamesForTeam(Long teamId) {
		return gameRepository.findUpcomingGamesForTeam(teamId, LocalDateTime.now());
	}

	public List<Game> getTeamSchedule(Long teamId) {
		return gameRepository.findByHomeTeam_TeamIdOrAwayTeam_TeamIdOrderByGameDateTimeAsc(teamId, teamId);

	}
}