package com.learning.main.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.learning.main.enm.Position;
import com.learning.main.model.User;
import com.learning.main.model.Player;
import com.learning.main.model.Team;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	List<Player> findByTeam(Team team);

	Optional<Player> findByUser(User user);

	List<Player> findByPosition(Position position);

	List<Player> findByJerseyNumber(int jerseyNumber);

	// For checking if a jersey number exists for a specific team
	boolean existsByJerseyNumberAndTeam(int jerseyNumber, Team team);

	// For checking if a jersey number exists for a specific team ID
	boolean existsByJerseyNumberAndTeam_TeamId(int jerseyNumber, Long teamId);

	// For finding a specific player by jersey number and team
	Optional<Player> findByJerseyNumberAndTeam(int jerseyNumber, Team team);

	// For finding all players by team ID
	List<Player> findByTeam_TeamId(Long teamId);

	// Custom query example if you need more complex logic
	@Query("SELECT p FROM Player p WHERE p.team.teamId = :teamId AND p.jerseyNumber = :jerseyNumber")
	Optional<Player> findPlayerByTeamAndJersey(@Param("teamId") Long teamId, @Param("jerseyNumber") int jerseyNumber);
	
	
}