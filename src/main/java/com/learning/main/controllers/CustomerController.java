
package com.learning.main.controllers;
 

import com.learning.main.enm.PaymentMethod;
import com.learning.main.enm.TicketStatus;
import com.learning.main.enm.MembershipType;
import com.learning.main.model.*;
import com.learning.main.repository.services.CustomerService;
import com.learning.main.repository.services.GameService;
import com.learning.main.repository.services.TicketService;
import com.learning.main.repository.services.MembershipService;
import com.learning.main.repository.services.MerchandiseService;
import com.learning.main.repository.services.NewsService; // Import NewsService

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired; // Added for clarity, though constructor injection doesn't strictly require it on the constructor itself

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
//Dependency Injection Pattern
// Services are injected via constructor, promoting loose coupling.

@Controller
@RequestMapping("/customer")
public class CustomerController {

	private final CustomerService customerService;
	private final TicketService ticketService;
	private final GameService gameService;
	private final MembershipService membershipService;
	private final MerchandiseService merchandiseService;
	private final NewsService newsService; // Declare NewsService

	// Update constructor to include MerchandiseService and NewsService
	@Autowired // Optional but good practice for clarity on constructor injection
	public CustomerController(CustomerService customerService, TicketService ticketService, GameService gameService,
			MembershipService membershipService, MerchandiseService merchandiseService, NewsService newsService) { // Add NewsService here
		this.customerService = customerService;
		this.ticketService = ticketService;
		this.gameService = gameService;
		this.membershipService = membershipService;
		this.merchandiseService = merchandiseService;
		this.newsService = newsService; // Assign injected NewsService
	}
	
	// --- NEW Endpoints for News ---

		@GetMapping("/news")
		public String showAllNews(Model model, HttpSession session) {
			User currentUser = (User) session.getAttribute("user");
			if (currentUser == null) {
				return "redirect:/login";
			}

			List<News> allNews = newsService.getAllNews();

			model.addAttribute("pageTitle", "Latest Club News");
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("newsArticles", allNews);

			return "customer/news-list";
		}
	
	@GetMapping("/news/{newsId}")
	public String showNewsDetails(@PathVariable Long newsId, Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<News> news = newsService.getNewsById(newsId);
		if (news.isEmpty()) {
			return "redirect:/customer/news?error=News article not found";
		}

		model.addAttribute("pageTitle", news.get().getTitle());
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("news", news.get());

		return "customer/news-details";
	}

	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern,
	// handling web requests and preparing model data for views.
	// Service Layer Pattern
	// It delegates business logic to various service classes (customerService,
	// ticketService, membershipService).

