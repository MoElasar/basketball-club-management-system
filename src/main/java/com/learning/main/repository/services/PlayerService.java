package com.learning.main.repository.services;

import com.learning.main.enm.Position;
import com.learning.main.model.*;
import com.learning.main.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerService {

	private final PlayerRepository playerRepository;
	private final TeamService teamService;
	private final UserService userService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository, TeamService teamService, UserService userService) {
		this.playerRepository = playerRepository;
		this.teamService = teamService;
		this.userService = userService;
	}

	// Player CRUD operations
	public Player savePlayer(Player player) {
		return playerRepository.save(player);
	}

	public Optional<Player> getPlayerById(Long id) {
		return playerRepository.findById(id);
	}

	public List<Player> getAllPlayers() {
		return playerRepository.findAll();
	}

	public void deletePlayer(Long id) {
		playerRepository.deleteById(id);
	}

	// Player query methods
	public List<Player> findPlayersByTeam(Team team) {
		return playerRepository.findByTeam(team);
	}

	public Optional<Player> findPlayerByUser(User user) {
		return playerRepository.findByUser(user);
	}

	public List<Player> findPlayersByPosition(Position position) {
		return playerRepository.findByPosition(position);
	}

	public List<Player> findPlayersByJerseyNumber(int jerseyNumber) {
		return playerRepository.findByJerseyNumber(jerseyNumber);
	}

	public boolean existsByJerseyNumberAndTeam(int jerseyNumber, Team team) {
		return playerRepository.existsByJerseyNumberAndTeam(jerseyNumber, team);
	}

	public List<Player> getTeamPlayers(Long teamId) {
		return playerRepository.findByTeam_TeamId(teamId);
	}

	public Optional<Player> findByUserOptional(User user) {
		return playerRepository.findByUser(user);
	}

	public Player findByUser(User user) {
		return playerRepository.findByUser(user)
				.orElseThrow(() -> new RuntimeException("Player not found for user: " + user.getUsername()));
	}

	// Team-related operations (delegated to TeamService)
	public List<Team> getAllTeams() {
		return teamService.getAllTeams();
	}

	public Optional<Team> getTeamById(Long teamId) {
		return teamService.getTeamById(teamId);
	}

	// User-related operations (delegated to UserService)
	public Optional<User> getUserById(Long userId) {
		return userService.getUserById(userId);
	}

	// Additional business logic methods
	public boolean isJerseyNumberAvailable(int jerseyNumber, Long teamId) {
		if (teamId == null) {
			// If no team is selected, jersey number is considered available
			return true;
		}
		return !playerRepository.existsByJerseyNumberAndTeam_TeamId(jerseyNumber, teamId);
	}

	public Player createPlayerProfile(User user, Player playerDetails) {
		if (findByUserOptional(user).isPresent()) {
			throw new IllegalStateException("Player profile already exists for this user");
		}

		playerDetails.setUser(user);
		playerDetails.setJoinDate(java.time.LocalDate.now());
		return savePlayer(playerDetails);
	}
}