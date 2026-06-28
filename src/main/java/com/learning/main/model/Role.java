package com.learning.main.model;

import com.learning.main.enm.ERole;
import jakarta.persistence.*;
//This class represents a core business entity, holding data and relationships relevant to user roles.
	// Data Mapper Pattern (via JPA annotations)
	// JPA annotations are used to map this object to the 'roles' table in the database.
	
@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole name;

	// Constructors, getters, setters
	public Role() {
	}

	public Role(ERole name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}

}