package com.learning.main.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//This class represents a core business entity, holding data and relationships relevant to staff contracts.
// Data Mapper Pattern (via JPA annotations)
// JPA annotations are used to map this object to the 'staff_contracts' table in the database.
@Entity
@Table(name = "staff_contracts")
public class StaffContract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contractId;

	@ManyToOne
	@JoinColumn(name = "staff_id")
	private Staff staff;

	private LocalDate startDate;
	private LocalDate endDate;
	private double salary;
	private String benefits;

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
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

	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}

	public StaffContract() {
		super();
		// TODO Auto-generated constructor stub
	}

	 

}