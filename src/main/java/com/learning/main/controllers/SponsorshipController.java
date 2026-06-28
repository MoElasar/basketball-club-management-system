package com.learning.main.controllers;


import com.learning.main.model.Sponsor;
import com.learning.main.model.Sponsorship;
import com.learning.main.model.Team;
import com.learning.main.repository.services.SponsorService;
import com.learning.main.repository.services.SponsorshipService;
import com.learning.main.repository.services.TeamService; // Assuming you have a TeamService
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/sponsorships")
public class SponsorshipController {

    @Autowired
    private SponsorshipService sponsorshipService;
    @Autowired
    private TeamService teamService; // To populate team dropdown
    @Autowired
    private SponsorService sponsorService; // To populate sponsor dropdown

    // Helper method for authentication
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    // List all Sponsorships
    @GetMapping
    public String listSponsorships(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        List<Sponsorship> sponsorships = sponsorshipService.getAllSponsorships();
        List<Team> teams = teamService.getAllTeams(); // For dropdowns
        List<Sponsor> sponsors = sponsorService.getAllSponsors(); // For dropdowns

        model.addAttribute("pageTitle", "Sponsorship Management");
        model.addAttribute("sponsorships", sponsorships);
        model.addAttribute("newSponsorship", new Sponsorship()); // For the add modal form
        model.addAttribute("teams", teams); // Pass teams for selection
        model.addAttribute("sponsors", sponsors); // Pass sponsors for selection
        model.addAttribute("currentUser", session.getAttribute("user"));
        return "admin/sponsorships";
    }

    // Add a new Sponsorship
    @PostMapping("/add")
    public String addSponsorship(@ModelAttribute("newSponsorship") Sponsorship sponsorship,
                                 @RequestParam("teamId") Long teamId,
                                 @RequestParam("sponsorId") Long sponsorId,
                                 Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<Team> team = teamService.getTeamById(teamId);
        Optional<Sponsor> sponsor = sponsorService.getSponsorById(sponsorId);

        if (team.isEmpty() || sponsor.isEmpty()) {
            return "redirect:/admin/sponsorships?error=invalidIds";
        }

        sponsorship.setTeam(team.get());
        sponsorship.setSponsor(sponsor.get());
        sponsorshipService.saveSponsorship(sponsorship);

        return "redirect:/admin/sponsorships?success=add";
    }

    // Show form to edit an existing Sponsorship
    @GetMapping("/edit/{id}")
    public String showEditSponsorshipForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<Sponsorship> sponsorship = sponsorshipService.getSponsorshipById(id);
        if (sponsorship.isEmpty()) {
            return "redirect:/admin/sponsorships?error=notFound";
        }

        List<Team> teams = teamService.getAllTeams();
        List<Sponsor> sponsors = sponsorService.getAllSponsors();

        model.addAttribute("pageTitle", "Edit Sponsorship");
        model.addAttribute("sponsorship", sponsorship.get());
        model.addAttribute("teams", teams);
        model.addAttribute("sponsors", sponsors);
        model.addAttribute("currentUser", session.getAttribute("user"));
        return "admin/edit-sponsorship";
    }

    // Update an existing Sponsorship
    @PostMapping("/update/{id}")
    public String updateSponsorship(@PathVariable Long id,
                                   @ModelAttribute("sponsorship") Sponsorship sponsorshipDetails,
                                   @RequestParam("teamId") Long teamId,
                                   @RequestParam("sponsorId") Long sponsorId,
                                   Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Sponsorship existingSponsorship = sponsorshipService.getSponsorshipById(id)
                .orElseThrow(() -> new RuntimeException("Sponsorship not found for this id :: " + id));

        Optional<Team> team = teamService.getTeamById(teamId);
        Optional<Sponsor> sponsor = sponsorService.getSponsorById(sponsorId);

        if (team.isEmpty() || sponsor.isEmpty()) {
            return "redirect:/admin/sponsorships/edit/" + id + "?error=invalidIds";
        }

        existingSponsorship.setTeam(team.get());
        existingSponsorship.setSponsor(sponsor.get());
        existingSponsorship.setStartDate(sponsorshipDetails.getStartDate());
        existingSponsorship.setEndDate(sponsorshipDetails.getEndDate());
        existingSponsorship.setAmount(sponsorshipDetails.getAmount());
        existingSponsorship.setDetails(sponsorshipDetails.getDetails());
        existingSponsorship.setActive(sponsorshipDetails.isActive()); // Assuming isActive field

        sponsorshipService.saveSponsorship(existingSponsorship);
        return "redirect:/admin/sponsorships?success=update";
    }

    // Delete a Sponsorship
    @GetMapping("/delete/{id}")
    public String deleteSponsorship(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<Sponsorship> sponsorship = sponsorshipService.getSponsorshipById(id);
        if (sponsorship.isEmpty()) {
            return "redirect:/admin/sponsorships?error=notFound";
        }

        sponsorshipService.deleteSponsorship(id);
        return "redirect:/admin/sponsorships?success=delete";
    }
}