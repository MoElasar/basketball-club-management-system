package com.learning.main.controllers;

import com.learning.main.model.Sponsor;
import com.learning.main.repository.services.SponsorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/sponsors")
public class SponsorController {

	@Autowired
	private SponsorService sponsorService;

	// Helper method for authentication (assuming 'user' attribute in session)
	private boolean isAuthenticated(HttpSession session) {
		return session.getAttribute("user") != null;
	}

	// List all Sponsors
	@GetMapping
	public String listSponsors(Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login"; // Redirect to login if not authenticated
		}

		List<Sponsor> sponsors = sponsorService.getAllSponsors();
		model.addAttribute("pageTitle", "Sponsor Management");
		model.addAttribute("sponsors", sponsors);
		model.addAttribute("newSponsor", new Sponsor()); // For the add modal form
		model.addAttribute("currentUser", session.getAttribute("user")); // Pass user for navbar
		return "admin/sponsors";
	}

	// Add a new Sponsor
	@PostMapping("/add")
	public String addSponsor(@ModelAttribute("newSponsor") Sponsor sponsor, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		// Basic validation: Check if sponsor with same name already exists (optional
		// but good practice)
		// You might need a findBySponsorName method in your SponsorService/Repository
		// Example: if
		// (sponsorService.findSponsorByName(sponsor.getSponsorName()).isPresent()) {
		// model.addAttribute("error", "Sponsor with this name already exists.");
		// List<Sponsor> sponsors = sponsorService.getAllSponsors();
		// model.addAttribute("sponsors", sponsors);
		// return "admin/sponsors"; // Return to the page with error
		// }

		sponsorService.saveSponsor(sponsor);
		return "redirect:/admin/sponsors?success=add";
	}

	// Show form to edit an existing Sponsor
	@GetMapping("/edit/{id}")
	public String showEditSponsorForm(@PathVariable Long id, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Optional<Sponsor> sponsor = sponsorService.getSponsorById(id);
		if (sponsor.isEmpty()) {
			return "redirect:/admin/sponsors?error=notFound";
		}

		model.addAttribute("pageTitle", "Edit Sponsor");
		model.addAttribute("sponsor", sponsor.get());
		model.addAttribute("currentUser", session.getAttribute("user"));
		return "admin/edit-sponsor";
	}

	// Update an existing Sponsor
	@PostMapping("/update/{id}")
	public String updateSponsor(@PathVariable Long id, @ModelAttribute("sponsor") Sponsor sponsorDetails, Model model,
			HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Sponsor existingSponsor = sponsorService.getSponsorById(id)
				.orElseThrow(() -> new RuntimeException("Sponsor not found for this id :: " + id));

		existingSponsor.setSponsorName(sponsorDetails.getSponsorName());
		existingSponsor.setContactPerson(sponsorDetails.getContactPerson());
		existingSponsor.setContactEmail(sponsorDetails.getContactEmail());
		existingSponsor.setPhone(sponsorDetails.getPhone());
		existingSponsor.setIndustry(sponsorDetails.getIndustry());
		existingSponsor.setWebsite(sponsorDetails.getWebsite());
		existingSponsor.setLogoUrl(sponsorDetails.getLogoUrl());

		sponsorService.saveSponsor(existingSponsor);
		return "redirect:/admin/sponsors?success=update";
	}

	// Delete a Sponsor
	@GetMapping("/delete/{id}")
	public String deleteSponsor(@PathVariable Long id, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Optional<Sponsor> sponsor = sponsorService.getSponsorById(id);
		if (sponsor.isEmpty()) {
			return "redirect:/admin/sponsors?error=notFound";
		}

		sponsorService.deleteSponsor(id);
		return "redirect:/admin/sponsors?success=delete";
	}
}