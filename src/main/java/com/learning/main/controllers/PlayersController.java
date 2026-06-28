
package com.learning.main.controllers;

import com.learning.main.enm.Position;
import com.learning.main.model.Player;
import com.learning.main.model.Team;
import com.learning.main.model.User;
import com.learning.main.repository.services.PlayerService;
import com.learning.main.repository.services.TeamService; // Assuming TeamService exists and is used here.
import com.learning.main.repository.services.UserService; // Assuming UserService exists and is used here.
import com.learning.main.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/players")
public class PlayersController {
	// Dependency Injection Pattern
	// Services are injected via @Autowired, promoting loose coupling.

	@Autowired
	private PlayerService playerService;
	@Autowired
	private TeamService teamService; // To fetch available teams for dropdowns
	@Autowired
	private UserService userService; // To fetch available users for association
	@Autowired
	private FileStorageService fileStorageService;

	private boolean isAuthenticated(HttpSession session) {
		return session.getAttribute("user") != null;
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern,
	// handling web requests and preparing model data for views.
	// Service Layer Pattern
	// It delegates business logic to various service classes (playerService,
	// teamService, userService).

	@GetMapping
	public String managePlayers(Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		User currentUser = (User) session.getAttribute("user");
		List<Player> players = playerService.getAllPlayers();
		List<Team> teams = teamService.getAllTeams(); // For team selection in add/edit form
		List<User> users = userService.getUsersNotLinkedToPlayers(); // For user association in add/edit form

		model.addAttribute("pageTitle", "Player Management");
		model.addAttribute("players", players);
		model.addAttribute("newPlayer", new Player()); // For the add new player form
		model.addAttribute("teams", teams);
		model.addAttribute("positions", Position.values()); // Pass enum values for dropdown
		model.addAttribute("users", users);
		model.addAttribute("currentUser", currentUser);
		return "admin/players"; // This will be the main players listing page (e.g., admin/players.html)
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to playerService, teamService, userService, and
	// fileStorageService.

	@PostMapping("/add")
	public String addPlayer(@ModelAttribute("newPlayer") Player player, @RequestParam("teamId") Long teamId,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam("profileImageFile") MultipartFile profileImageFile, HttpSession session, Model model) {

		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		// Handle file upload
		if (!profileImageFile.isEmpty()) {
			try {
				String fileName = fileStorageService.storeFile(profileImageFile);
				player.setProfileImage("/api/files/" + fileName); // Store the URL path
			} catch (Exception e) {
				model.addAttribute("error", "Failed to upload profile image: " + e.getMessage());
				return showPlayerFormWithError(model, session, player);
			}
		}

		Optional<Team> teamOptional = teamService.getTeamById(teamId);
		if (teamOptional.isEmpty()) {
			model.addAttribute("error", "Team not found.");
			// Re-populate model for error display
			model.addAttribute("players", playerService.getAllPlayers());
			model.addAttribute("teams", teamService.getAllTeams());
			model.addAttribute("positions", Position.values());
			model.addAttribute("users", userService.getAllUsers());
			model.addAttribute("newPlayer", player); // Keep entered data
			model.addAttribute("pageTitle", "Player Management");
			model.addAttribute("currentUser", (User) session.getAttribute("user"));
			return "admin/players";
		}
		player.setTeam(teamOptional.get());

		if (userId != null) {
			Optional<User> userOptional = userService.getUserById(userId);
			userOptional.ifPresent(player::setUser);
		}

		// Validate jersey number uniqueness within the selected team
		if (playerService.existsByJerseyNumberAndTeam(player.getJerseyNumber(), player.getTeam())) {
			model.addAttribute("error", "Player with this jersey number already exists in this team.");
			// Re-populate model for error display
			model.addAttribute("players", playerService.getAllPlayers());
			model.addAttribute("teams", teamService.getAllTeams());
			model.addAttribute("positions", Position.values());
			model.addAttribute("users", userService.getAllUsers());
			model.addAttribute("newPlayer", player); // Keep entered data
			model.addAttribute("pageTitle", "Player Management");
			model.addAttribute("currentUser", (User) session.getAttribute("user"));
			return "admin/players";
		}

		// Set join date if not already set (e.g., from a form field)
		if (player.getJoinDate() == null) {
			player.setJoinDate(LocalDate.now());
		}

		playerService.savePlayer(player);
		return "redirect:/admin/players?success=add";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to playerService, teamService, and userService.

	@GetMapping("/edit/{id}")
	public String showEditPlayerForm(@PathVariable Long id, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		User currentUser = (User) session.getAttribute("user");
		Optional<Player> player = playerService.getPlayerById(id);
		if (player.isEmpty()) {
			return "redirect:/admin/players?error=notFound";
		}
		model.addAttribute("pageTitle", "Edit Player");
		model.addAttribute("player", player.get());
		model.addAttribute("teams", teamService.getAllTeams()); // For team selection
		model.addAttribute("positions", Position.values()); // For position selection
		model.addAttribute("users", userService.getAllUsers()); // For user association
		model.addAttribute("currentUser", currentUser);
		return "admin/edit-player"; // Form for editing an existing player (e.g., admin/edit-player.html)
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to playerService, teamService, userService, and
	// fileStorageService.

	@PostMapping("/update/{id}")
	public String updatePlayer(@PathVariable Long id, @ModelAttribute("player") Player playerDetails,
			@RequestParam("teamId") Long teamId, @RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
			@RequestParam(value = "removeImage", required = false) boolean removeImage, HttpSession session,
			Model model) {

		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Player existingPlayer = playerService.getPlayerById(id)
				.orElseThrow(() -> new RuntimeException("Player not found for this id :: " + id));

		// Handle image removal if checkbox is checked
		if (removeImage && existingPlayer.getProfileImage() != null) {
			String oldFileName = existingPlayer.getProfileImage().replace("/api/files/", "");
			fileStorageService.deleteFile(oldFileName);
			existingPlayer.setProfileImage(null);
		}

		// Handle new file upload
		if (profileImageFile != null && !profileImageFile.isEmpty()) {
			try {
				// Delete old image if exists
				if (existingPlayer.getProfileImage() != null) {
					String oldFileName = existingPlayer.getProfileImage().replace("/api/files/", "");
					fileStorageService.deleteFile(oldFileName);
				}

				// Store new image
				String fileName = fileStorageService.storeFile(profileImageFile);
				existingPlayer.setProfileImage("/api/files/" + fileName);
			} catch (Exception e) {
				model.addAttribute("error", "Failed to upload profile image: " + e.getMessage());
				return showEditFormWithError(model, session, existingPlayer);
			}

		}

		Optional<Team> teamOptional = teamService.getTeamById(teamId);
		if (teamOptional.isEmpty()) {
			model.addAttribute("error", "Team not found.");
			// Re-populate model for error display
			model.addAttribute("player", existingPlayer); // Show existing data
			model.addAttribute("teams", teamService.getAllTeams());
			model.addAttribute("positions", Position.values());
			model.addAttribute("users", userService.getAllUsers());
			model.addAttribute("pageTitle", "Edit Player");
			model.addAttribute("currentUser", (User) session.getAttribute("user"));
			return "admin/edit-player";
		}
		existingPlayer.setTeam(teamOptional.get());

		if (userId != null) {
			Optional<User> userOptional = userService.getUserById(userId);
			existingPlayer.setUser(userOptional.orElse(null)); // Set null if user not found
		} else {
			existingPlayer.setUser(null); // Allow un-associating user
		}

		// Validate jersey number uniqueness for update (exclude current player)
		// This checks if the new jersey number + team combination already exists for
		// *another* player.
		// It correctly handles cases where a player's own jersey number or team is
		// unchanged.
		if (playerService.existsByJerseyNumberAndTeam(playerDetails.getJerseyNumber(), existingPlayer.getTeam())
				&& !(existingPlayer.getJerseyNumber() == playerDetails.getJerseyNumber()
						&& existingPlayer.getTeam().getTeamId().equals(teamId))) {
			model.addAttribute("error", "Player with this jersey number already exists in this team.");
			// Re-populate model for error display
			model.addAttribute("player", existingPlayer); // Show existing data
			model.addAttribute("teams", teamService.getAllTeams());
			model.addAttribute("positions", Position.values());
			model.addAttribute("users", userService.getAllUsers());
			model.addAttribute("pageTitle", "Edit Player");
			model.addAttribute("currentUser", (User) session.getAttribute("user"));
			return "admin/edit-player";
		}

		// Update player details
		existingPlayer.setFirstName(playerDetails.getFirstName());
		existingPlayer.setLastName(playerDetails.getLastName());
		existingPlayer.setDateOfBirth(playerDetails.getDateOfBirth());
		existingPlayer.setPosition(playerDetails.getPosition());
		existingPlayer.setNationality(playerDetails.getNationality());
		existingPlayer.setJerseyNumber(playerDetails.getJerseyNumber());
		existingPlayer.setHeight(playerDetails.getHeight());
		existingPlayer.setWeight(playerDetails.getWeight());
		existingPlayer.setJoinDate(playerDetails.getJoinDate());
		existingPlayer.setProfileImage(playerDetails.getProfileImage());

		playerService.savePlayer(existingPlayer);
		return "redirect:/admin/players?success=update";
	}

	@GetMapping("/delete/{id}")
	public String deletePlayer(@PathVariable Long id, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		playerService.deletePlayer(id);
		return "redirect:/admin/players?success=delete";
	}
	// Helper method to show player form with error, part of MVC.

	private String showPlayerFormWithError(Model model, HttpSession session, Player player) {
		User currentUser = (User) session.getAttribute("user");
		model.addAttribute("players", playerService.getAllPlayers());
		model.addAttribute("teams", teamService.getAllTeams());
		model.addAttribute("positions", Position.values());
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("newPlayer", player);
		model.addAttribute("pageTitle", "Player Management");
		model.addAttribute("currentUser", currentUser);
		return "admin/players";
	}
	// Helper method to show edit form with error, part of MVC.

	private String showEditFormWithError(Model model, HttpSession session, Player player) {
		User currentUser = (User) session.getAttribute("user");
		model.addAttribute("player", player);
		model.addAttribute("teams", teamService.getAllTeams());
		model.addAttribute("positions", Position.values());
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("pageTitle", "Edit Player");
		model.addAttribute("currentUser", currentUser);
		return "admin/edit-player";
	}
}