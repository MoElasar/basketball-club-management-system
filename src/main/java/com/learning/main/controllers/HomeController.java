package com.learning.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("pageTitle", "Home - Basketball Club");
		return "index";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("pageTitle", "About Us - Basketball Club");
		return "about";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("pageTitle", "Contact Us - Basketball Club");
		return "contact";
	}

	@PostMapping("/contact")
	public String handleContactForm(@RequestParam String name, @RequestParam String email, @RequestParam String message,
			Model model) {
		// Here you would typically process the form (send email, save to DB, etc.)
		model.addAttribute("success", true);
		model.addAttribute("pageTitle", "Contact Us - Basketball Club");
		return "contact";
	}
}