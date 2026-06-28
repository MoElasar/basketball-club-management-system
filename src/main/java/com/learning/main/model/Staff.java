package com.learning.main.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.learning.main.enm.GameStatus;
import com.learning.main.enm.MediaType;
import com.learning.main.enm.StaffRole;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//This class represents a core business entity, holding data and relationships relevant to staff.
// Data Mapper Pattern (via JPA annotations)
// JPA annotations are used to map this object to the 'staff' table in the database.

@Entity
@Table(name = "staff")
public class Staff {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long staffId;

	private String firstName;
	private String lastName;

	@Enumerated(EnumType.STRING)
	private StaffRole role;

	private LocalDate hireDate;
	private String specialization;
	private String profileImage;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	@OneToMany(mappedBy = "staff")
	private Set<StaffContract> contracts;

	public Staff() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
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

	public StaffRole getRole() {
		return role;
	}

	public void setRole(StaffRole role) {
		this.role = role;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
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

	public Set<StaffContract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<StaffContract> contracts) {
		this.contracts = contracts;
	}

}
