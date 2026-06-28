package com.learning.main.controllers;

import com.learning.main.enm.InjuryStatus;
import com.learning.main.model.*;
import com.learning.main.repository.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Import for file upload
import jakarta.servlet.http.HttpSession;

import java.io.IOException; // For handling file operations
import java.nio.file.Files; // For handling file operations
import java.nio.file.Path; // For handling file operations
import java.nio.file.Paths; // For handling file operations
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Import for Optional

@Controller
@RequestMapping("/staff")
public class StaffController {

	private final StaffService staffService;
	private final TrainingSessionService trainingSessionService;
	private final PlayerService playerService;
	private final PlayerInjuryService playerInjuryService;
	private final TeamService teamService;

	// Define the upload directory for profile images
	private static String UPLOAD_DIR = "src/main/resources/static/images/staff_profiles/";

	public StaffController(StaffService staffService, TrainingSessionService trainingSessionService,
			PlayerService playerService, PlayerInjuryService playerInjuryService, TeamService teamService) {
		this.staffService = staffService;
		this.trainingSessionService = trainingSessionService;
		this.playerService = playerService;
		this.playerInjuryService = playerInjuryService;
		this.teamService = teamService;
	}

	@GetMapping("/dashboard")
	public String showStaffDashboard(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Staff staff = staffService.findStaffByUser(currentUser)
				.orElseThrow(() -> new RuntimeException("Staff profile not found"));

		List<TrainingSession> upcomingSessions = trainingSessionService
				.findTrainingSessionsBySessionDateTimeBetween(LocalDateTime.now(), LocalDateTime.now().plusWeeks(1));

		List<Player> teamPlayers = playerService.findPlayersByTeam(staff.getTeam());

		List<PlayerInjury> recentInjuries = playerInjuryService
				.findPlayerInjuriesByInjuryDateBetween(LocalDate.now().minusWeeks(2), LocalDate.now());

		// Add role badge class for styling
		String roleBadgeClass = "badge-other";
		if (staff.getRole() != null) {
			switch (staff.getRole()) {
			case HEAD_COACH:
				roleBadgeClass = "badge-coach";
				break;
			case PHYSICAL_TRAINER:
				roleBadgeClass = "badge-trainer";
				break;
			case MEDICAL_STAFF:
				roleBadgeClass = "badge-medical";
				break;
			default:
				roleBadgeClass = "badge-other";
			}
		}

		model.addAttribute("pageTitle", "Staff Dashboard");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("staff", staff);
		model.addAttribute("upcomingSessions", upcomingSessions);
		model.addAttribute("teamPlayers", teamPlayers);
		model.addAttribute("recentInjuries", recentInjuries);
		model.addAttribute("roleBadgeClass", roleBadgeClass);

		return "staff/dashboard";
	}

	@GetMapping("/profile")
	public String showProfile(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Staff staff = staffService.findStaffByUser(currentUser)
				.orElseThrow(() -> new RuntimeException("Staff profile not found"));

		model.addAttribute("pageTitle", "My Profile");
		model.addAttribute("staff", staff);
		return "staff/profile";
	}

