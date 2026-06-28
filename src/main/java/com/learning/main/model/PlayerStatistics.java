package com.learning.main.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
//This class represents a core business entity, holding data and relationships relevant to player statistics.
	// Data Mapper Pattern (via JPA annotations)
	// JPA annotations are used to map this object to the 'player_statistics' table in the database.
	
@Entity
@Table(name = "player_statistics")
public class PlayerStatistics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long statId;

	@ManyToOne
	@JoinColumn(name = "player_id")
	private Player player;

	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;

	private int points;
	private int rebounds;
	private int assists;
	private int steals;
	private int blocks;
	private int turnovers;
	private int fouls;
	private int minutesPlayed;
	private int fieldGoalsAttempted;
	private int fieldGoalsMade;
	private int threePointsAttempted;
	private int threePointsMade;
	private int freeThrowsAttempted;
	private int freeThrowsMade;

	public Long getStatId() {
		return statId;
	}

	public void setStatId(Long statId) {
		this.statId = statId;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getRebounds() {
		return rebounds;
	}

	public void setRebounds(int rebounds) {
		this.rebounds = rebounds;
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

	public int getMinutesPlayed() {
		return minutesPlayed;
	}

	public void setMinutesPlayed(int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

	public int getFieldGoalsAttempted() {
		return fieldGoalsAttempted;
	}

	public void setFieldGoalsAttempted(int fieldGoalsAttempted) {
		this.fieldGoalsAttempted = fieldGoalsAttempted;
	}

	public int getFieldGoalsMade() {
		return fieldGoalsMade;
	}

	public void setFieldGoalsMade(int fieldGoalsMade) {
		this.fieldGoalsMade = fieldGoalsMade;
	}

	public int getThreePointsAttempted() {
		return threePointsAttempted;
	}

	public void setThreePointsAttempted(int threePointsAttempted) {
		this.threePointsAttempted = threePointsAttempted;
	}

	public int getThreePointsMade() {
		return threePointsMade;
	}

	public void setThreePointsMade(int threePointsMade) {
		this.threePointsMade = threePointsMade;
	}

	public int getFreeThrowsAttempted() {
		return freeThrowsAttempted;
	}

	public void setFreeThrowsAttempted(int freeThrowsAttempted) {
		this.freeThrowsAttempted = freeThrowsAttempted;
	}

	public int getFreeThrowsMade() {
		return freeThrowsMade;
	}

	public void setFreeThrowsMade(int freeThrowsMade) {
		this.freeThrowsMade = freeThrowsMade;
	}

	public PlayerStatistics() {
		super();
		// TODO Auto-generated constructor stub
	}

}