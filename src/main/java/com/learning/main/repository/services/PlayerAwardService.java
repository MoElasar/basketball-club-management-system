package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class PlayerAwardService {
	@Autowired
	private PlayerAwardRepository playerAwardRepository;

	public PlayerAward savePlayerAward(PlayerAward playerAward) {
		return playerAwardRepository.save(playerAward);
	}

	public Optional<PlayerAward> getPlayerAwardById(Long id) {
		return playerAwardRepository.findById(id);
	}

	public List<PlayerAward> getAllPlayerAwards() {
		return playerAwardRepository.findAll();
	}

	public void deletePlayerAward(Long id) {
		playerAwardRepository.deleteById(id);
	}

	public List<PlayerAward> findPlayerAwardsByPlayer(Player player) {
		return playerAwardRepository.findByPlayer(player);
	}

	public List<PlayerAward> findPlayerAwardsByAwardDateBetween(LocalDate start, LocalDate end) {
		return playerAwardRepository.findByAwardDateBetween(start, end);
	}
}
