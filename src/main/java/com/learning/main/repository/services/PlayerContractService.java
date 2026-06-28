package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class PlayerContractService {
	@Autowired
	private PlayerContractRepository playerContractRepository;

	public PlayerContract savePlayerContract(PlayerContract playerContract) {
		return playerContractRepository.save(playerContract);
	}

	public Optional<PlayerContract> getPlayerContractById(Long id) {
		return playerContractRepository.findById(id);
	}

	public List<PlayerContract> getAllPlayerContracts() {
		return playerContractRepository.findAll();
	}

	public void deletePlayerContract(Long id) {
		playerContractRepository.deleteById(id);
	}

	public List<PlayerContract> findPlayerContractsByPlayer(Player player) {
		return playerContractRepository.findByPlayer(player);
	}

	public List<PlayerContract> findPlayerContractsByStartDateAfter(LocalDate date) {
		return playerContractRepository.findByStartDateAfter(date);
	}

	public List<PlayerContract> findPlayerContractsByEndDateBefore(LocalDate date) {
		return playerContractRepository.findByEndDateBefore(date);
	}
}