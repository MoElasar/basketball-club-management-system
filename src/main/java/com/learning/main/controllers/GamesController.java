// src/main/java/com/learning/main/controllers/GamesController.java
package com.learning.main.controllers;

import com.learning.main.enm.GameStatus;
import com.learning.main.model.Game;
import com.learning.main.model.Team;
import com.learning.main.model.User;
import com.learning.main.repository.services.GameService;
import com.learning.main.repository.services.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/games")
public class GamesController {

    @Autowired
    private GameService gameService;
    @Autowired
    private TeamService teamService;

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping
    public String manageGames(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        User currentUser = (User) session.getAttribute("user");
        List<Game> games = gameService.getAllGames();
        List<Team> teams = teamService.getAllTeams();

        model.addAttribute("pageTitle", "Game Management");
        model.addAttribute("games", games);
        model.addAttribute("newGame", new Game());
        model.addAttribute("teams", teams);
        model.addAttribute("gameStatuses", GameStatus.values());
        model.addAttribute("currentUser", currentUser);
        return "admin/games";
    }

    @PostMapping("/add")
    public String addGame(@ModelAttribute("newGame") Game game,
                          @RequestParam("homeTeamId") Long homeTeamId,
                          @RequestParam("awayTeamId") Long awayTeamId,
                          HttpSession session, Model model) {

        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<Team> homeTeamOptional = teamService.getTeamById(homeTeamId);
        Optional<Team> awayTeamOptional = teamService.getTeamById(awayTeamId);

        if (homeTeamOptional.isEmpty() || awayTeamOptional.isEmpty()) {
            model.addAttribute("error", "Home or Away team not found.");
            return showGameFormWithError(model, session, game);
        }

        Team homeTeam = homeTeamOptional.get();
        Team awayTeam = awayTeamOptional.get();

        if (homeTeam.equals(awayTeam)) {
            model.addAttribute("error", "Home and Away teams cannot be the same.");
            return showGameFormWithError(model, session, game);
        }

        game.setHomeTeam(homeTeam);
        game.setAwayTeam(awayTeam);
        
        // Default status if not provided by form
        if (game.getStatus() == null) {
            game.setStatus(GameStatus.SCHEDULED);
        }

        // Validate if a game with the same teams and datetime already exists
        if (gameService.existsByHomeTeamAndAwayTeamAndGameDateTime(homeTeam, awayTeam, game.getGameDateTime()) ||
            gameService.existsByHomeTeamAndAwayTeamAndGameDateTime(awayTeam, homeTeam, game.getGameDateTime())) {
            model.addAttribute("error", "A game between these teams at this date/time already exists.");
            return showGameFormWithError(model, session, game);
        }

        gameService.saveGame(game);
        return "redirect:/admin/games?success=add";
    }

    @GetMapping("/edit/{id}")
    public String showEditGameForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        User currentUser = (User) session.getAttribute("user");
        Optional<Game> game = gameService.getGameById(id);
        if (game.isEmpty()) {
            return "redirect:/admin/games?error=notFound";
        }
        model.addAttribute("pageTitle", "Edit Game");
        model.addAttribute("game", game.get());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("gameStatuses", GameStatus.values());
        model.addAttribute("currentUser", currentUser);
        return "admin/edit-game";
    }

    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable Long id, @ModelAttribute("game") Game gameDetails,
                             @RequestParam("homeTeamId") Long homeTeamId,
                             @RequestParam("awayTeamId") Long awayTeamId,
                             HttpSession session, Model model) {

        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Game existingGame = gameService.getGameById(id)
                .orElseThrow(() -> new RuntimeException("Game not found for this id :: " + id));

        Optional<Team> homeTeamOptional = teamService.getTeamById(homeTeamId);
        Optional<Team> awayTeamOptional = teamService.getTeamById(awayTeamId);

        if (homeTeamOptional.isEmpty() || awayTeamOptional.isEmpty()) {
            model.addAttribute("error", "Home or Away team not found.");
            return showEditFormWithError(model, session, existingGame);
        }

        Team homeTeam = homeTeamOptional.get();
        Team awayTeam = awayTeamOptional.get();

        if (homeTeam.equals(awayTeam)) {
            model.addAttribute("error", "Home and Away teams cannot be the same.");
            return showEditFormWithError(model, session, existingGame);
        }

        // Validate if a game with the same teams and datetime already exists (excluding the current game)
        if ((gameService.existsByHomeTeamAndAwayTeamAndGameDateTime(homeTeam, awayTeam, gameDetails.getGameDateTime()) &&
             !(existingGame.getHomeTeam().equals(homeTeam) && existingGame.getAwayTeam().equals(awayTeam) && existingGame.getGameDateTime().equals(gameDetails.getGameDateTime()))) ||
            (gameService.existsByHomeTeamAndAwayTeamAndGameDateTime(awayTeam, homeTeam, gameDetails.getGameDateTime()) &&
             !(existingGame.getHomeTeam().equals(awayTeam) && existingGame.getAwayTeam().equals(homeTeam) && existingGame.getGameDateTime().equals(gameDetails.getGameDateTime())))) {
            model.addAttribute("error", "A game between these teams at this date/time already exists.");
            return showEditFormWithError(model, session, existingGame);
        }


        existingGame.setHomeTeam(homeTeam);
        existingGame.setAwayTeam(awayTeam);
        existingGame.setGameDateTime(gameDetails.getGameDateTime());
        existingGame.setVenue(gameDetails.getVenue());
        existingGame.setHomeScore(gameDetails.getHomeScore());
        existingGame.setAwayScore(gameDetails.getAwayScore());
        existingGame.setStatus(gameDetails.getStatus());

        gameService.saveGame(existingGame);
        return "redirect:/admin/games?success=update";
    }

    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        gameService.deleteGame(id);
        return "redirect:/admin/games?success=delete";
    }

    private String showGameFormWithError(Model model, HttpSession session, Game game) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("gameStatuses", GameStatus.values());
        model.addAttribute("newGame", game);
        model.addAttribute("pageTitle", "Game Management");
        model.addAttribute("currentUser", currentUser);
        return "admin/games";
    }

    private String showEditFormWithError(Model model, HttpSession session, Game game) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("game", game);
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("gameStatuses", GameStatus.values());
        model.addAttribute("pageTitle", "Edit Game");
        model.addAttribute("currentUser", currentUser);
        return "admin/edit-game";
    }
}