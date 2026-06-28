// File: AdminController.java
package com.learning.main.controllers;

import com.learning.main.enm.ERole;
import com.learning.main.model.*;
import com.learning.main.repository.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime; // Import LocalDateTime
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private GameService gameService; // New Autowired service

	@Autowired
	private TeamService teamService; // New Autowired service

	@Autowired
	private FacilityService facilityService; // New Autowired service

	@GetMapping("/dashboard")
	public String showAdminDashboard(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("pageTitle", "Admin Dashboard");
		model.addAttribute("currentUser", currentUser);

		// Fetch dynamic data for dashboard
		long totalUsers = userService.getAllUsers().size(); // Get total users
		LocalDateTime now = LocalDateTime.now();

		// Fetch all games and filter for upcoming games
		List<Game> allGames = gameService.getAllGames();
		List<Game> upcomingGames = allGames.stream()
				.filter(game -> game.getGameDateTime() != null && game.getGameDateTime().isAfter(now))
				.sorted(Comparator.comparing(Game::getGameDateTime)).collect(Collectors.toList());

		long upcomingGamesCount = upcomingGames.size(); // Get count of upcoming games
		long activeTeamsCount = teamService.getAllTeams().size(); // Get total teams
		long totalFacilities = facilityService.getAllFacilities().size(); // Get total facilities

		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("upcomingGamesCount", upcomingGamesCount);
		model.addAttribute("upcomingGames", upcomingGames); // Add upcoming games list to the model
		model.addAttribute("activeTeamsCount", activeTeamsCount);
		model.addAttribute("totalFacilities", totalFacilities);

		return "admin/dashboard";
	}

	@GetMapping("/game")
	public String manageGames(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("pageTitle", "Game Management");
		return "admin/games";
	}

	@GetMapping("/teams")
	public String manageTeams(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("pageTitle", "Team Management");
		return "admin/team";
	}

	@GetMapping("/facilitie")
	public String manageFacilities(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("pageTitle", "Facility Management");
		return "admin/facilities";
	}

	@GetMapping("/reports")
	public String viewReports(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("pageTitle", "System Reports");
		return "admin/reports";
	}

	@GetMapping("/settings")
	public String systemSettings(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("pageTitle", "System Settings");
		return "admin/settings";
	}

	////////////

	@GetMapping("/users")
	public String manageUsers(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		List<User> users = userService.getAllUsers();
		List<Role> roles = roleService.getAllRoles();

		model.addAttribute("pageTitle", "User Management");
		model.addAttribute("users", users);
		model.addAttribute("roles", roles);
		model.addAttribute("newUser", new User());
		model.addAttribute("currentUser", currentUser);
		return "admin/users";
	}

	@PostMapping("/users/add")
	public String addUser(@ModelAttribute("newUser") User user, @RequestParam("role") ERole role, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		// Set default password and encode it
		user.setPassword("defaultPassword");
		user.setEnabled(true);

		// Set role
		Role userRole = roleService.findRoleByName(role)
				.orElseThrow(() -> new RuntimeException("Error: Role not found."));
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		user.setRoles(roles);

		// Save user
		User savedUser = userService.saveUser(user);

		// Create corresponding profile based on role
		switch (role) {
		case ROLE_ADMIN:
			Admin admin = new Admin();
			admin.setUser(savedUser);
			adminService.saveAdmin(admin);
			break;
		case ROLE_PLAYER:
			Player player = new Player();
			player.setUser(savedUser);
			playerService.savePlayer(player);
			break;
		case ROLE_STAFF:
			Staff staff = new Staff();
			staff.setUser(savedUser);
			staffService.saveStaff(staff);
			break;
		case ROLE_CUSTOMER:
			Customer customer = new Customer();
			customer.setUser(savedUser);
			customer.setEmail(savedUser.getEmail());
			customerService.saveCustomer(customer);
			break;
		}

		return "redirect:/admin/users";
	}

	@GetMapping("/users/edit/{id}")
	public String showEditUserForm(@PathVariable Long id, Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
		List<Role> roles = roleService.getAllRoles();

		model.addAttribute("pageTitle", "Edit User");
		model.addAttribute("user", user);
		model.addAttribute("roles", roles);
		model.addAttribute("currentUser", currentUser);
		return "admin/edit-user";
	}

	@PostMapping("/users/update/{id}")
	public String updateUser(@PathVariable Long id, @ModelAttribute("user") User userDetails,
			@RequestParam(value = "selectedRoles", required = false) Set<ERole> selectedERoles, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));

		// Update user details
		user.setUsername(userDetails.getUsername());
		user.setEmail(userDetails.getEmail());
		user.setEnabled(userDetails.isEnabled());

		// Update roles
		user.getRoles().clear(); // Clear existing roles
		if (selectedERoles != null && !selectedERoles.isEmpty()) {
			Set<Role> newRoles = new HashSet<>();
			for (ERole eRole : selectedERoles) { // Corrected line: ensure opening brace is present if multiple
													// statements
				Role roleEntity = roleService.findRoleByName(eRole)
						.orElseThrow(() -> new RuntimeException("Error: Role '" + eRole.name() + "' not found."));
				newRoles.add(roleEntity);
			} // Corrected line: ensure closing brace is present for the for loop
			user.setRoles(newRoles);
		} else {
			// Optional: Handle case where no roles are selected.
			// Consider adding a default role or validation if a user must have at least one
			// role.
		}

		userService.saveUser(user);
		return "redirect:/admin/users";
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable Long id, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		userService.deleteUser(id);
		return "redirect:/admin/users";
	}

	@GetMapping("/users/reset-password/{id}")
	public String resetPassword(@PathVariable Long id, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));

		// Reset password to default and encode it
		user.setPassword("defaultPassword");
		userService.saveUser(user);

		return "redirect:/admin/users";
	}
}