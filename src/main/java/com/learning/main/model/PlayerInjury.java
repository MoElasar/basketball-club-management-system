package com.learning.main.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.learning.main.enm.GameStatus;
import com.learning.main.enm.InjuryStatus;
import com.learning.main.enm.MediaType;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//This class represents a core business entity, holding data and relationships relevant to player injuries.
	// Data Mapper Pattern (via JPA annotations)
	// JPA annotations are used to map this object to the 'injuries' table in the database.
	
@Entity
@Table(name = "injuries")
public class PlayerInjury {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long injuryId;

	@ManyToOne
	@JoinColumn(name = "player_id")
	private Player player;

	private String injuryType;
	private LocalDate injuryDate;
	private LocalDate expectedRecoveryDate;

	@Enumerated(EnumType.STRING)
	private InjuryStatus status;

	private String treatmentPlan;
	private String notes;
	public Long getInjuryId() {
		return injuryId;
	}
	public void setInjuryId(Long injuryId) {
		this.injuryId = injuryId;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public String getInjuryType() {
		return injuryType;
	}
	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}
	public LocalDate getInjuryDate() {
		return injuryDate;
	}
	public void setInjuryDate(LocalDate injuryDate) {
		this.injuryDate = injuryDate;
	}
	public LocalDate getExpectedRecoveryDate() {
		return expectedRecoveryDate;
	}
	public void setExpectedRecoveryDate(LocalDate expectedRecoveryDate) {
		this.expectedRecoveryDate = expectedRecoveryDate;
	}
	public InjuryStatus getStatus() {
		return status;
	}
	public void setStatus(InjuryStatus status) {
		this.status = status;
	}
	public String getTreatmentPlan() {
		return treatmentPlan;
	}
	public void setTreatmentPlan(String treatmentPlan) {
		this.treatmentPlan = treatmentPlan;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public PlayerInjury() {
	    // Default constructor
	}

	// Add this constructor if you don't have it
	public PlayerInjury(Player player, String injuryType, LocalDate injuryDate, 
	                   LocalDate expectedRecoveryDate, InjuryStatus status) {
	    this.player = player;
	    this.injuryType = injuryType;
	    this.injuryDate = injuryDate;
	    this.expectedRecoveryDate = expectedRecoveryDate;
	    this.status = status;
	}
	
	
}