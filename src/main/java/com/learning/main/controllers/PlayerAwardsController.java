package com.learning.main.controllers;

import com.learning.main.model.Player;
import com.learning.main.model.PlayerAward;
import com.learning.main.repository.services.PlayerAwardService;
import com.learning.main.repository.services.PlayerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/players/awards")
public class PlayerAwardsController {

    @Autowired
    private PlayerAwardService playerAwardService;
    
    @Autowired
    private PlayerService playerService;

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping("/{playerId}")
    public String viewPlayerAwards(@PathVariable Long playerId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        Optional<Player> player = playerService.getPlayerById(playerId);
        if (player.isEmpty()) {
            return "redirect:/admin/players?error=playerNotFound";
        }
        
        List<PlayerAward> awards = playerAwardService.findPlayerAwardsByPlayer(player.get());
        
        model.addAttribute("pageTitle", "Player Awards - " + player.get().getFirstName() + " " + player.get().getLastName());
        model.addAttribute("awards", awards);
        model.addAttribute("player", player.get());
        model.addAttribute("newAward", new PlayerAward());
        return "admin/player-awards";
    }

    @PostMapping("/add/{playerId}")
    public String addPlayerAward(@PathVariable Long playerId, 
                               @ModelAttribute("newAward") PlayerAward award,
                               Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        Optional<Player> player = playerService.getPlayerById(playerId);
        if (player.isEmpty()) {
            return "redirect:/admin/players?error=playerNotFound";
        }
        
        award.setPlayer(player.get());
        if (award.getAwardDate() == null) {
            award.setAwardDate(LocalDate.now());
        }
        
        playerAwardService.savePlayerAward(award);
        return "redirect:/admin/players/awards/" + playerId + "?success=add";
    }

    @GetMapping("/edit/{awardId}")
    public String showEditAwardForm(@PathVariable Long awardId, 
                                  Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        Optional<PlayerAward> award = playerAwardService.getPlayerAwardById(awardId);
        if (award.isEmpty()) {
            return "redirect:/admin/players?error=awardNotFound";
        }
        
        model.addAttribute("pageTitle", "Edit Award");
        model.addAttribute("award", award.get());
        return "admin/edit-player-award";
    }

    @PostMapping("/update/{awardId}")
    public String updatePlayerAward(@PathVariable Long awardId,
                                  @ModelAttribute("award") PlayerAward awardDetails,
                                  Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        PlayerAward existingAward = playerAwardService.getPlayerAwardById(awardId)
                .orElseThrow(() -> new RuntimeException("Award not found"));
        
        existingAward.setAwardName(awardDetails.getAwardName());
        existingAward.setAwardDate(awardDetails.getAwardDate());
        existingAward.setDescription(awardDetails.getDescription());
        existingAward.setTeamAward(awardDetails.isTeamAward());
        
        playerAwardService.savePlayerAward(existingAward);
        return "redirect:/admin/players/awards/" + existingAward.getPlayer().getPlayerId() + "?success=update";
    }

    @GetMapping("/delete/{awardId}")
    public String deletePlayerAward(@PathVariable Long awardId, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        Optional<PlayerAward> award = playerAwardService.getPlayerAwardById(awardId);
        if (award.isEmpty()) {
            return "redirect:/admin/players?error=awardNotFound";
        }
        
        Long playerId = award.get().getPlayer().getPlayerId();
        playerAwardService.deletePlayerAward(awardId);
        return "redirect:/admin/players/awards/" + playerId + "?success=delete";
    }
}