package com.learning.main.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

//This class represents a core business entity, holding data and relationships relevant to sponsorships.
// Data Mapper Pattern (via JPA annotations)
// JPA annotations are used to map this object to the 'sponsorships' table in the database.

@Entity
@Table(name = "sponsorships")
public class Sponsorship {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sponsorshipId;

	@ManyToOne
	@JoinColumn(name = "sponsor_id")
	private Sponsor sponsor;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	private LocalDate startDate;
	private LocalDate endDate;
	private double amount;
	private String benefits;
	private String agreementTerms;
	private String details; // Added missing field for 'getDetails()'
	private boolean active; // Added missing field for 'isActive()'

	// Getters and Setters
	public Long getSponsorshipId() {
		return sponsorshipId;
	}

	public void setSponsorshipId(Long sponsorshipId) {
		this.sponsorshipId = sponsorshipId;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}

	public String getAgreementTerms() {
		return agreementTerms;
	}

	public void setAgreementTerms(String agreementTerms) {
		this.agreementTerms = agreementTerms;
	}

	// New Getter and Setter for details
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	 
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}