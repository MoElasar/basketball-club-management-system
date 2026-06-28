package com.learning.main.controllers;

import com.learning.main.enm.ERole;
import com.learning.main.model.*;
import com.learning.main.repository.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
//MVC Pattern
	// This class acts as a controller in the Model-View-Controller pattern, handling web requests and preparing model data for views.

@Controller
public class AuthController {

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

	@Autowired // Add this Autowired for AdminService
	private AdminService adminService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String showLoginForm(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			return "redirect:/dashboard";
		}

		if (error != null) {
			model.addAttribute("error", "Invalid username or password!");
		}
		if (logout != null) {
			model.addAttribute("message", "You have been logged out successfully.");
		}
		model.addAttribute("pageTitle", "Login - Basketball Club");
		return "login";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			return "redirect:/dashboard";
		}

		model.addAttribute("user", new User());
		model.addAttribute("pageTitle", "Register - Basketball Club");
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user, @RequestParam("accountType") String accountType,
			@RequestParam("confirmPassword") String confirmPassword, BindingResult bindingResult, Model model,
			HttpSession session) {
		if (session.getAttribute("user") != null) {
			return "redirect:/dashboard";
		}

		// Validate password match
		if (!user.getPassword().equals(confirmPassword)) {
			bindingResult.rejectValue("password", "error.user", "Passwords do not match");
		}

		// Check for existing username
		if (userService.existsUserByUsername(user.getUsername())) {
			bindingResult.rejectValue("username", "error.user", "Username already exists");
		}

		// Check for existing email
		if (userService.existsUserByEmail(user.getEmail())) {
			bindingResult.rejectValue("email", "error.user", "Email already registered");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("pageTitle", "Register - Basketball Club");
			return "register";
		}

		// Encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(true);

		// Set role based on account type
		ERole roleEnum;
		switch (accountType.toLowerCase()) {
		case "player":
			roleEnum = ERole.ROLE_PLAYER;
			break;
		case "staff":
			roleEnum = ERole.ROLE_STAFF;
			break;
		case "customer":
			roleEnum = ERole.ROLE_CUSTOMER;
			break;
		case "admin": // Add case for Admin
			roleEnum = ERole.ROLE_ADMIN;
			break;
		default:
			model.addAttribute("error", "Invalid account type selected.");
			model.addAttribute("user", user);
			return "register";
		}

		// Get or create the role
		Role role = roleService.findRoleByName(roleEnum).orElseGet(() -> {
			Role newRole = new Role(roleEnum);
			return roleService.saveRole(newRole);
		});

		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);

		// Save the user
		User savedUser = userService.saveUser(user);

		// Create and link the corresponding profile
		switch (accountType.toLowerCase()) {
		case "player":
			Player player = new Player();
			player.setUser(savedUser);
			playerService.savePlayer(player);
			break;
		case "staff":
			Staff staff = new Staff();
			staff.setUser(savedUser);
			staffService.saveStaff(staff);
			break;
		case "customer":
			Customer customer = new Customer();
			customer.setUser(savedUser);
			customer.setEmail(savedUser.getEmail());
			customerService.saveCustomer(customer);
			break;
		case "admin": // Add case for Admin profile creation
			Admin admin = new Admin();
			admin.setUser(savedUser);
			// If Admin model has other fields like fullName or phoneNumber, you might need
			// to set them here
			// For now, assuming only the user linkage is required for initial admin
			// creation
			adminService.saveAdmin(admin);
			break;
		}

		model.addAttribute("success", "Registration successful! Please login.");
		return "redirect:/login";
	}
	// GOF Pattern: Facade Pattern
			// SecurityContextLogoutHandler provides a simplified interface for the complex logout process.
			
	@GetMapping("/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.invalidate();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
				String username = auth.getName();
				user = userService.findUserByUsername(username).orElse(null);
				if (user != null) {
					session.setAttribute("user", user);
				}
			}
		}

		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("currentUser", user);

		if (user.getRoles() != null) {
			if (user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
				return "redirect:/admin/dashboard";
			} else if (user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_PLAYER)) {
				return "redirect:/player/dashboard";
			} else if (user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_STAFF)) {
				return "redirect:/staff/dashboard";
			} else if (user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_CUSTOMER)) {
				return "redirect:/customer/dashboard";
			}
		}

		return "redirect:/";
	}
}