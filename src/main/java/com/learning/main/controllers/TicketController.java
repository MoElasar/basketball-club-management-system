package com.learning.main.controllers;

import com.learning.main.enm.PaymentMethod;
import com.learning.main.enm.TicketStatus;
import com.learning.main.model.*;
import com.learning.main.repository.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class TicketController {

    private final TicketService ticketService;
    private final TicketPurchaseService ticketPurchaseService;
    private final GameService gameService;
    private final CustomerService customerService;

    @Autowired
    public TicketController(TicketService ticketService,
                          TicketPurchaseService ticketPurchaseService,
                          GameService gameService,
                          CustomerService customerService) {
        this.ticketService = ticketService;
        this.ticketPurchaseService = ticketPurchaseService;
        this.gameService = gameService;
        this.customerService = customerService;
    }

    // Helper method to check authentication
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    // Ticket Management Endpoints

    @GetMapping("/tickets")
    public String getAllTickets(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");
        List<Ticket> tickets = ticketService.getAllTickets();
        List<Game> games = gameService.getAllGames();

        model.addAttribute("pageTitle", "Ticket Management");
        model.addAttribute("tickets", tickets);
        model.addAttribute("games", games);
        model.addAttribute("statusValues", TicketStatus.values());
        model.addAttribute("ticket", new Ticket()); // For the form
        model.addAttribute("currentUser", currentUser);
        
        return "admin/tickets";
    }

    @GetMapping("/tickets/{id}")
    public String getTicketDetails(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        model.addAttribute("pageTitle", "Ticket Details");
        model.addAttribute("ticket", ticket);
        model.addAttribute("currentUser", currentUser);
        return "admin/ticket-details";
    }

    @GetMapping("/tickets/new")
    public String showCreateTicketForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");
        List<Game> games = gameService.getAllGames();

        model.addAttribute("pageTitle", "Create New Ticket");
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("games", games);
        model.addAttribute("statusValues", TicketStatus.values());
        model.addAttribute("currentUser", currentUser);
        return "admin/ticket-form";
    }

    @PostMapping("/tickets")
    public String createTicket(@ModelAttribute Ticket ticket,
                             @RequestParam Long gameId,
                             HttpSession session, Model model) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        try {
            Game game = gameService.getGameById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));
            ticket.setGame(game);

            if (ticket.getStatus() == null) {
                ticket.setStatus(TicketStatus.AVAILABLE);
            }

            ticketService.saveTicket(ticket);
            return "redirect:/admin/tickets?success=created";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating ticket: " + e.getMessage());
            return showCreateTicketForm(model, session);
        }
    }

    @GetMapping("/tickets/edit/{id}")
    public String showEditTicketForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        List<Game> games = gameService.getAllGames();

        model.addAttribute("pageTitle", "Edit Ticket");
        model.addAttribute("ticket", ticket);
        model.addAttribute("games", games);
        model.addAttribute("statusValues", TicketStatus.values());
        model.addAttribute("currentUser", currentUser);
        return "admin/ticket-form";
    }

    @PostMapping("/tickets/update/{id}")
    public String updateTicket(@PathVariable Long id,
                             @ModelAttribute Ticket ticket,
                             @RequestParam Long gameId,
                             HttpSession session, Model model) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        try {
            Ticket existingTicket = ticketService.getTicketById(id)
                    .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

            Game game = gameService.getGameById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));

            existingTicket.setGame(game);
            existingTicket.setSeatNumber(ticket.getSeatNumber());
            existingTicket.setSection(ticket.getSection());
            existingTicket.setPrice(ticket.getPrice());
            existingTicket.setStatus(ticket.getStatus());

            ticketService.saveTicket(existingTicket);
            return "redirect:/admin/tickets?success=updated";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating ticket: " + e.getMessage());
            return showEditTicketForm(id, model, session);
        }
    }

    @GetMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        try {
            ticketService.deleteTicket(id);
            return "redirect:/admin/tickets?success=deleted";
        } catch (Exception e) {
            return "redirect:/admin/tickets?error=delete_failed";
        }
    }

    // Ticket Purchase Management Endpoints

    @GetMapping("/ticket-purchases")
    public String getAllTicketPurchases(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");
        List<TicketPurchase> purchases = ticketPurchaseService.getAllTicketPurchases();

        model.addAttribute("pageTitle", "All Ticket Purchases");
        model.addAttribute("ticketPurchases", purchases);
        model.addAttribute("currentUser", currentUser);
        return "admin/ticket-purchases";
    }

    @GetMapping("/ticket-purchases/{id}")
    public String getTicketPurchaseDetails(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");
        TicketPurchase purchase = ticketPurchaseService.getTicketPurchaseById(id)
                .orElseThrow(() -> new RuntimeException("Ticket purchase not found with id: " + id));

        model.addAttribute("pageTitle", "Ticket Purchase Details");
        model.addAttribute("purchase", purchase);
        model.addAttribute("currentUser", currentUser);
        return "admin/ticket-purchase-details";
    }

    @GetMapping("/ticket-purchases/new")
    public String showCreateTicketPurchaseForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");
        List<Customer> customers = customerService.getAllCustomers();
        List<Ticket> availableTickets = ticketService.findTicketsByStatus(TicketStatus.AVAILABLE);

        model.addAttribute("pageTitle", "Create New Ticket Purchase");
        model.addAttribute("purchase", new TicketPurchase());
        model.addAttribute("customers", customers);
        model.addAttribute("availableTickets", availableTickets);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        model.addAttribute("currentUser", currentUser);
        return "admin/ticket-purchase-form";
    }

    @PostMapping("/ticket-purchases")
    public String createTicketPurchase(@ModelAttribute TicketPurchase purchase,
                                     @RequestParam Long customerId,
                                     @RequestParam List<Long> ticketIds,
                                     HttpSession session, Model model) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        try {
            // Set purchase details
            purchase.setPurchaseDateTime(LocalDateTime.now());
            
            // Set customer
            Customer customer = customerService.getCustomerById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
            purchase.setCustomer(customer);
            
            // Calculate total amount and update tickets
            double totalAmount = 0;
            for (Long ticketId : ticketIds) {
                Ticket ticket = ticketService.getTicketById(ticketId)
                        .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
                ticket.setStatus(TicketStatus.SOLD);
                ticket.setPurchase(purchase);
                totalAmount += ticket.getPrice();
                ticketService.saveTicket(ticket);
            }
            purchase.setTotalAmount(totalAmount);
            
            ticketPurchaseService.saveTicketPurchase(purchase);
            return "redirect:/admin/ticket-purchases?success=created";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating purchase: " + e.getMessage());
            return showCreateTicketPurchaseForm(model, session);
        }
    }

    @GetMapping("/ticket-purchases/delete/{id}")
    public String deleteTicketPurchase(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        try {
            ticketPurchaseService.deleteTicketPurchase(id);
            return "redirect:/admin/ticket-purchases?success=deleted";
        } catch (Exception e) {
            return "redirect:/admin/ticket-purchases?error=delete_failed";
        }
    }
}