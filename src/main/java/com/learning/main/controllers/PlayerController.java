package com.learning.main.controllers;

import com.learning.main.model.*;
import com.learning.main.repository.services.GameService;
import com.learning.main.repository.services.PlayerService;
import com.learning.main.repository.services.PlayerStatisticsService;
import com.learning.main.enm.Position; // Import Position enum

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult; // For validation errors
import jakarta.validation.Valid; // For @Valid annotation
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections; // For empty lists
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/player")
public class PlayerController {

	private final PlayerService playerService;
	private final GameService gameService;
	private final PlayerStatisticsService statisticsService;

	public PlayerController(PlayerService playerService, GameService gameService,
			PlayerStatisticsService statisticsService) {
		this.playerService = playerService;
		this.gameService = gameService;
		this.statisticsService = statisticsService;
	}

	@GetMapping("/dashboard")
	public String showPlayerDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login"; // Redirect to login if no user in session
		}

		Optional<Player> playerOptional = playerService.findByUserOptional(currentUser);

		if (playerOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Player profile not found. Please create one.");
			return "redirect:/player/create-profile";
		}

		Player player = playerOptional.get();

		List<Game> upcomingGames = Collections.emptyList(); // Initialize as empty list
		if (player.getTeam() != null) { // [cite: 145, 154]
			upcomingGames = gameService.getUpcomingGamesForTeam(player.getTeam().getTeamId()); // [cite: 194]
		} else {
			// If player has no team, upcomingGames remains empty, and the UI will show "No
			// upcoming games scheduled."
			// This prevents NullPointerException.
		}

		List<PlayerStatistics> recentStats = statisticsService.getRecentPlayerStats(player.getPlayerId(), 5); // [cite:
																												// 194]
		PlayerStatistics seasonStats = statisticsService.getSeasonStats(player.getPlayerId()); // [cite: 194]

		model.addAttribute("pageTitle", "Player Dashboard");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("player", player);
		model.addAttribute("upcomingGames", upcomingGames);
		model.addAttribute("recentStats", recentStats);
		model.addAttribute("seasonStats", seasonStats);

		return "player/dashboard";
	}

	@GetMapping("/schedule")
	public String showPlayerSchedule(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<Player> playerOptional = playerService.findByUserOptional(currentUser);
		if (playerOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Player profile not found.");
			return "redirect:/player/create-profile";
		}
		Player player = playerOptional.get();

		List<Game> schedule = Collections.emptyList(); // Initialize as empty list
		if (player.getTeam() != null) { // [cite: 145, 154]
			schedule = gameService.getTeamSchedule(player.getTeam().getTeamId()); // [cite: 198]
		} else {
			// If player has no team, schedule remains empty.
		}

		model.addAttribute("pageTitle", "My Schedule");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("schedule", schedule);

		return "player/schedule";
	}

	@GetMapping("/stats")
	public String showPlayerStats(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<Player> playerOptional = playerService.findByUserOptional(currentUser);
		if (playerOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Player profile not found.");
			return "redirect:/player/create-profile";
		}
		Player player = playerOptional.get();

		List<PlayerStatistics> allStats = statisticsService.getAllPlayerStats(player.getPlayerId()); // [cite: 201]
		PlayerStatistics seasonStats = statisticsService.getSeasonStats(player.getPlayerId()); // [cite: 201]

		model.addAttribute("pageTitle", "My Statistics");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("allStats", allStats);
		model.addAttribute("seasonStats", seasonStats);

		return "player/stats";
	}

	@GetMapping("/team")
	public String showTeamInfo(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<Player> playerOptional = playerService.findByUserOptional(currentUser);
		if (playerOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Player profile not found.");
			return "redirect:/player/create-profile";
		}
		Player player = playerOptional.get();

		Team team = player.getTeam(); // [cite: 204]
		List<Player> teammates = Collections.emptyList(); // Initialize as empty list
		if (team != null) { // [cite: 145, 154]
			teammates = playerService.getTeamPlayers(team.getTeamId()); // [cite: 204]
		} else {
			// If player has no team, teammates remains empty.
		}

		model.addAttribute("pageTitle", "Team Information");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("team", team); // team can still be null if the player has no team, but the UI should handle
											// this
		model.addAttribute("teammates", teammates);

		return "player/team";
	}

	@GetMapping("/create-profile")
	public String showCreatePlayerProfileForm(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		if (playerService.findByUserOptional(currentUser).isPresent()) {
			return "redirect:/player/dashboard?alreadyExists";
		}

		model.addAttribute("player", new Player());
		model.addAttribute("pageTitle", "Create Player Profile");
		model.addAttribute("positions", Position.values()); // Add Position enum values for dropdown
		model.addAttribute("teams", playerService.getAllTeams()); // Add teams for selection [cite: 135, 205]

		return "player/create-profile";
	}

	@PostMapping("/create-profile")
	public String savePlayerProfile(@Valid @ModelAttribute("player") Player player, BindingResult bindingResult,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, // imageFile is optional
			HttpSession session, RedirectAttributes redirectAttributes, Model model) {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("pageTitle", "Create Player Profile");
			model.addAttribute("positions", Position.values());
			model.addAttribute("teams", playerService.getAllTeams()); // [cite: 135, 205]
			return "player/create-profile";
		}

		if (playerService.findByUserOptional(currentUser).isPresent()) {
			redirectAttributes.addFlashAttribute("errorMessage", "A player profile already exists for your account.");
			return "redirect:/player/dashboard";
		}

		try {
			player.setUser(currentUser); // [cite: 207]
			player.setJoinDate(LocalDate.now()); // [cite: 207]

			// Find full Team by ID only if a team ID was provided in the form
			if (player.getTeam() != null && player.getTeam().getTeamId() != null) { // [cite: 207]
				Optional<Team> fullTeam = playerService.getTeamById(player.getTeam().getTeamId()); // [cite: 207]
				fullTeam.ifPresent(player::setTeam); // [cite: 208]
			} else {
				// If no team selected, player.team remains null.
				// This is now handled gracefully in dashboard/schedule/team views.
			}

			// Handle image file upload (optional)
			if (imageFile != null && !imageFile.isEmpty()) { // [cite: 208]
				String uploadDir = "src/main/resources/static/images/player-profiles/";
				File dir = new File(uploadDir);
				if (!dir.exists())
					dir.mkdirs(); // [cite: 208]
				String fileName = currentUser.getId() + "_" + imageFile.getOriginalFilename(); // [cite: 209]
				String filePath = uploadDir + fileName;
				imageFile.transferTo(new File(filePath)); // [cite: 209]

				player.setProfileImage("/images/player-profiles/" + fileName); // [cite: 209]
			}

			playerService.savePlayer(player); //
			redirectAttributes.addFlashAttribute("successMessage", "Player profile created successfully!");
			return "redirect:/player/dashboard"; //
		} catch (IOException e) { // [cite: 210]
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload image: " + e.getMessage());
			return "redirect:/player/create-profile";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error creating profile: " + e.getMessage());
			return "redirect:/player/create-profile";
		}
	}

	@GetMapping("/profile")
	public String showPlayerProfile(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<Player> playerOptional = playerService.findByUserOptional(currentUser);
		if (playerOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Player profile not found. Please create one.");
			return "redirect:/player/create-profile";
		}

		model.addAttribute("player", playerOptional.get());
		model.addAttribute("positions", Position.values());
		model.addAttribute("teams", playerService.getAllTeams());
		model.addAttribute("pageTitle", "My Profile");
		return "player/profile";
	}

	@PostMapping("/profile")
	public String updatePlayerProfile(@Valid @ModelAttribute("player") Player updatedPlayer,
			BindingResult bindingResult, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			@RequestParam(value = "teamId", required = false) Long teamId, HttpSession session,
			RedirectAttributes redirectAttributes, Model model) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<Player> existingPlayerOptional = playerService.findByUserOptional(currentUser);
		if (existingPlayerOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Player profile not found for update.");
			return "redirect:/player/create-profile";
		}

		Player existingPlayer = existingPlayerOptional.get();

		if (bindingResult.hasErrors()) {
			model.addAttribute("pageTitle", "My Profile");
			model.addAttribute("positions", Position.values());
			model.addAttribute("teams", playerService.getAllTeams());
			updatedPlayer.setProfileImage(existingPlayer.getProfileImage());
			return "player/profile";
		}

		try {
			// Copy form values to existing player
			existingPlayer.setFirstName(updatedPlayer.getFirstName());
			existingPlayer.setLastName(updatedPlayer.getLastName());
			existingPlayer.setDateOfBirth(updatedPlayer.getDateOfBirth());
			existingPlayer.setNationality(updatedPlayer.getNationality());
			existingPlayer.setPosition(updatedPlayer.getPosition());
			existingPlayer.setJerseyNumber(updatedPlayer.getJerseyNumber());
			existingPlayer.setHeight(updatedPlayer.getHeight());
			existingPlayer.setWeight(updatedPlayer.getWeight());

			// Handle teamId to Team mapping
			if (teamId != null) {
				playerService.getTeamById(teamId).ifPresent(existingPlayer::setTeam);
			} else {
				existingPlayer.setTeam(null);
			}

			// Handle image upload
			if (imageFile != null && !imageFile.isEmpty()) {
				String uploadDir = "src/main/resources/static/images/player-profiles/";
				File dir = new File(uploadDir);
				if (!dir.exists())
					dir.mkdirs();

				String fileName = existingPlayer.getPlayerId() + "_" + imageFile.getOriginalFilename();
				imageFile.transferTo(new File(uploadDir + fileName));

				existingPlayer.setProfileImage("/images/player-profiles/" + fileName);
			}

			playerService.savePlayer(existingPlayer);
			redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
			return "redirect:/player/profile";

		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload image: " + e.getMessage());
			return "redirect:/player/profile";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
			return "redirect:/player/profile";
		}
	}
	 @GetMapping("/game/{gameId}")
	    public String showPlayerGameDetails(@PathVariable Long gameId, Model model, HttpSession session) {
	        User currentUser = (User) session.getAttribute("user");
	        if (currentUser == null) {
	            return "redirect:/login"; // Redirect to login if user not found in session
	        }

	        Optional<Game> gameOptional = gameService.getGameById(gameId);
	        if (gameOptional.isEmpty()) {
	            // Handle case where game is not found
	            return "redirect:/player/schedule?error=Game not found"; // Redirect with an error
	        }

	        Game game = gameOptional.get();

	        model.addAttribute("pageTitle", "Game Details");
	        model.addAttribute("currentUser", currentUser);
	        model.addAttribute("game", game);
	        // You might want to add player-specific stats for this game here
	        // Optional<PlayerStatistics> playerStatsForGame = playerStatisticsService.getStatisticsForPlayerAndGame(playerService.findByUser(currentUser), game);
	        // model.addAttribute("playerStatsForGame", playerStatsForGame.orElse(null));

	        return "player/game-details"; // This will map to src/main/resources/templates/player/game-details.html
	    }

}