	@GetMapping("/dashboard")
	public String showCustomerDashboard(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer customer = customerService.findByUser(currentUser);
		List<TicketPurchase> purchases = customerService.getCustomerPurchases(customer.getCustomerId());
		List<Game> upcomingGames = ticketService.getUpcomingGames();
		Optional<Membership> activeMembership = membershipService.findActiveMembershipByCustomer(customer);

		model.addAttribute("pageTitle", "Customer Dashboard");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("customer", customer);
		model.addAttribute("purchases", purchases);
		model.addAttribute("upcomingGames", upcomingGames);
		model.addAttribute("activeMembership", activeMembership.orElse(null));

		return "customer/dashboard";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the customerService.

	@GetMapping("/tickets")
	public String showCustomerTickets(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer customer = customerService.findByUser(currentUser);
		List<TicketPurchase> purchases = customerService.getCustomerPurchases(customer.getCustomerId());

		model.addAttribute("pageTitle", "My Tickets");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("purchases", purchases);

		return "customer/tickets";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the customerService and membershipService.

	@GetMapping("/profile")
	public String showCustomerProfile(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer customer = customerService.findByUser(currentUser);
		Optional<Membership> activeMembership = membershipService.findActiveMembershipByCustomer(customer);

		model.addAttribute("pageTitle", "My Profile");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("customer", customer);
		model.addAttribute("activeMembership", activeMembership.orElse(null));

		return "customer/profile";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the customerService.

	@PostMapping("/profile/update")
	public String updateCustomerProfile(@ModelAttribute Customer updatedCustomer, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer existingCustomer = customerService.findByUser(currentUser);
		existingCustomer.setFirstName(updatedCustomer.getFirstName());
		existingCustomer.setLastName(updatedCustomer.getLastName());
		existingCustomer.setPhone(updatedCustomer.getPhone());
		existingCustomer.setAddress(updatedCustomer.getAddress());

		customerService.saveCustomer(existingCustomer);

		return "redirect:/customer/profile?success";
	}

	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the ticketService and customerService.

	@GetMapping("/tickets/{purchaseId}")
	public String showPurchaseDetails(@PathVariable Long purchaseId, Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<TicketPurchase> purchase = ticketService.getPurchaseById(purchaseId);
		if (purchase.isEmpty()) {
			return "redirect:/customer/tickets?error=Purchase not found";
		}

		Customer customer = customerService.findByUser(currentUser);
		if (!purchase.get().getCustomer().getCustomerId().equals(customer.getCustomerId())) {
			return "redirect:/customer/tickets?error=Unauthorized access";
		}

		model.addAttribute("pageTitle", "Purchase Details");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("purchase", purchase.get());

		return "customer/purchase-details";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the gameService.

	@GetMapping("/games/{gameId}")
	public String showGameDetails(@PathVariable Long gameId, Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<Game> game = gameService.getGameById(gameId);
		if (game.isEmpty()) {
			return "redirect:/customer/dashboard?error=Game not found";
		}

		model.addAttribute("pageTitle", "Game Details");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("game", game.get());

		return "customer/game-details";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the gameService and ticketService.

	@GetMapping("/tickets/game/{gameId}")
	public String showGameTickets(@PathVariable Long gameId, Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Optional<Game> game = gameService.getGameById(gameId);
		if (game.isEmpty()) {
			return "redirect:/customer/dashboard?error=Game not found";
		}

		List<Ticket> availableTickets = ticketService.getAvailableTicketsForGame(gameId);

		model.addAttribute("pageTitle", "Buy Tickets");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("game", game.get());
		model.addAttribute("tickets", availableTickets);

		return "customer/buy-tickets";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the customerService and ticketService.

	@PostMapping("/tickets/purchase")
	public String purchaseTicket(@RequestParam Long ticketId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer customer = customerService.findByUser(currentUser);
		Optional<Ticket> ticketOpt = ticketService.getTicketById(ticketId);

		if (ticketOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Ticket not found");
			return "redirect:/customer/dashboard";
		}

		Ticket ticket = ticketOpt.get();

		if (ticket.getStatus() == TicketStatus.SOLD) {
			redirectAttributes.addFlashAttribute("error", "Ticket is already sold");
			return "redirect:/customer/tickets/game/" + ticket.getGame().getGameId();
		}

		TicketPurchase purchase = new TicketPurchase();
		purchase.setCustomer(customer);
		purchase.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		purchase.setTotalAmount(ticket.getPrice());

		purchase.addTicket(ticket);
		ticket.setStatus(TicketStatus.SOLD);

		ticketService.savePurchase(purchase);

		redirectAttributes.addFlashAttribute("success", "Ticket purchased successfully!");
		return "redirect:/customer/tickets";
	}

	// --- Membership Endpoints ---
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the customerService and membershipService.

	@GetMapping("/membership")
	public String showMembershipDetails(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer customer = customerService.findByUser(currentUser);
		Optional<Membership> activeMembership = membershipService.findActiveMembershipByCustomer(customer);

		model.addAttribute("pageTitle", "My Membership");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("activeMembership", activeMembership.orElse(null));

		return "customer/membership-details";
	}

	@GetMapping("/membership/join")
	public String showJoinMembershipForm(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer customer = customerService.findByUser(currentUser);
		Optional<Membership> activeMembership = membershipService.findActiveMembershipByCustomer(customer);
		if (activeMembership.isPresent()) {
			return "redirect:/customer/membership?error=You already have an active membership.";
		}

		model.addAttribute("pageTitle", "Join Membership");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("membershipTypes", MembershipType.values());
		return "customer/join-membership";
	}
	// MVC Pattern
	// This method acts as a controller in the Model-View-Controller pattern.
	// Service Layer Pattern
	// It delegates business logic to the customerService and membershipService.

	@PostMapping("/membership/join")
	public String joinMembership(@RequestParam("membershipType") String membershipTypeStr, HttpSession session,
			RedirectAttributes redirectAttributes) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		Customer customer = customerService.findByUser(currentUser);

		if (membershipService.findActiveMembershipByCustomer(customer).isPresent()) {
			redirectAttributes.addFlashAttribute("error", "You already have an active membership.");
			return "redirect:/customer/membership";
		}

		try {
			MembershipType membershipType = MembershipType.valueOf(membershipTypeStr);
			Membership newMembership = membershipService.createNewMembership(customer, membershipType);
			membershipService.saveMembership(newMembership);

			redirectAttributes.addFlashAttribute("success", "Membership joined successfully!");
			return "redirect:/customer/membership";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", "Invalid membership type selected.");
			return "redirect:/customer/membership/join";
		}
	}

	// --- New Endpoints for Schedule and Merchandise ---

	@GetMapping("/schedule")
	public String showGameSchedule(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		List<Game> upcomingGames = gameService.getUpcomingGames(); // Fetches all upcoming games

		model.addAttribute("pageTitle", "Game Schedule");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("games", upcomingGames); // Use "games" to avoid conflict with "upcomingGames" in dashboard

		return "customer/schedule";
	}

	@GetMapping("/merchandise")
	public String showMerchandise(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}

		List<Merchandise> allMerchandise = merchandiseService.getAllMerchandise(); // Fetches all merchandise

		model.addAttribute("pageTitle", "Official Merchandise");
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("merchandiseList", allMerchandise);

		return "customer/merchandise";
	}
} 