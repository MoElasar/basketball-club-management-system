package com.learning.main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_statistics")
public class GameStatistics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long statId;

	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;

	// Team statistics
	private int totalPoints;
	private int fieldGoalsMade;
	private int fieldGoalsAttempted;
	private int threePointsMade;
	private int threePointsAttempted;
	private int freeThrowsMade;
	private int freeThrowsAttempted;
	private int totalRebounds;
	private int offensiveRebounds;
	private int defensiveRebounds;
	private int assists;
	private int steals;
	private int blocks;
	private int turnovers;
	private int fouls;

	// Additional game metrics
	private int leadChanges;
	private int timesTied;
	private int largestLead;

	// Constructors
	public GameStatistics() {
	}

	public GameStatistics(Game game) {
		this.game = game;
	}

	// Getters and Setters
	public Long getStatId() {
		return statId;
	}

	public void setStatId(Long statId) {
		this.statId = statId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public int getFieldGoalsMade() {
		return fieldGoalsMade;
	}

	public void setFieldGoalsMade(int fieldGoalsMade) {
		this.fieldGoalsMade = fieldGoalsMade;
	}

	public int getFieldGoalsAttempted() {
		return fieldGoalsAttempted;
	}

	public void setFieldGoalsAttempted(int fieldGoalsAttempted) {
		this.fieldGoalsAttempted = fieldGoalsAttempted;
	}

	public int getThreePointsMade() {
		return threePointsMade;
	}

	public void setThreePointsMade(int threePointsMade) {
		this.threePointsMade = threePointsMade;
	}

	public int getThreePointsAttempted() {
		return threePointsAttempted;
	}

	public void setThreePointsAttempted(int threePointsAttempted) {
		this.threePointsAttempted = threePointsAttempted;
	}

	public int getFreeThrowsMade() {
		return freeThrowsMade;
	}

	public void setFreeThrowsMade(int freeThrowsMade) {
		this.freeThrowsMade = freeThrowsMade;
	}

	public int getFreeThrowsAttempted() {
		return freeThrowsAttempted;
	}

	public void setFreeThrowsAttempted(int freeThrowsAttempted) {
		this.freeThrowsAttempted = freeThrowsAttempted;
	}

	public int getTotalRebounds() {
		return totalRebounds;
	}

	public void setTotalRebounds(int totalRebounds) {
		this.totalRebounds = totalRebounds;
	}

	public int getOffensiveRebounds() {
		return offensiveRebounds;
	}

	public void setOffensiveRebounds(int offensiveRebounds) {
		this.offensiveRebounds = offensiveRebounds;
	}

	public int getDefensiveRebounds() {
		return defensiveRebounds;
	}

	public void setDefensiveRebounds(int defensiveRebounds) {
		this.defensiveRebounds = defensiveRebounds;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getSteals() {
		return steals;
	}

	public void setSteals(int steals) {
		this.steals = steals;
	}

	public int getBlocks() {
		return blocks;
	}

	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}

	public int getTurnovers() {
		return turnovers;
	}

	public void setTurnovers(int turnovers) {
		this.turnovers = turnovers;
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}

	public int getLeadChanges() {
		return leadChanges;
	}

	public void setLeadChanges(int leadChanges) {
		this.leadChanges = leadChanges;
	}

	public int getTimesTied() {
		return timesTied;
	}

	public void setTimesTied(int timesTied) {
		this.timesTied = timesTied;
	}

	public int getLargestLead() {
		return largestLead;
	}

	public void setLargestLead(int largestLead) {
		this.largestLead = largestLead;
	}

}