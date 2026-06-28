package com.learning.main.controllers;

import com.learning.main.enm.ERole;
import com.learning.main.enm.NewsCategory;
import com.learning.main.model.News;
import com.learning.main.model.User; // Assuming User model for authentication
import com.learning.main.repository.services.NewsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.Optional;

@Controller
@RequestMapping("/admin/news")
public class NewsController {

	@Autowired
	private NewsService newsService;

	// Helper method for authentication (assuming 'user' attribute in HttpSession)
	// Corrected isAuthenticated method
	private boolean isAuthenticated(HttpSession session) {
		Object userAttribute = session.getAttribute("user");
		if (userAttribute instanceof User) {
			User user = (User) userAttribute;
			// Check if the user has any role that matches ERole.ROLE_ADMIN
			return user.getRoles() != null
					&& user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);
		}
		return false; // Not authenticated or user object is not of type User
	}

	@GetMapping
	public String listNews(Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login"; // Redirect to login if not authenticated as admin
		}
		model.addAttribute("pageTitle", "Manage News");
		model.addAttribute("newsArticles", newsService.getAllNews());
		return "admin/news-list";
	}

	@GetMapping("/new")
	public String showAddNewsForm(Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		model.addAttribute("pageTitle", "Add New News Article");
		model.addAttribute("news", new News());
		model.addAttribute("categories", NewsCategory.values()); // Pass enum values to template
		return "admin/news-form";
	}

	@PostMapping("/save")
	public String saveNews(@ModelAttribute("news") News news, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		news.setPublishDate(LocalDate.now()); // Set publish date to current date
		newsService.saveNews(news);
		return "redirect:/admin/news?success=save";
	}

	@GetMapping("/edit/{newsId}")
	public String showEditNewsForm(@PathVariable Long newsId, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		Optional<News> news = newsService.getNewsById(newsId);
		if (news.isEmpty()) {
			return "redirect:/admin/news?error=notFound";
		}
		model.addAttribute("pageTitle", "Edit News Article");
		model.addAttribute("news", news.get());
		model.addAttribute("categories", NewsCategory.values()); // Pass enum values to template
		return "admin/news-form";
	}

	@GetMapping("/delete/{newsId}")
	public String deleteNews(@PathVariable Long newsId, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}
		newsService.deleteNews(newsId);
		return "redirect:/admin/news?success=delete";
	}
}