	@PostMapping("/profile")
	public String updateProfile(@ModelAttribute Staff staff, @RequestParam("imageFile") MultipartFile imageFile, // For
																													// image
																													// upload
			Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		// Fetch existing staff to preserve user and team relationships
		Optional<Staff> existingStaffOptional = staffService.findStaffByUser(currentUser);
		if (existingStaffOptional.isEmpty()) {
			throw new RuntimeException("Staff profile not found for update.");
		}
		Staff existingStaff = existingStaffOptional.get();

		// Update fields that are editable
		existingStaff.setFirstName(staff.getFirstName());
		existingStaff.setLastName(staff.getLastName());
		existingStaff.setSpecialization(staff.getSpecialization());

		// Handle profile image upload
		if (!imageFile.isEmpty()) {
			try {
				// Ensure the upload directory exists
				Path uploadPath = Paths.get(UPLOAD_DIR);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				String fileName = existingStaff.getStaffId() + "_" + imageFile.getOriginalFilename();
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(imageFile.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				existingStaff.setProfileImage("/images/staff_profiles/" + fileName); // Save relative path
			} catch (IOException e) {
				model.addAttribute("errorMessage", "Failed to upload profile image: " + e.getMessage());
				model.addAttribute("pageTitle", "My Profile");
				model.addAttribute("staff", existingStaff); // Pass back the staff object even on error
				return "staff/profile";
			}
		}

		staffService.saveStaff(existingStaff);
		model.addAttribute("successMessage", "Profile updated successfully!");
		return "redirect:/staff/profile";
	}

	@GetMapping("/training-sessions")
	public String showTrainingSessions(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Staff staff = staffService.findStaffByUser(currentUser)
				.orElseThrow(() -> new RuntimeException("Staff profile not found"));

		List<TrainingSession> sessions = trainingSessionService.findTrainingSessionsByConductedBy(staff);

		model.addAttribute("pageTitle", "Training Sessions");
		model.addAttribute("sessions", sessions); // ✅ Use "sessions"
		return "staff/training-sessions";
	}

	@GetMapping("/players")
	public String showTeamPlayers(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Staff staff = staffService.findStaffByUser(currentUser)
				.orElseThrow(() -> new RuntimeException("Staff profile not found"));

		List<Player> players = playerService.findPlayersByTeam(staff.getTeam());

		model.addAttribute("pageTitle", "Team Players");
		model.addAttribute("players", players);
		return "staff/players";
	}

	@GetMapping("/injuries")
	public String showPlayerInjuries(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Staff staff = staffService.findStaffByUser(currentUser)
				.orElseThrow(() -> new RuntimeException("Staff profile not found"));

		List<PlayerInjury> injuries = playerInjuryService.findPlayerInjuriesByPlayer_Team(staff.getTeam());

		model.addAttribute("pageTitle", "Player Injuries");
		model.addAttribute("injuries", injuries);
		return "staff/injuries";
	}

	@GetMapping("/training-sessions/new")
	public String showNewTrainingSessionForm(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Staff staff = staffService.findStaffByUser(currentUser)
				.orElseThrow(() -> new RuntimeException("Staff profile not found"));

		TrainingSession trainingSession = new TrainingSession();
		// The conductedBy field will be set on form submission based on the logged-in
		// staff
		// No need to set it here explicitly for the new form, as it will be overwritten
		// in saveTrainingSession
		trainingSession.setConductedBy(staff); // Set the staff for initial display or pre-filling if needed, but the
												// save method will handle the actual association.

		model.addAttribute("pageTitle", "New Training Session");
		model.addAttribute("trainingSession", trainingSession);
		model.addAttribute("teamPlayers", playerService.findPlayersByTeam(staff.getTeam()));
		return "staff/training-session-form";
	}

	@PostMapping("/training-sessions/save")
	public String saveTrainingSession(@ModelAttribute TrainingSession trainingSession, HttpSession session,
			Model model) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		// Fetch the Staff object associated with the current user to ensure correct
		// "conductedBy"
		Staff staff = staffService.findStaffByUser(currentUser).orElseThrow(
				() -> new RuntimeException("Staff profile not found for current user. Cannot save training session."));

		// Set the fetched Staff object to the trainingSession's conductedBy field
		trainingSession.setConductedBy(staff);

		trainingSessionService.saveTrainingSession(trainingSession);
		model.addAttribute("successMessage", "Training session created successfully!");
		return "redirect:/staff/training-sessions";
	}

	@GetMapping("/players/{playerId}")
	public String showPlayerDetails(@PathVariable Long playerId, Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Player player = playerService.getPlayerById(playerId)
				.orElseThrow(() -> new RuntimeException("Player not found"));

		model.addAttribute("pageTitle", "Player Details");
		model.addAttribute("player", player);
		return "staff/player-details";
	}

	@GetMapping("/training-sessions/{sessionId}")
	public String showTrainingSessionDetails(@PathVariable Long sessionId, Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		TrainingSession trainingSession = trainingSessionService.getTrainingSessionById(sessionId)
				.orElseThrow(() -> new RuntimeException("Training Session not found"));

		model.addAttribute("pageTitle", "Training Session Details");
		model.addAttribute("trainingSession", trainingSession);
		return "staff/training-session-details";
	}

	@GetMapping("/injuries/new")
	public String showNewInjuryForm(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Staff staff = staffService.findStaffByUser(currentUser)
				.orElseThrow(() -> new RuntimeException("Staff profile not found"));

		// Create new injury object for the form
		PlayerInjury injury = new PlayerInjury();
		injury.setInjuryDate(LocalDate.now()); // Default to today's date

		// Get list of players from the staff's team
		List<Player> teamPlayers = playerService.findPlayersByTeam(staff.getTeam());

		// Debugging: Log the number of players retrieved
		System.out.println("Number of players retrieved: " + teamPlayers.size());

		model.addAttribute("pageTitle", "New Injury Report");
		model.addAttribute("injury", injury);
		model.addAttribute("teamPlayers", teamPlayers);
		model.addAttribute("injuryStatuses", InjuryStatus.values());

		return "staff/injury-form";
	}

	@PostMapping("/injuries/save")
	public String saveInjury(@ModelAttribute("injury") PlayerInjury injury, HttpSession session, Model model) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		// The player should already be bound to the injury object
		if (injury.getPlayer() == null || injury.getPlayer().getPlayerId() == null) {
			model.addAttribute("errorMessage", "Please select a player");
			return "redirect:/staff/injuries/new";
		}

		playerInjuryService.savePlayerInjury(injury);
		model.addAttribute("successMessage", "Injury report saved successfully!");
		return "redirect:/staff/injuries";
	}

	@GetMapping("/injuries/{injuryId}")
	public String showInjuryDetails(@PathVariable Long injuryId, Model model, HttpSession session) {
	    User currentUser = (User) session.getAttribute("user");
	    if (currentUser == null) {
	        return "redirect:/login";
	    }

	    PlayerInjury injury = playerInjuryService.getPlayerInjuryById(injuryId)
	            .orElseThrow(() -> new RuntimeException("Injury not found"));

	    model.addAttribute("pageTitle", "Injury Details");
	    model.addAttribute("injury", injury);
	    return "staff/injury-details";
	}
}