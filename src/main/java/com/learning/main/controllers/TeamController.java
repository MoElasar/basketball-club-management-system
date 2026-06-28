package com.learning.main.controllers;

import com.learning.main.model.Team;
import com.learning.main.model.User;
import com.learning.main.repository.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/team") // All team management routes will start with /admin/teams
public class TeamController {

    @Autowired
    private TeamService teamService;

    // Helper to check if user is logged in (similar to AdminController)
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping
    public String manageTeams(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        User currentUser = (User) session.getAttribute("user");
        List<Team> teams = teamService.getAllTeams();
        model.addAttribute("pageTitle", "Team Management");
        model.addAttribute("teams", teams);
        model.addAttribute("newTeam", new Team()); // For the add new team form
        model.addAttribute("currentUser", currentUser);
        return "admin/team"; // This will be the main teams listing page
    }

    @PostMapping("/add")
    public String addTeam(@ModelAttribute("newTeam") Team team, HttpSession session, Model model) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        // Basic validation for name uniqueness using the new teamName field
        if (teamService.existsTeamByTeamName(team.getTeamName())) {
            // Handle error: team with this name already exists
            model.addAttribute("error", "Team with this name already exists.");
            List<Team> teams = teamService.getAllTeams(); // Re-fetch teams to display them again
            model.addAttribute("teams", teams);
            model.addAttribute("pageTitle", "Team Management");
            model.addAttribute("currentUser", (User) session.getAttribute("user"));
            return "admin/team"; // Return to the form with an error message
        }
        teamService.saveTeam(team);
        return "redirect:/admin/team?success=add";
    }

    @GetMapping("/edit/{id}")
    public String showEditTeamForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        User currentUser = (User) session.getAttribute("user");
        Optional<Team> team = teamService.getTeamById(id);
        if (team.isEmpty()) {
            // Handle error: team not found
            return "redirect:/admin/team?error=notFound";
        }
        model.addAttribute("pageTitle", "Edit Team");
        model.addAttribute("team", team.get());
        model.addAttribute("currentUser", currentUser);
        return "admin/edit-team"; // Form for editing an existing team
    }

    @PostMapping("/update/{id}")
    public String updateTeam(@PathVariable Long id, @ModelAttribute("team") Team teamDetails, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        Team team = teamService.getTeamById(id)
                .orElseThrow(() -> new RuntimeException("Team not found for this id :: " + id));

        // Update details using the new field names
        team.setTeamName(teamDetails.getTeamName());
        team.setCity(teamDetails.getCity());
        team.setHomeArena(teamDetails.getHomeArena());
        team.setFoundingDate(teamDetails.getFoundingDate());
        team.setLogoImage(teamDetails.getLogoImage());

        teamService.saveTeam(team);
        return "redirect:/admin/team?success=update";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        teamService.deleteTeam(id);
        return "redirect:/admin/team?success=delete";
    }
}