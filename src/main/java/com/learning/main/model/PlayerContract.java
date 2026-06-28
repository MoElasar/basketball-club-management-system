package com.learning.main.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.learning.main.enm.ContractType;
import com.learning.main.enm.GameStatus;
import com.learning.main.enm.MediaType;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "player_contracts")
public class PlayerContract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contractId;

	@ManyToOne
	@JoinColumn(name = "player_id")
	private Player player;

	private LocalDate startDate;
	private LocalDate endDate;
	private double salary;
	private double bonus;

	@Enumerated(EnumType.STRING)
	private ContractType contractType;

	private String clauses;

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
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

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public String getClauses() {
		return clauses;
	}

	public void setClauses(String clauses) {
		this.clauses = clauses;
	}

	public String getStatus() {
		LocalDate today = LocalDate.now();

		if (endDate != null && endDate.isBefore(today)) {
			return "Expired";
		} else if (startDate != null && startDate.isAfter(today)) {
			return "Upcoming";
		} else {
			return "Active";
		}
	}

}