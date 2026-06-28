package com.learning.main.model;

import java.time.LocalDate;
import java.util.Set;

import javax.management.relation.Role;

import com.learning.main.enm.Position;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "players")
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long playerId;

	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;

	@Enumerated(EnumType.STRING)
	private Position position;

	private String nationality;
	private int jerseyNumber;
	private double height; // in cm
	private double weight; // in kg
	private LocalDate joinDate;
	private String profileImage;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	@OneToMany(mappedBy = "player")
	private Set<PlayerContract> contracts;

	@OneToMany(mappedBy = "player")
	private Set<PlayerStatistics> statistics;

	@OneToMany(mappedBy = "player")
	private Set<PlayerInjury> injuries;

	@OneToMany(mappedBy = "player")
	private Set<PlayerAward> awards;

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public int getJerseyNumber() {
		return jerseyNumber;
	}

	public void setJerseyNumber(int jerseyNumber) {
		this.jerseyNumber = jerseyNumber;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Set<PlayerContract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<PlayerContract> contracts) {
		this.contracts = contracts;
	}

	public Set<PlayerStatistics> getStatistics() {
		return statistics;
	}

	public void setStatistics(Set<PlayerStatistics> statistics) {
		this.statistics = statistics;
	}

	public Set<PlayerInjury> getInjuries() {
		return injuries;
	}

	public void setInjuries(Set<PlayerInjury> injuries) {
		this.injuries = injuries;
	}

	public Set<PlayerAward> getAwards() {
		return awards;
	}

	public void setAwards(Set<PlayerAward> awards) {
		this.awards = awards;
	}

	public Player() {
		super();
		// TODO Auto-generated constructor stub
	}

}