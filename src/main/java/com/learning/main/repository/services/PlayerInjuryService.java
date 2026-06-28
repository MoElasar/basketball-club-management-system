package com.learning.main.repository.services;

import com.learning.main.enm.InjuryStatus;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class PlayerInjuryService {
	@Autowired
	private PlayerInjuryRepository playerInjuryRepository;

	 

	public Optional<PlayerInjury> getPlayerInjuryById(Long id) {
		return playerInjuryRepository.findById(id);
	}

	public List<PlayerInjury> getAllPlayerInjuries() {
		return playerInjuryRepository.findAll();
	}

	public void deletePlayerInjury(Long id) {
		playerInjuryRepository.deleteById(id);
	}

	public List<PlayerInjury> findPlayerInjuriesByPlayer(Player player) {
		return playerInjuryRepository.findByPlayer(player);
	}

	public List<PlayerInjury> findPlayerInjuriesByStatus(InjuryStatus status) {
		return playerInjuryRepository.findByStatus(status);
	}

	public List<PlayerInjury> findPlayerInjuriesByInjuryDateBetween(LocalDate start, LocalDate end) {
		return playerInjuryRepository.findByInjuryDateBetween(start, end);
	}

	public List<PlayerInjury> findPlayerInjuriesByPlayer_Team(Team team) {
		return playerInjuryRepository.findByPlayer_Team(team);
	}
	
	public PlayerInjury savePlayerInjury(PlayerInjury injury) {
	    // Make sure the player exists
	    if (injury.getPlayer() == null || injury.getPlayer().getPlayerId() == null) {
	        throw new IllegalArgumentException("Player must be specified");
	    }
	    return playerInjuryRepository.save(injury);
	}

}