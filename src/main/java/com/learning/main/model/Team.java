package com.learning.main.model;

import java.time.LocalDate;
import java.util.Set;

// Remove unused import if present: import javax.management.relation.Role;
// Make sure all necessary jakarta.persistence imports are present
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long teamId;

	@Column(nullable = false, unique = true)
	private String teamName;

	private String city;
	private String homeArena;
	private LocalDate foundingDate;
	private String logoImage;

	// Add these new fields for team performance/record
	private Integer totalWins;
	private Integer totalLosses;
	private Integer homeWins;
	private Integer homeLosses;
	private Integer awayWins;
	private Integer awayLosses;
	private String conferenceStanding; // Assuming this is a String like "1st", "2nd", etc. or "Eastern Conference,
										// 5th"

	@OneToMany(mappedBy = "team")
	private Set<Player> players;

	@OneToMany(mappedBy = "team")
	private Set<Staff> staffMembers;

	@OneToMany(mappedBy = "homeTeam")
	private Set<Game> homeGames;

	@OneToMany(mappedBy = "awayTeam")
	private Set<Game> awayGames;

	@OneToMany(mappedBy = "team")
	private Set<Sponsorship> sponsorships;

	// Constructors
	public Team() {
		super();
	}

	// You will need to add constructors with fields if you use them,
	// and importantly, all the getters and setters for all fields.
	// For example:

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHomeArena() {
		return homeArena;
	}

	public void setHomeArena(String homeArena) {
		this.homeArena = homeArena;
	}

	public LocalDate getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(LocalDate foundingDate) {
		this.foundingDate = foundingDate;
	}

	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	// Getters and Setters for the NEW fields
	public Integer getTotalWins() {
		return totalWins;
	}

	public void setTotalWins(Integer totalWins) {
		this.totalWins = totalWins;
	}

	public Integer getTotalLosses() {
		return totalLosses;
	}

	public void setTotalLosses(Integer totalLosses) {
		this.totalLosses = totalLosses;
	}

	public Integer getHomeWins() {
		return homeWins;
	}

	public void setHomeWins(Integer homeWins) {
		this.homeWins = homeWins;
	}

	public Integer getHomeLosses() {
		return homeLosses;
	}

	public void setHomeLosses(Integer homeLosses) {
		this.homeLosses = homeLosses;
	}

	public Integer getAwayWins() {
		return awayWins;
	}

	public void setAwayWins(Integer awayWins) {
		this.awayWins = awayWins;
	}

	public Integer getAwayLosses() {
		return awayLosses;
	}

	public void setAwayLosses(Integer awayLosses) {
		this.awayLosses = awayLosses;
	}

	public String getConferenceStanding() {
		return conferenceStanding;
	}

	public void setConferenceStanding(String conferenceStanding) {
		this.conferenceStanding = conferenceStanding;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Set<Staff> getStaffMembers() {
		return staffMembers;
	}

	public void setStaffMembers(Set<Staff> staffMembers) {
		this.staffMembers = staffMembers;
	}

	public Set<Game> getHomeGames() {
		return homeGames;
	}

	public void setHomeGames(Set<Game> homeGames) {
		this.homeGames = homeGames;
	}

	public Set<Game> getAwayGames() {
		return awayGames;
	}

	public void setAwayGames(Set<Game> awayGames) {
		this.awayGames = awayGames;
	}

	public Set<Sponsorship> getSponsorships() {
		return sponsorships;
	}

	public void setSponsorships(Set<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}
}