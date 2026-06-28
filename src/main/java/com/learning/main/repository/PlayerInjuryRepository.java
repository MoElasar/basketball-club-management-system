package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.enm.InjuryStatus;
import com.learning.main.model.Player;
import com.learning.main.model.PlayerInjury;
import com.learning.main.model.Team;

import java.time.LocalDate;

import java.util.List;

public interface PlayerInjuryRepository extends JpaRepository<PlayerInjury, Long> {
	List<PlayerInjury> findByPlayer(Player player);

	List<PlayerInjury> findByStatus(InjuryStatus status);

	List<PlayerInjury> findByInjuryDateBetween(LocalDate start, LocalDate end);

	List<PlayerInjury> findByPlayer_Team(Team team);

}
