package com.learning.main.controllers;

import com.learning.main.model.Game;
import com.learning.main.model.Player;
import com.learning.main.model.PlayerStatistics;
import com.learning.main.repository.services.GameService;
import com.learning.main.repository.services.PlayerService;
import com.learning.main.repository.services.PlayerStatisticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/players/statistics")
public class PlayerStatisticsController {

    @Autowired
    private PlayerStatisticsService playerStatisticsService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping("/{playerId}")
    public String viewPlayerStatistics(@PathVariable Long playerId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<Player> player = playerService.getPlayerById(playerId);
        if (player.isEmpty()) {
            return "redirect:/admin/players?error=playerNotFound";
        }

        List<PlayerStatistics> statistics = playerStatisticsService.findPlayerStatisticsByPlayer(player.get());
        PlayerStatistics seasonStats = playerStatisticsService.getSeasonStats(playerId);

        model.addAttribute("pageTitle", "Player Statistics - " + player.get().getFirstName() + " " + player.get().getLastName());
        model.addAttribute("statistics", statistics);
        model.addAttribute("seasonStats", seasonStats);
        model.addAttribute("player", player.get());
        model.addAttribute("newStat", new PlayerStatistics());
        model.addAttribute("allGames", gameService.getAllGames()); // ✅ Added to populate game dropdown
        return "admin/player-statistics";
    }

    @PostMapping("/add/{playerId}")
    public String addPlayerStatistic(@PathVariable Long playerId,
                                     @ModelAttribute("newStat") PlayerStatistics stat,
                                     @RequestParam(value = "game.gameId", required = false) Long gameId,
                                     Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<Player> player = playerService.getPlayerById(playerId);
        if (player.isEmpty()) {
            return "redirect:/admin/players?error=playerNotFound";
        }

        stat.setPlayer(player.get());

        // ✅ Ensure Game is managed
        if (gameId != null) {
            Optional<Game> game = gameService.getGameById(gameId);
            if (game.isPresent()) {
                stat.setGame(game.get());
            } else {
                return "redirect:/admin/players/statistics/" + playerId + "?error=gameNotFound";
            }
        }

        playerStatisticsService.savePlayerStatistics(stat);
        return "redirect:/admin/players/statistics/" + playerId + "?success=add";
    }

    @GetMapping("/edit/{statId}")
    public String showEditStatisticForm(@PathVariable Long statId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<PlayerStatistics> stat = playerStatisticsService.getPlayerStatisticsById(statId);
        if (stat.isEmpty()) {
            return "redirect:/admin/players?error=statisticNotFound";
        }

        model.addAttribute("pageTitle", "Edit Statistic");
        model.addAttribute("stat", stat.get());
        model.addAttribute("allGames", gameService.getAllGames()); // Optional for editing
        return "admin/edit-player-statistic";
    }

    @PostMapping("/update/{statId}")
    public String updatePlayerStatistic(@PathVariable Long statId,
                                        @ModelAttribute("stat") PlayerStatistics statDetails,
                                        @RequestParam(value = "game.gameId", required = false) Long gameId,
                                        Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        PlayerStatistics existingStat = playerStatisticsService.getPlayerStatisticsById(statId)
                .orElseThrow(() -> new RuntimeException("Statistic not found"));

        // ✅ Optionally update game
        if (gameId != null) {
            Optional<Game> game = gameService.getGameById(gameId);
            game.ifPresent(existingStat::setGame);
        }

        // Update fields
        existingStat.setPoints(statDetails.getPoints());
        existingStat.setRebounds(statDetails.getRebounds());
        existingStat.setAssists(statDetails.getAssists());
        existingStat.setSteals(statDetails.getSteals());
        existingStat.setBlocks(statDetails.getBlocks());
        existingStat.setTurnovers(statDetails.getTurnovers());
        existingStat.setFouls(statDetails.getFouls());
        existingStat.setMinutesPlayed(statDetails.getMinutesPlayed());
        existingStat.setFieldGoalsAttempted(statDetails.getFieldGoalsAttempted());
        existingStat.setFieldGoalsMade(statDetails.getFieldGoalsMade());
        existingStat.setThreePointsAttempted(statDetails.getThreePointsAttempted());
        existingStat.setThreePointsMade(statDetails.getThreePointsMade());
        existingStat.setFreeThrowsAttempted(statDetails.getFreeThrowsAttempted());
        existingStat.setFreeThrowsMade(statDetails.getFreeThrowsMade());

        playerStatisticsService.savePlayerStatistics(existingStat);
        return "redirect:/admin/players/statistics/" + existingStat.getPlayer().getPlayerId() + "?success=update";
    }

    @GetMapping("/delete/{statId}")
    public String deletePlayerStatistic(@PathVariable Long statId, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }

        Optional<PlayerStatistics> stat = playerStatisticsService.getPlayerStatisticsById(statId);
        if (stat.isEmpty()) {
            return "redirect:/admin/players?error=statisticNotFound";
        }

        Long playerId = stat.get().getPlayer().getPlayerId();
        playerStatisticsService.deletePlayerStatistics(statId);
        return "redirect:/admin/players/statistics/" + playerId + "?success=delete";
    }
}
