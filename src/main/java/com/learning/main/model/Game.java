package com.learning.main.model;

 
import java.time.LocalDateTime;
import java.util.Set;

import com.learning.main.enm.GameStatus;
 

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gameId;

	@ManyToOne
	@JoinColumn(name = "home_team_id")
	private Team homeTeam;

	@ManyToOne
	@JoinColumn(name = "away_team_id")
	private Team awayTeam;

	private LocalDateTime gameDateTime;
	private String venue;
	private Integer homeScore;
	private Integer awayScore;

	@Enumerated(EnumType.STRING)
	private GameStatus status;

	@OneToMany(mappedBy = "game")
	private Set<GameStatistics> statistics;

	@OneToMany(mappedBy = "game")
	private Set<Ticket> tickets;

	@OneToMany(mappedBy = "game")
	private Set<Media> media;

	//GOF Pattern: Observer Pattern (suggested usage)
	// Services can register listeners to update scores, notify fans, or track
	// ticket sales when Game status changes.

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public LocalDateTime getGameDateTime() {
		return gameDateTime;
	}

	public void setGameDateTime(LocalDateTime gameDateTime) {
		this.gameDateTime = gameDateTime;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public Integer getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(Integer homeScore) {
		this.homeScore = homeScore;
	}

	public Integer getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(Integer awayScore) {
		this.awayScore = awayScore;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Set<GameStatistics> getStatistics() {
		return statistics;
	}

	public void setStatistics(Set<GameStatistics> statistics) {
		this.statistics = statistics;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Set<Media> getMedia() {
		return media;
	}

	public void setMedia(Set<Media> media) {
		this.media = media;
	}

	public Game() {
		super();
	}

}