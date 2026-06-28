package com.learning.main.controllers;

import com.learning.main.enm.ContractType;
import com.learning.main.model.Player;
import com.learning.main.model.PlayerContract;
import com.learning.main.repository.services.PlayerContractService;
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
@RequestMapping("/admin/players/contracts")
public class PlayerContractsController {

	@Autowired
	private PlayerContractService playerContractService;

	@Autowired
	private PlayerService playerService;

	private boolean isAuthenticated(HttpSession session) {
		return session.getAttribute("user") != null;
	}

	@GetMapping("/{playerId}")
	public String viewPlayerContracts(@PathVariable Long playerId, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Optional<Player> player = playerService.getPlayerById(playerId);
		if (player.isEmpty()) {
			return "redirect:/admin/players?error=playerNotFound";
		}

		List<PlayerContract> contracts = playerContractService.findPlayerContractsByPlayer(player.get());

		model.addAttribute("pageTitle",
				"Player Contracts - " + player.get().getFirstName() + " " + player.get().getLastName());
		model.addAttribute("contracts", contracts);
		model.addAttribute("player", player.get());
		model.addAttribute("newContract", new PlayerContract());
		model.addAttribute("contractTypes", ContractType.values());
		return "admin/player-contracts";
	}

	@PostMapping("/add/{playerId}")
	public String addPlayerContract(@PathVariable Long playerId, @ModelAttribute("newContract") PlayerContract contract,
			Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Optional<Player> player = playerService.getPlayerById(playerId);
		if (player.isEmpty()) {
			return "redirect:/admin/players?error=playerNotFound";
		}

		contract.setPlayer(player.get());
		playerContractService.savePlayerContract(contract);
		return "redirect:/admin/players/contracts/" + playerId + "?success=add";
	}

	@GetMapping("/edit/{contractId}")
	public String showEditContractForm(@PathVariable Long contractId, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Optional<PlayerContract> contract = playerContractService.getPlayerContractById(contractId);
		if (contract.isEmpty()) {
			return "redirect:/admin/players?error=contractNotFound";
		}

		model.addAttribute("pageTitle", "Edit Contract");
		model.addAttribute("contract", contract.get());
		model.addAttribute("contractTypes", ContractType.values());
		return "admin/edit-player-contract";
	}

	@PostMapping("/update/{contractId}")
	public String updatePlayerContract(@PathVariable Long contractId,
			@ModelAttribute("contract") PlayerContract contractDetails, Model model, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		PlayerContract existingContract = playerContractService.getPlayerContractById(contractId)
				.orElseThrow(() -> new RuntimeException("Contract not found"));

		existingContract.setStartDate(contractDetails.getStartDate());
		existingContract.setEndDate(contractDetails.getEndDate());
		existingContract.setSalary(contractDetails.getSalary());
		existingContract.setBonus(contractDetails.getBonus());
		existingContract.setContractType(contractDetails.getContractType());
		existingContract.setClauses(contractDetails.getClauses());

		playerContractService.savePlayerContract(existingContract);
		return "redirect:/admin/players/contracts/" + existingContract.getPlayer().getPlayerId() + "?success=update";
	}

	@GetMapping("/delete/{contractId}")
	public String deletePlayerContract(@PathVariable Long contractId, HttpSession session) {
		if (!isAuthenticated(session)) {
			return "redirect:/login";
		}

		Optional<PlayerContract> contract = playerContractService.getPlayerContractById(contractId);
		if (contract.isEmpty()) {
			return "redirect:/admin/players?error=contractNotFound";
		}

		Long playerId = contract.get().getPlayer().getPlayerId();
		playerContractService.deletePlayerContract(contractId);
		return "redirect:/admin/players/contracts/" + playerId + "?success=delete";
	}
}