package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Game;
import com.learning.main.model.Player;
import com.learning.main.model.PlayerStatistics;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PlayerStatisticsRepository extends JpaRepository<PlayerStatistics, Long> {
	List<PlayerStatistics> findByPlayer(Player player);

	List<PlayerStatistics> findByGame(Game game);

	List<PlayerStatistics> findByPlayerAndGame(Player player, Game game);

	List<PlayerStatistics> findByPlayer_PlayerId(Long playerId);

	List<PlayerStatistics> findTop5ByPlayer_PlayerIdOrderByGame_GameDateTimeDesc(Long playerId);

	List<PlayerStatistics> findByPlayer_PlayerIdOrderByGame_GameDateTimeDesc(Long playerId, Pageable pageable);

}