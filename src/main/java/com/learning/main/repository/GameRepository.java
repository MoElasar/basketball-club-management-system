package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.main.enm.GameStatus;
import com.learning.main.model.Game;
import com.learning.main.model.Team;
import java.time.*;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByHomeTeam(Team team);
    List<Game> findByAwayTeam(Team team);
    List<Game> findByStatus(GameStatus status);
    List<Game> findByGameDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Game> findByGameDateTimeAfterOrderByGameDateTimeAsc(LocalDateTime dateTime);

    Optional<Game> findByHomeTeamAndAwayTeamAndGameDateTime(Team homeTeam, Team awayTeam, LocalDateTime gameDateTime);
    @Query("SELECT g FROM Game g WHERE (g.homeTeam.teamId = :teamId OR g.awayTeam.teamId = :teamId) AND g.gameDateTime > :now ORDER BY g.gameDateTime ASC")
    List<Game> findUpcomingGamesForTeam(@Param("teamId") Long teamId, @Param("now") LocalDateTime now);

    List<Game> findByHomeTeam_TeamIdOrAwayTeam_TeamIdOrderByGameDateTime(Long homeTeamId, Long awayTeamId);
    
    List<Game> findByHomeTeam_TeamIdOrAwayTeam_TeamIdOrderByGameDateTimeAsc(Long homeTeamId, Long awayTeamId);


}